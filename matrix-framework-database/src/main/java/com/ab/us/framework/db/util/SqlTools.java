package com.ab.us.framework.db.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ab.us.framework.db.annotation.FieldAnnotation;
import com.ab.us.framework.db.annotation.FieldType;
import com.ab.us.framework.db.annotation.TableAnnotation;
import com.ab.us.framework.core.utils.DateUtil;

/**
 * @ClassName: SqlTools
 * @Description: TODO(根据实体生成SQL)
 * @author xusisheng
 * @date 2017年03月07日14:59:15
 *
 */
public class SqlTools {
	private static final Logger logger = LoggerFactory.getLogger(SqlTools.class);

	private static final String qrySymbolPostfix = "QrySymbol";
	private static final String symbolEqual = "=";

	/**
	 * 获取表名
	 *
	 * @param obj
	 * @return
	 */
	private static String getTableName(Object obj) {
		String tableName = null;
		if (obj.getClass().isAnnotationPresent(TableAnnotation.class)) {
			String postfix = obj.getClass().getAnnotation(TableAnnotation.class).postfix();
			String postfixVal = "";
			try {
				if (StringUtils.isNotBlank(postfix))
					postfixVal = (String) ClassUtils.getFieldValue(obj, obj.getClass().getDeclaredField(postfix));
			} catch (NoSuchFieldException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
			} catch (SecurityException e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
			}
			tableName = obj.getClass().getAnnotation(TableAnnotation.class).tableName()
					+ (StringUtils.isNotBlank(postfixVal) ? "_" + postfixVal : "");
		}
		return tableName;
	}

	/**
	 * 转换字段域值类型为字符串
	 *
	 * @param type
	 * @param object
	 * @return 字段值
	 */
	private static String convFiledString(FieldType type, Object object) {
		switch (type) {
		case NUMBER:
			return object.toString();
		case DATE:
			return "TO_DATE('" + DateUtil.dateToString((Date) object, "") + "', 'YYYY-MM-DD HH24:MI:SS')";
		case TIMESTAMP:
			return "TO_TIMESTAMP('" + DateUtil.dateToString((Date) object, "yyyy-MM-dd HH:mm:ss.SSSSSS")
					+ "', 'YYYY-MM-DD HH24:MI:SS:FF')";
		default:
			return "'" + object.toString() + "'";
		}
	}

	/**
	 *
	 * @Title: getInsertSql @Description: TODO(根据实体类对象字段的值生成INSERT SQL语句) @param
	 * obj @return String 返回类型 @throws
	 */
	public static String getInsertSql(Object obj) {
		return getInsertSql(obj, null);
	}

