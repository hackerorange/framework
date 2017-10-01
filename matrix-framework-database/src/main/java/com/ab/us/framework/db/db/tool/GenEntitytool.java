package com.ab.us.framework.db.db.tool;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.handlers.MapListHandler;

import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.db.db.DBPool;
import com.ab.us.framework.db.db.DBUtilsHelper;
import com.ab.us.framework.db.entity.DataSchema;

/**
 * 数据库表转换成javaBean对象小工具(已用了很长时间), 1 bean属性按原始数据库字段经过去掉下划线,并大写处理首字母等等. 2
 * 生成的bean带了数据库的字段说明. 3 各位自己可以修改此工具用到项目中去.
 */
public class GenEntitytool {

	private static Pattern linePattern = Pattern.compile("_(\\w)");
	private static Pattern humpPattern = Pattern.compile("[A-Z]");

	private String classname = "";
	private String tablename = "";
	private String[] colnames; // 字段名
	private String[] colTypes; // 字段类型
	private int[] colPrecs; // 字段类型的精度的总长度
	private int[] colScale; // 字段类型的精度范围,小数点后的位数
	private String[] colClass; // 字段类型对应的Java类型
	private String[] colIsNull; // 字段是否可以为空
	private String[] colIsKey; // 字段是否主键
	private String[] colComm; // 字段注释

	private boolean impUtil = false;
	private boolean impMath = false;
	private boolean impTimestamp = false;
	private boolean impClob = false;
	private boolean impBlob = false;

	private String packageName = "";
	private Connection connect = null;
	private DataSchema dataSchema;

	public void setDataSchema(DataSchema dataSchema) {
		this.dataSchema = dataSchema;
		connect = DBPool.getInstance().getConnection(dataSchema);
	}

