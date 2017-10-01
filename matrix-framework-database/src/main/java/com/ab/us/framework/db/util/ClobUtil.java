package com.ab.us.framework.db.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClobUtil {
	/**
	 *
	 * @param insertSQL
	 *            插入sql语句 有clob字段时，值必须设置成empty_clob()函数 例:insert into ba
	 *            valus(1,empty_clob())
	 * @param updateSQL
	 *            带有修改的查询语句,并应增加条件判断.例：select * from BA where ba_id =
	 *            '"+ba.getBA_id()+"' for update
	 * @param con
	 *            数据库链接
	 * @param bigString
	 *            要插入的clob值
	 * @param updateColumn
	 *            要插入的表字段名
	 * @return
	 * @throws SQLException
	 */
	public static Boolean clobInsert(String insertSQL, String updateSQL, Connection con, String bigString,
			String updateColumn) throws SQLException {
		// 结果集
		ResultSet rs = null;
		// 插入数据库的sql
		String query = insertSQL;
		// 设置不自动提交
		con.setAutoCommit(false);
		// 定义预处理
		java.sql.PreparedStatement pstmt = con.prepareStatement(query);
		// 执行插入语句
		pstmt.executeUpdate();
		// 清空
		pstmt = null;
		// 执行更改
		query = updateSQL;
		// 显示执行带有修改方式的select
		pstmt = con.prepareStatement(query);
		rs = pstmt.executeQuery();
		// 采用流的方式处理结果集
		if (rs.next()) {
			// 得到指定的clob字段
			oracle.sql.CLOB singnaturedateClob = (oracle.sql.CLOB) rs.getClob(updateColumn);
			// 把clob字段放到输出流当中
			BufferedOutputStream out = new BufferedOutputStream(singnaturedateClob.getAsciiOutputStream());
			// 判断传入的数据是否为空
			if (bigString != null) {
				try {
					// 把要保存的数据转换成输入流
					InputStream is = (new ByteArrayInputStream(bigString.getBytes()));
					copyStream(is, out);
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		rs.close();
		con.commit();

		return true;
	}

	/**
	 * 将输入流写入到输出流当中
	 *
	 * @param is
	 *            输入流
	 * @param os
	 *            输出流
	 * @throws IOException
	 */
	public static void copyStream(InputStream is, OutputStream os) throws IOException {
		byte[] data = new byte[4096];
		int readed = is.read(data);
		while (readed != -1) {
			os.write(data, 0, readed);
			readed = is.read(data);
		}
	}

	/**
	 * 通过Clob对象返回字符串
	 *
	 * @param c
	 * @return
	 */
	public static String getClobString(Clob c) {
		try {
			Reader reader = c.getCharacterStream();
			if (reader == null) {
				return null;
			}
			StringBuffer sb = new StringBuffer();
			char[] charbuf = new char[4096];
			for (int i = reader.read(charbuf); i > 0; i = reader.read(charbuf)) {
				sb.append(charbuf, 0, i);
			}
			return sb.toString();
		} catch (Exception e) {
			return "";
		}
	}
}