	/**
	 * @Title: getInsertSql @Description: TODO(根据实体类对象字段的值生成INSERT
	 * SQL语句,可选固定参数) @param obj @param fixedParams
	 * 固定参数(如该参数与实体类中有相同的字段,则忽略实体类中的对应字段,HashMap<String,
	 * Object>,key=指定字段名,value=对应字段的值) @return 设定文件 @return String 返回类型 @throws
	 */
	public static String getInsertSql(Object obj, HashMap<String, Object> fixedParams) {
		String insertSql = null;
		String tableName = getTableName(obj);
		if (tableName != null) {
			StringBuffer sqlStr = new StringBuffer("INSERT INTO " + tableName + " (");
			StringBuffer valueStr = new StringBuffer(" VALUES (");
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				for (Field field : annoFieldList) {
					FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
					Object fieldValue = ClassUtils.getFieldValue(obj, field);
					if (fieldValue != null) {
						if ((fixedParams != null) && (fixedParams.containsKey(anno.fieldName()))) {
							fieldValue = fixedParams.get(anno.fieldName());
						}
						sqlStr.append(anno.fieldName() + ", ");
						valueStr.append(convFiledString(anno.fieldType(), fieldValue) + ", ");
					}
				}
				insertSql = sqlStr.toString().substring(0, sqlStr.length() - 2) + ")"
						+ valueStr.toString().substring(0, valueStr.length() - 2) + ")";
			}
		}
		logger.debug("[getInsertSql:][" + insertSql + "]");
		return insertSql;
	}

	public static String getInsertPreStatementSql(Object obj, List<Object> params) {
		if (params == null) {
			logger.error("params can't is null");
			return null;
		}
		String insertSql = null;
		String tableName = getTableName(obj);
		if (tableName != null) {
			StringBuffer sqlStr = new StringBuffer("INSERT INTO " + tableName + " (");
			StringBuffer valueStr = new StringBuffer(" VALUES (");
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				for (Field field : annoFieldList) {
					FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
					if (!anno.pk()) {
						Object fieldValue = ClassUtils.getFieldValue(obj, field);
						if (fieldValue != null) {
							sqlStr.append(anno.fieldName() + ", ");
							valueStr.append("?, ");
							params.add(fieldValue);
						}
					}
				}
				insertSql = sqlStr.toString().substring(0, sqlStr.length() - 2) + ")"
						+ valueStr.toString().substring(0, valueStr.length() - 2) + ")";
			}
		}
		logger.debug("[getInsertPreStatementSql:][" + insertSql + "]");
		return insertSql;
	}

	/**
	 * 没有主键
	 *
	 * @param obj
	 * @param reqPk
	 * @param params
	 * @return
	 */
	public static String getUpdatePreStatementSql(Object obj, boolean reqPk, List<Object> params) {
		if (params == null) {
			logger.error("params can't is null ");
			return null;
		}
		String updateSql = null;
		String tableName = getTableName(obj);
		// 是否存在主键
		List<Object> whereParams = new ArrayList<>();
		if (tableName != null) {
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				StringBuffer sqlStr = new StringBuffer("UPDATE " + tableName);
				StringBuffer valueStr = new StringBuffer(" SET ");
				String whereStr = " WHERE ";
				for (Field field : annoFieldList) {
					Object fieldValue = ClassUtils.getFieldValue(obj, field);
					if (fieldValue != null) {
						FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
						if (reqPk && anno.pk()) {
							if (whereStr.contains("=")) {
								whereStr += " AND ";
							}
							whereStr += anno.fieldName() + " = ?";
							whereParams.add(fieldValue);
						} else {
							valueStr.append(anno.fieldName() + " = ?, ");
							params.add(fieldValue);
						}
					}
				}
				updateSql = sqlStr.toString() + valueStr.toString().substring(0, valueStr.length() - 2)
						+ ((whereStr.contains("=")) ? whereStr : "");
			}
		}
		params.addAll(whereParams);
		logger.debug("[getUpdatePreStatementSql:][" + updateSql + "]");
		return updateSql;
	}

	/**
	 *
	 *
	 * @param obj
	 * @param params
	 * @param conditionFields
	 *            要设置为where条件的字段
	 * @return
	 */
	public static String getUpdatePreStatementSql(Object obj, List<Object> params, String... conditionFields) {
		if (params == null) {
			logger.error("params can't is null ");
			return null;
		}
		if (conditionFields == null || conditionFields.length == 0) {
			logger.error("conditionFields can't is null ");
			return null;
		}
		String updateSql = null;
		String tableName = getTableName(obj);
		List<Object> whereParams = new ArrayList<>();
		Set<String> conditionFieldSet = new HashSet<>(Arrays.asList(conditionFields));

		if (tableName != null) {
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				StringBuffer sqlStr = new StringBuffer("UPDATE " + tableName);
				StringBuffer valueStr = new StringBuffer(" SET ");
				String whereStr = " WHERE ";
				for (Field field : annoFieldList) {
					Object fieldValue = ClassUtils.getFieldValue(obj, field);
					if (fieldValue != null) {
						FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
						if (conditionFieldSet.contains(anno.fieldName())) {
							if (whereStr.contains("=")) {
								whereStr += " AND ";
							}
							whereStr += anno.fieldName() + " = ?";
							whereParams.add(fieldValue);
						} else {
							valueStr.append(anno.fieldName() + " = ?, ");
							params.add(fieldValue);
						}

					}
				}
				updateSql = sqlStr.toString() + valueStr.toString().substring(0, valueStr.length() - 2) + whereStr;
			}
		}
		params.addAll(whereParams);
		logger.debug("[getUpdatePreStatementSql:][" + updateSql + "]");
		return updateSql;
	}

	/**
	 *
	 * @Title: getUpdateSql @Description: TODO(根据实体类对象字段的值生成UPDATE
	 * SQL语句,可选更新条件为主键,可选固定更新参数) @param obj @param reqPk
	 * 是否指定更新条件为主键(true=是,false=否) @param fixedParams
	 * 固定参数(如该参数与实体类中有相同的字段,则忽略实体类中的对应字段,HashMap<String,
	 * Object>,key=指定字段名,value=对应字段的值) @param conditionFields
	 * 要设置为where条件的字段 @return String 返回类型 @throws
	 */
	public static String getUpdateSql(Object obj, boolean reqPk, HashMap<String, Object> fixedParams,
			String... conditionFields) {
		String updateSql = null;
		Set<String> conditionFieldSet = null;
		String tableName = getTableName(obj);
		if (tableName != null) {
			if ((conditionFields != null) && (conditionFields.length > 0)) {
				conditionFieldSet = new HashSet<>(Arrays.asList(conditionFields));
			}
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				StringBuffer sqlStr = new StringBuffer("UPDATE " + tableName);
				StringBuffer valueStr = new StringBuffer(" SET ");
				String whereStr = " WHERE ";
				for (Field field : annoFieldList) {
					Object fieldValue = ClassUtils.getFieldValue(obj, field);
					if (fieldValue != null) {
						FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
						if (reqPk) {
							// 主键更新
							if (anno.pk()) {
								// 主键字段只做条件，不更新
								if (whereStr.contains("=")) {
									whereStr += " AND ";
								}
								whereStr += anno.fieldName() + " = " + convFiledString(anno.fieldType(), fieldValue);
							} else {
								if (valueStr.indexOf("=") > 0) {
									valueStr.append(", ");
								}
								if ((fixedParams != null) && (fixedParams.containsKey(anno.fieldName()))) {
									fieldValue = fixedParams.get(anno.fieldName());
								}
								valueStr.append(
										anno.fieldName() + " = " + convFiledString(anno.fieldType(), fieldValue));
							}
						} else if (conditionFieldSet != null) {
							// 条件更新，条件字段不参与更新
							if (conditionFieldSet.contains(anno.fieldName())) {
								if (whereStr.contains("=")) {
									whereStr += " AND ";
								}
								whereStr += anno.fieldName() + " = " + convFiledString(anno.fieldType(), fieldValue);
							} else {
								if (valueStr.indexOf("=") > 0) {
									valueStr.append(", ");
								}
								if ((fixedParams != null) && (fixedParams.containsKey(anno.fieldName()))) {
									fieldValue = fixedParams.get(anno.fieldName());
								}
								valueStr.append(
										anno.fieldName() + " = " + convFiledString(anno.fieldType(), fieldValue));
							}
						} else {
							// 无条件更新
							if (valueStr.indexOf("=") > 0) {
								valueStr.append(", ");
							}
							if ((fixedParams != null) && (fixedParams.containsKey(anno.fieldName()))) {
								fieldValue = fixedParams.get(anno.fieldName());
							}
							valueStr.append(anno.fieldName() + " = " + convFiledString(anno.fieldType(), fieldValue));
						}
					}
				}
				updateSql = sqlStr.toString() + valueStr.toString() + (whereStr.contains("=") ? whereStr : "");
				if (reqPk || (conditionFieldSet != null)) {
					if (!whereStr.contains("=")) {
						logger.warn("getUpdateSql: Have PK or condition Udate must have where keyword");
						return null;
					}
				}
			}
		}
		logger.debug("[getUpdateSql:][" + updateSql + "]");
		return updateSql;
	}

	/**
	 *
	 * @Title: getUpdateSql @Description: TODO(根据实体类对象字段的值生成UPDATE
	 * SQL语句,无条件) @param obj @return String 返回类型 @throws
	 */
	public static String getUpdateSql(Object obj) {
		return getUpdateSql(obj, false, null);
	}

	/**
	 *
	 * @Title: getUpdateSql @Description: TODO(根据实体类对象字段的值生成UPDATE
	 * SQL语句,可选更新条件为主键) @param obj @param reqPk
	 * 是否指定更新条件为主键(true=是,false=否) @return String 返回类型 @throws
	 */
	public static String getUpdateSql(Object obj, boolean reqPk) {
		return getUpdateSql(obj, reqPk, null);
	}

	/**
	 *
	 * @Title: getDeleteSql @Description: TODO(根据实体类对象字段的值生成有条件的DELETE
	 * SQL语句，可选主键为删除条件或使用各个字段的值为条件，多个条件用AND连接) @param obj @param reqPk
	 * 是否指定更新条件为主键(true=是,false=否) @return String 返回类型 @throws
	 */
	public static String getDeleteSql(Object obj, boolean reqPk) {
		String deleteSql = null;
		String tableName = getTableName(obj);
		if (tableName != null) {
			StringBuffer delSqlBuffer = new StringBuffer("DELETE FROM " + tableName);
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				int pos = 0;
				for (Field field : annoFieldList) {
					FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
					Object fieldValue = ClassUtils.getFieldValue(obj, field);
					if (fieldValue != null) {
						// 获取查询符号
						Object qrySymbol = ClassUtils.getFieldValue(obj, field.getName() + qrySymbolPostfix);
						if (StringUtils.isBlank((String) qrySymbol)) {
							qrySymbol = symbolEqual;
						}
						if (reqPk) {
							if (anno.pk()) {
								if (pos == 0) {
									delSqlBuffer.append(" WHERE ");
									delSqlBuffer.append(anno.fieldName() + " " + qrySymbol);
									pos = 1;
								} else {
									delSqlBuffer.append(" AND " + anno.fieldName() + " " + qrySymbol);
								}
								delSqlBuffer.append(" " + convFiledString(anno.fieldType(), fieldValue));
							}
						} else {
							if (pos == 0) {
								delSqlBuffer.append(" WHERE ");
								delSqlBuffer.append(anno.fieldName() + " " + qrySymbol);
								pos = 1;
							} else {
								delSqlBuffer.append(" AND " + anno.fieldName() + " " + qrySymbol);
							}
							delSqlBuffer.append(" " + convFiledString(anno.fieldType(), fieldValue));
						}
					}
				}
			}
			deleteSql = delSqlBuffer.toString();
		}
		logger.debug("[getDeleteSql:][" + deleteSql + "]");
		return deleteSql;
	}

	/**
	 *
	 * @Title: getDeleteSql @Description: TODO(根据实体类对象字段的值生成有条件的DELETE
	 * SQL语句，使用各个字段的值为条件，多个条件用AND连接) @param obj @return 设定文件 @return String
	 * 返回类型 @throws
	 */
	public static String getDeleteSql(Object obj) {
		return getDeleteSql(obj, true);
	}

	/**
	 * @Title: getSelectAllSql @Description: TODO(根据实体类对象字段的值生成SELECT
	 * SQL语句,无查询条件) @param obj @return 设定文件 @return String 返回类型 @throws
	 */
	public static String getSelectAllSql(Object obj) {
		String selectSql = null;
		String tableName = getTableName(obj);
		if (tableName != null) {
			StringBuffer selectBuffer = new StringBuffer("SELECT ");
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				for (Field field : annoFieldList) {
					FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
					if (!anno.isQueryField()) {
						selectBuffer.append(anno.fieldName() + " as " + field.getName() + ",");
					}
				}
				selectSql = selectBuffer.toString().substring(0, selectBuffer.length() - 1) + " FROM " + tableName;
			}
		}
		logger.debug("[getSelectSql:][" + selectSql + "]");
		return selectSql;
	}

	public static String getSelectCountSql(Object obj) {
		String selectSql = null;
		String tableName = getTableName(obj);
		if (tableName != null) {
			StringBuffer selectBuffer = new StringBuffer("SELECT count(1) as qryCount ");
			StringBuffer whereBuffer = new StringBuffer(" WHERE ");
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				int pos = 0;
				for (Field field : annoFieldList) {
					FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);

					Object fieldValue = ClassUtils.getFieldValue(obj, field);
					if (fieldValue != null) {
						// 获取查询符号
						Object qrySymbol = ClassUtils.getFieldValue(obj, field.getName() + qrySymbolPostfix);
						if (StringUtils.isBlank((String) qrySymbol)) {
							qrySymbol = symbolEqual;
						}
						if (pos == 0) {
							whereBuffer.append(anno.fieldName() + " " + qrySymbol);
							pos = 1;
						} else {
							whereBuffer.append(" AND " + anno.fieldName() + " " + qrySymbol);
						}
						whereBuffer.append(" " + convFiledString(anno.fieldType(), fieldValue));
					}
				}
				String whereSql = whereBuffer.toString().equals(" WHERE ") ? "" : whereBuffer.toString();
				selectSql = selectBuffer.toString().substring(0, selectBuffer.length() - 1) + " FROM " + tableName
						+ whereSql;
			}
		}
		logger.debug("[getSelectCountSql:][" + selectSql + "]");
		return selectSql;

	}

	public static String getSelectSql(Object obj) {
		String selectSql = null;
		String tableName = getTableName(obj);
		if (tableName != null) {
			StringBuffer selectBuffer = new StringBuffer("SELECT ");
			StringBuffer whereBuffer = new StringBuffer();
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				int pos = 0;
				for (Field field : annoFieldList) {
					FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
					if (!anno.isQueryField())
						selectBuffer.append(anno.fieldName() + " as " + field.getName() + ",");
					Object fieldValue = ClassUtils.getFieldValue(obj, field);
					if (fieldValue != null) {
						// 获取查询符号
						Object qrySymbol = ClassUtils.getFieldValue(obj, field.getName() + qrySymbolPostfix);
						if (StringUtils.isBlank((String) qrySymbol)) {
							qrySymbol = symbolEqual;
						}
						if (pos == 0) {
							whereBuffer.append(" WHERE ");
							whereBuffer.append(anno.fieldName() + " " + qrySymbol);
							pos = 1;
						} else {
							whereBuffer.append(" AND " + anno.fieldName() + " " + qrySymbol);
						}
						whereBuffer.append(" " + convFiledString(anno.fieldType(), fieldValue));
					}
				}
				String whereSql = whereBuffer.toString();
				selectSql = selectBuffer.toString().substring(0, selectBuffer.length() - 1) + " FROM " + tableName
						+ whereSql;
			}
		}
		// 获取order by
		try {
			Object orderBy = ClassUtils.getFieldValue(obj, "orderBy");
			if (StringUtils.isNotBlank((String) orderBy)) {
				selectSql += " order by " + orderBy;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("[getSelectSql:][" + selectSql + "]");

		return selectSql;
	}

	/**
	 * 获取查询数据项语句
	 *
	 * @param obj
	 * @return
	 */
	public static String getSelectItemSql(Object obj) {
		String selectSql = "";
		StringBuffer selectBuffer = new StringBuffer("SELECT ");
		List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
		if (annoFieldList != null && annoFieldList.size() > 0) {
			for (Field field : annoFieldList) {
				FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
				if (!anno.isQueryField()) {
					selectBuffer.append(anno.fieldName() + " as " + field.getName() + ",");
				}
			}
			selectSql = selectBuffer.toString().substring(0, selectBuffer.length() - 1) + "  ";
		}
		return selectSql;
	}

	public static String getOptimizeFromWhereSql(Object obj) {
		String selectSql = null;
		String tableName = getTableName(obj);
		if (tableName != null) {
			StringBuffer whereBuffer = new StringBuffer();
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				int pos = 0;
				for (Field field : annoFieldList) {
					FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);
					Object fieldValue = ClassUtils.getFieldValue(obj, field);
					if (fieldValue != null) {
						// 获取查询符号
						Object qrySymbol = ClassUtils.getFieldValue(obj, field.getName() + qrySymbolPostfix);
						if (StringUtils.isBlank((String) qrySymbol)) {
							qrySymbol = symbolEqual;
						}
						if (pos == 0) {
							whereBuffer.append(" WHERE ");
							whereBuffer.append(anno.fieldName() + " " + qrySymbol);
							pos = 1;
						} else {
							whereBuffer.append(" AND " + anno.fieldName() + " " + qrySymbol);
						}

						whereBuffer.append(" " + convFiledString(anno.fieldType(), fieldValue));
					}
				}
				// 获取order by
				try {
					Object orderBy = ClassUtils.getFieldValue(obj, "orderBy");
					if (StringUtils.isNotBlank((String) orderBy)) {
						whereBuffer.append(" order by " + orderBy);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				String whereSql = whereBuffer.toString();
				selectSql = " FROM " + tableName + whereSql;
			}
		}
		return selectSql;
	}

	public static String getFromWhereSql(Object obj) {
		String selectSql = null;
		String tableName = getTableName(obj);
		if (tableName != null) {
			StringBuffer whereBuffer = new StringBuffer();
			List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
			if (annoFieldList != null && annoFieldList.size() > 0) {
				int pos = 0;
				for (Field field : annoFieldList) {
					FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);

					Object fieldValue = ClassUtils.getFieldValue(obj, field);
					if (fieldValue != null) {
						// 获取查询符号
						Object qrySymbol = ClassUtils.getFieldValue(obj, field.getName() + qrySymbolPostfix);
						if (StringUtils.isBlank((String) qrySymbol)) {
							qrySymbol = symbolEqual;
						}
						if (pos == 0) {
							whereBuffer.append(" WHERE ");
							whereBuffer.append(anno.fieldName() + " " + qrySymbol);
							pos = 1;
						} else {
							whereBuffer.append(" AND " + anno.fieldName() + " " + qrySymbol);
						}

						whereBuffer.append(" " + convFiledString(anno.fieldType(), fieldValue));
					}
				}
				// 获取order by
				try {
					Object orderBy = ClassUtils.getFieldValue(obj, "orderBy");
					if (StringUtils.isNotBlank((String) orderBy)) {
						whereBuffer.append(" order by " + orderBy);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				String whereSql = whereBuffer.toString();
				selectSql = " FROM " + tableName + whereSql;
			}
		}
		return selectSql;
	}

	public static String getFromWhereItemSql(Object obj) {
		String selectSql = null;
		StringBuffer whereBuffer = new StringBuffer();
		List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
		if (annoFieldList != null && annoFieldList.size() > 0) {
			int pos = 0;
			for (Field field : annoFieldList) {
				FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);

				Object fieldValue = ClassUtils.getFieldValue(obj, field);
				if (fieldValue != null) {
					// 获取查询符号
					Object qrySymbol = ClassUtils.getFieldValue(obj, field.getName() + qrySymbolPostfix);
					if (StringUtils.isBlank((String) qrySymbol)) {
						qrySymbol = symbolEqual;
					}
					if (pos == 0) {
						whereBuffer.append(" ");
						whereBuffer.append(anno.fieldName() + " " + qrySymbol);
						pos = 1;
					} else {
						whereBuffer.append(" AND " + anno.fieldName() + " " + qrySymbol);
					}
					whereBuffer.append(" " + convFiledString(anno.fieldType(), fieldValue));
				}
			}
			if (StringUtils.isNotBlank(whereBuffer.toString())) {
				whereBuffer.insert(0, " WHERE");
			}
			// 获取order by
			try {
				Object orderBy = ClassUtils.getFieldValue(obj, "orderBy");
				if (StringUtils.isNotBlank((String) orderBy)) {
					whereBuffer.append(" order by " + orderBy);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			String whereSql = whereBuffer.toString();
			selectSql = whereSql;
		}
		return selectSql;
	}

	public static String getFromWhereCountItemSql(Object obj) {
		String selectSql = null;
		StringBuffer whereBuffer = new StringBuffer();
		List<Field> annoFieldList = ClassUtils.getAnnoFieldList(obj.getClass(), FieldAnnotation.class);
		if (annoFieldList != null && annoFieldList.size() > 0) {
			int pos = 0;
			for (Field field : annoFieldList) {
				FieldAnnotation anno = field.getAnnotation(FieldAnnotation.class);

				Object fieldValue = ClassUtils.getFieldValue(obj, field);
				if (fieldValue != null) {
					// 获取查询符号
					Object qrySymbol = ClassUtils.getFieldValue(obj, field.getName() + qrySymbolPostfix);
					if (StringUtils.isBlank((String) qrySymbol)) {
						qrySymbol = symbolEqual;
					}
					if (pos == 0) {
						whereBuffer.append(" ");
						whereBuffer.append(anno.fieldName() + " " + qrySymbol);
						pos = 1;
					} else {
						whereBuffer.append(" AND " + anno.fieldName() + " " + qrySymbol);
					}
					whereBuffer.append(" " + convFiledString(anno.fieldType(), fieldValue));
				}
			}
			if (StringUtils.isNotBlank(whereBuffer.toString())) {
				whereBuffer.insert(0, " WHERE");
			}
			String whereSql = whereBuffer.toString();
			selectSql = whereSql;
		}
		return selectSql;
	}
}
