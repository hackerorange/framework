package com.ab.us.framework.db.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ab.us.framework.db.entity.DataSchema;
import com.alibaba.druid.pool.DruidDataSourceFactory;

/**
 * 数据库连接池，通过默认加载方式时，属性文件必须在资源目录中。
 *
 */
public class DBPool {

	private static Logger logger = LoggerFactory.getLogger(DBPool.class);
	private static Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();

	private static DBPool instance = null;

	/**
	 * 加载数据源，此方式通过Spring加载，只能执行一次！！！
	 *
	 * @param map
	 */
//	public static void init(Map<String, Map<String, String>> map) {
//		for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
//			logger.info("begin load " + entry.getKey() + ".properties");
//			try {
//				DataSource dataSource = DruidDataSourceFactory.createDataSource(entry.getValue());
//
//				// 数据源枚举中不存在，则直接用DB的大写名称作为KEY
//				boolean exists = false;
//				for (DataSchema schema : DataSchema.values()) {
//					if (entry.getKey().equalsIgnoreCase(schema.name())) {
//						dataSourceMap.put(schema.name(), dataSource);
//						exists = true;
//						break;
//					}
//				}
//				if (!exists) {
//					dataSourceMap.put(entry.getKey().toUpperCase(), dataSource);
//				}
//				logger.info("end load " + entry.getKey() + ".properties");
//			} catch (Exception e) {
//				logger.error(e.getMessage());
//			}
//		}
//	}

	private synchronized DataSource loadDbProperties(String dbName) {
		String dbconfig = dbName + ".properties";
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			inputStream = DBPool.class.getClassLoader().getResourceAsStream(dbconfig);
			if (inputStream == null) {
				logger.warn("load " + dbconfig + " failed");
				return null;
			}
			logger.info("begin load " + dbconfig);
			prop.load(inputStream);
			DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

			// 数据源枚举中不存在，则直接用DB的大写名称作为KEY
			boolean exists = false;
			for (DataSchema schema : DataSchema.values()) {
				if (dbName.equalsIgnoreCase(schema.name())) {
					dataSourceMap.put(schema.name(), dataSource);
					exists = true;
					break;
				}
			}
			if (!exists) {
				dataSourceMap.put(dbName.toUpperCase(), dataSource);
			}
			logger.info("end load " + dbconfig);
			return dataSource;

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
		return null;
	}

	private DBPool(){
	}

	public static synchronized DBPool getInstance(){
		if(instance == null){
			instance = new DBPool();
		}
		return instance;
	}

	public DataSource getDataSource(DataSchema code){
		DataSource source = dataSourceMap.get(code.name());
		if (source == null ) {
			source = loadDbProperties(code.name().toLowerCase());
		}
		return source;
	}

	/**
	 * 获取指定数据库的连接
	 *
	 * @param code
	 * @return
	 */
	public Connection getConnection(DataSchema code) {
		Connection conn = null;
		try {
			DataSource source = dataSourceMap.get(code.name());
			if (source == null ) {
				source = loadDbProperties(code.name().toLowerCase());
			}
			conn = source.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * 获取查询结果
	 *
	 * @param conn
	 * @param sql
	 * @return
	 */
	public static List<Map<String, Object>> SelectDBBase(Connection conn, String sql) {
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
		Statement statement = null;
		ResultSet rs = null;
		try {
			statement = conn.createStatement();
			rs = statement.executeQuery(sql);
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				int count = rsmd.getColumnCount();
				Map<String, Object> map = new HashMap<String, Object>();
				for (int index = 1; index <= count; index++) {
					String key = rsmd.getColumnLabel(index);
					Object value = rs.getObject(index);
					map.put(key, value);
				}
				resList.add(map);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return resList;
	}

	/**
	 *
	 * @param sql
	 * @param code
	 *            0,选股数据库;1,巨灵数据库;2,爬虫数据库
	 * @return
	 */
	public boolean ExecuteSql(String sql, DataSchema code) {
		boolean res = false;
		Connection conn = getConnection(code);
		Statement sm = null;
		try {
			sm = conn.createStatement();
			int count = sm.executeUpdate(sql);
			if (count > 0) {
				res = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (sm != null)
				try {
					sm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return res;
	}

	public int ExecuteSql(List<String> sqlList, DataSchema code) {
		int index = 0;
		Connection conn = getConnection(code);
		Statement sm = null;
		try {

			sm = conn.createStatement();
			for (String sql : sqlList) {
				try {
					int count = sm.executeUpdate(sql);
					index += count;
				} catch (Exception se) {
					System.out.println(se.getMessage());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (sm != null) {
				try {
					sm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return index;
	}

	public int[] ExecuteBatchSql(List<String> sqlList, DataSchema code) {
		int execNum[] = null;
		Connection conn = getConnection(code);
		Statement sm = null;
		try {
			int index = 0;
			conn.setAutoCommit(false);
			sm = conn.createStatement();
			for (String sql : sqlList) {
				sm.addBatch(sql);
				if (index > 0 && index % 10000 == 0) {
					execNum = sm.executeBatch();
					conn.commit();
				}
				index += 1;
			}
			// 最后插入不足1w条的数据
			conn.setAutoCommit(true);
			execNum = sm.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (sm != null) {
				try {
					sm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return execNum;
	}

	public long insertAndGetId(String sql, DataSchema code) {
		Connection conn = getConnection(code);
		PreparedStatement insertStatement = null;
		ResultSet generatedKeys = null;
		long insertId = 0;
		try {
			insertStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			insertStatement.execute();
			generatedKeys = insertStatement.getGeneratedKeys();
			if (generatedKeys.next()) {
				insertId = generatedKeys.getLong(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (generatedKeys != null)
				try {
					generatedKeys.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (insertStatement != null)
				try {
					insertStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return insertId;
	}

}