	public void setConnect(Connection connect) {
		this.connect = connect;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 表对象转实体类对象
	 *
	 * @param tablename 表名
	 * @param prefix 表名前缀
	 * @param sign 分隔符
	 */
	public void tableToEntity(String tablename, String prefix, String sign) {
		this.tablename = tablename;
		this.classname = convClassName(tablename, prefix, sign);

		try {
			StringBuffer sb = new StringBuffer(
					"SELECT t1.*, t2.CONSTRAINT_TYPE FROM( SELECT utc.COLUMN_NAME, utc.DATA_TYPE, CASE WHEN utc.DATA_PRECISION IS NULL THEN utc.DATA_LENGTH ELSE utc.DATA_PRECISION END AS DATA_PRECISION, utc.DATA_SCALE, utc.NULLABLE, ucc.COMMENTS, utc.COLUMN_ID FROM user_tab_columns utc INNER JOIN user_col_comments ucc ON ucc.TABLE_NAME = utc.TABLE_NAME AND ucc.COLUMN_NAME = utc.COLUMN_NAME WHERE utc.table_name = '");
			sb.append(tablename);
			sb.append(
					"') t1 LEFT JOIN( SELECT col.COLUMN_NAME, con.CONSTRAINT_TYPE FROM user_constraints con, user_cons_columns col WHERE con.CONSTRAINT_NAME = col.CONSTRAINT_NAME AND con.CONSTRAINT_TYPE = 'P' AND col.TABLE_NAME = '");
			sb.append(tablename);
			sb.append("') t2 ON t1.COLUMN_NAME = t2.COLUMN_NAME ORDER BY t1.COLUMN_ID ASC");
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			list = new DBUtilsHelper(DBPool.getInstance().getDataSource(dataSchema)).getRunner().query(sb.toString(), new MapListHandler());

			int size = list.size();
			colnames = new String[size];
			colTypes = new String[size];
			colPrecs = new int[size];
			colScale = new int[size];
			colClass = new String[size];
			colIsNull = new String[size];
			colIsKey = new String[size];
			colComm = new String[size];

			for (int i = 0; i < size; i++) {
				Map<String, Object> map = list.get(i);
				if (map == null)
					continue;

				colnames[i] = (String) map.get("COLUMN_NAME");
				colTypes[i] = (String) map.get("DATA_TYPE");
				colPrecs[i] = StringUtil.toIntegerWithZero(map.get("DATA_PRECISION"));
				colScale[i] = StringUtil.toIntegerWithZero(map.get("DATA_SCALE"));
				colClass[i] = sqlType2JavaType(colTypes[i], colPrecs[i], colScale[i]);
				colIsNull[i] = StringUtil.toStringWithBlank(map.get("NULLABLE"));
				colIsKey[i] = StringUtil.toStringWithBlank(map.get("CONSTRAINT_TYPE"));
				colComm[i] = StringUtil.toStringWithBlank(map.get("COMMENTS"));

				switch (colClass[i].toLowerCase()) {
				case "bigdecimal":
					impMath = true;
					break;
				case "date":
				case "datetime":
					impUtil = true;
					break;
				case "timestamp":
					impTimestamp = true;
					break;
				case "blob":
					impBlob = true;
					break;
				case "clob":
					impClob = true;
					break;
				default:
					break;
				}
			}

			String content = parse(colnames, colTypes, colPrecs);
			try {
				FileWriter fw = new FileWriter(classname + ".java");
				PrintWriter pw = new PrintWriter(fw);
				pw.println(content);
				pw.flush();
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析处理(生成实体类主体代码)
	 */
	private String parse(String[] colNames, String[] colTypes, int[] colSizes) {
		StringBuffer sb = new StringBuffer();
		sb.append(packageName + ";\r\n\r\n");

		if (impMath) {
			sb.append("import java.math.BigDecimal;\r\n");
		}
		if (impBlob) {
			sb.append("import java.sql.Blob;\r\n");
		}
		if (impClob) {
			sb.append("import java.sql.Clob;\r\n");
		}
		if (impTimestamp) {
			sb.append("import java.sql.Timestamp;\r\n");
		}
		if (impUtil) {
			sb.append("import java.util.Date;\r\n");
		}

		if (impMath || impBlob || impClob || impTimestamp || impUtil) {
			sb.append("\r\n");
		}

		sb.append("import com.ab.us.framework.db.annotation.FieldAnnotation;\r\n");
		sb.append("import com.ab.us.framework.db.annotation.FieldType;\r\n");
		sb.append("import com.ab.us.framework.db.annotation.TableAnnotation;\r\n");
		sb.append("import com.ab.us.framework.db.entity.BaseEntity;\r\n");
		sb.append("import com.ab.us.framework.db.entity.DataSchema;\r\n");
		sb.append("import com.ab.us.framework.db.entity.OrderBy;\r\n");

		// 表注释
		processColnames(sb);
		sb.append("@TableAnnotation(tableName = \"" + tablename + "\", schema = DataSchema." + dataSchema + ")\r\n");
		sb.append("public class " + classname + " extends OrderBy implements BaseEntity {\r\n");
		processAllAttrs(sb);
		processAllMethod(sb);
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 处理数据库列名
	 *
	 * @param sb
	 */
	private void processColnames(StringBuffer sb) {
		sb.append("\r\n/** " + tablename + "\r\n");
		String type = "";
		for (int i = 0; i < colnames.length; i++) {
			type = colTypes[i] + (colPrecs[i] <= 0 ? "" : (colScale[i] <= 0 ? "(" + colPrecs[i] + ")" : "(" + colPrecs[i] + "," + colScale[i] + ")"));
			sb.append("\t" + String.format("%-30s", colnames[i]) + String.format("%-18s", type) + colIsNull[i] + "\t\t" + colIsKey[i] + "\t\t" + colComm[i] + "\r\n");
		}
		sb.append("*/\r\n");
	}

	/**
	 * 解析输出属性
	 *
	 * @return
	 */
	private void processAllAttrs(StringBuffer sb) {
		for (int i = 0; i < colnames.length; i++) {
			String type = sqlType2JavaType(colTypes[i], colPrecs[i], colScale[i]);
			String fieldType = "FieldType.STRING";
			switch (type.toLowerCase()) {
			case "integer":
			case "long":
			case "double":
			case "float":
			case "decimal":
			case "bigdecimal":
				fieldType = "FieldType.NUMBER";
				break;
			default:
				fieldType = "FieldType." + type.toUpperCase();
				break;
			}
			String pk = "P".equalsIgnoreCase(colIsKey[i]) ? "true" : "false";
			sb.append("\t@FieldAnnotation(fieldName = \"" + colnames[i] + "\", fieldType = " + fieldType + ", pk = " + pk + ")\r\n");
			sb.append("\tprivate " + type + " " + lineToHump(colnames[i]) + ";\r\n");
		}
		sb.append("\r\n");
	}

	/**
	 * 生成所有的方法
	 *
	 * String name = lineToHump(tableName.replace(prefix, "")); return
	 * initcap(name);
	 *
	 * @param sb
	 */
	private void processAllMethod(StringBuffer sb) {
		for (int i = 0; i < colnames.length; i++) {
			String type = sqlType2JavaType(colTypes[i], colPrecs[i], colScale[i]);
			sb.append("\tpublic void set" + initcap(lineToHump(colnames[i])) + "(" + type + " " + lineToHump(colnames[i]) + "){\r\n");
			sb.append("\t\tthis." + lineToHump(colnames[i]) + " = " + lineToHump(colnames[i]) + ";\r\n");
			sb.append("\t}\r\n");
			sb.append("\tpublic " + type + " get" + initcap(lineToHump(colnames[i])) + "(){\r\n");
			sb.append("\t\treturn " + lineToHump(colnames[i]) + ";\r\n");
			sb.append("\t}\r\n");
		}
	}

	/**
	 * Oracle
	 *
	 * @param sqlType 数据库类型
	 * @param scale
	 * @return
	 */
	private String sqlType2JavaType(String sqlType, int size, int scale) {
		int pos = sqlType.indexOf("(");
		if(pos > 0) {
			sqlType = sqlType.substring(0, pos);
		}
		switch (sqlType.toLowerCase()) {
		case "binary_double":
			return "Double";
		case "binary_float":
			return "Float";
		case "blob":
			return "Blob";
		case "date":
			return "Date";
		case "timestamp":
		case "timestamptz":
			return "Timestamp";
		case "clob":
			return "Clob";
		case "char":
		case "nvarchar2":
		case "varchar2":
		case "long":
		case "rowid":
			return "String";
		case "number":
			// number长度 Java类型
			// 1~4 Short
			// 5~9 Integer
			// 10~18 Long
			// 18+ BigDecimal
			if (scale == 0) {
				if (size < 10) {
					return "Integer";
				} else if (size <= 18) {
					return "Long";
				}
			}
			return "BigDecimal";
		default:
			return "String";
		}
	}

	/**
	 * 查询所有表名
	 *
	 * @return 表名列表
	 */
	public List<String> getTableNames(String owner) {
		List<String> list = new ArrayList<String>();
		String sql = "SELECT TABLE_NAME FROM ALL_TABLES WHERE OWNER = '" + owner.toUpperCase() + "'";
		try {
			Statement stmt = connect.createStatement();
			ResultSet res = stmt.executeQuery(sql);
			while (res.next()) {
				list.add(res.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 表名转实体类名称
	 *
	 * @param tableName 表名
	 * @param prefix 前缀
	 * @param sign 分隔符，下划线
	 * @return 类名
	 * @note 分隔符使用下划线以外字符时不能使用lineToHump
	 */
	private String convClassName(String tableName, String prefix, String sign) {
		String name = lineToHump(tableName.replaceAll("^" + prefix, ""));
		return initcap(name);
	}

	/**
	 * 下划线转驼峰
	 *
	 * @param str
	 * @return
	 */
	public static String lineToHump(String str) {
		str = str.toLowerCase();
		Matcher matcher = linePattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 驼峰转下划线(简单写法，效率低于{@link #humpToLine2(String)})
	 *
	 * @param str
	 * @return
	 */
	public static String humpToLine(String str) {
		return str.replaceAll("[A-Z]", "_$0").toLowerCase();
	}

	/**
	 * 驼峰转下划线,效率比上面高
	 *
	 * @param str
	 * @return
	 */
	public static String humpToLine2(String str) {
		Matcher matcher = humpPattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 把输入字符串的首字母改成大写
	 */
	private String initcap(String str) {
		char[] ch = str.toCharArray();
		if (ch[0] >= 'a' && ch[0] <= 'z') {
			ch[0] = (char) (ch[0] - 32);
		}
		return new String(ch);
	}

	private String getSimpleName(String classname) {
		int pos = classname.lastIndexOf(".");
		return classname.substring(pos + 1);
	}
}