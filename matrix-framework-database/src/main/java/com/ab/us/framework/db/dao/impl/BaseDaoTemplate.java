package com.ab.us.framework.db.dao.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ab.us.framework.db.db.DBUtilsHelper;
import com.ab.us.framework.core.utils.DateConverter;
import com.ab.us.framework.db.util.PageInfo;

/**
 * @ClassName: DaoTemplate
 * @Description: TODO(Dao模板类,揭供DAO的公共模板)
 * @author xusisheng
 * @date 2017-02-06 09:09:33
 *
 */
public class BaseDaoTemplate {

	public static final Logger logger = LoggerFactory.getLogger(BaseDaoTemplate.class);

	private DataSource dataSource;

	public BaseDaoTemplate() {
		dataSource = null;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public long insert(String sql, Object... args) {
		int affectedRows = 0;
		try {
			if (args == null) {
				affectedRows = new DBUtilsHelper(dataSource).getRunner().update(sql);
			} else {
				affectedRows = new DBUtilsHelper(dataSource).getRunner().update(sql, args);
			}
		} catch (SQLException se) {
			logger.error("insert failed:" + sql, se);
		}
		return affectedRows;
	}

	/**
	 * 插入数据
	 *
	 * @param conn
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unused")
	public void insertByConnection(Connection conn, String sql) throws SQLException {
		int key = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			ParameterMetaData pmd = stmt.getParameterMetaData();
			stmt.executeUpdate();
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		} finally {
			closeDbAll(rs, stmt);
		}
	}

	/**
	 * 插入数据返回key
	 *
	 * @param conn
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public int insertForKeysByConnection(Connection conn, String sql, Object... args) throws SQLException {
		int key = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ParameterMetaData pmd = stmt.getParameterMetaData();
			if (args != null) {
				if (args.length < pmd.getParameterCount()) {
					throw new SQLException("参数错误:" + pmd.getParameterCount());
				}
				for (int i = 0; i < args.length; i++) {
					stmt.setObject(i + 1, args[i]);
				}
			}
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				key = rs.getInt(1);
			}
		} catch (Exception e) {
			throw new SQLException(e.getMessage());
		} finally {
			closeDbAll(rs, stmt);
		}
		return key;
	}

	/**
	 * 更新数据
	 *
	 * @param conn
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public int updateByConnection(Connection conn, String sql, Object... args) throws SQLException {
		int result = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			ParameterMetaData pmd = stmt.getParameterMetaData();
			if (args != null) {
				if (args.length < pmd.getParameterCount()) {
					throw new SQLException("参数错误:" + pmd.getParameterCount());
				}
				for (int i = 0; i < args.length; i++) {
					stmt.setObject(i + 1, args[i]);
				}
			}
			result = stmt.executeUpdate();
			if (result < 1) {
				throw new SQLException("sql:" + sql + ",params:" + args);
			}
		} catch (SQLException e) {
			logger.error("insertForKey.插入返回主键错误：" + sql, e);
			throw new SQLException(e);
		} finally {
			closeDbAll(rs, stmt);
		}
		return result;
	}

	/**
	 * 删除数据
	 *
	 * @param conn
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public int deleteByConnection(Connection conn, String sql, Object... args) throws SQLException {
		int result = 0;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			ParameterMetaData pmd = stmt.getParameterMetaData();
			if (args != null) {
				if (args.length < pmd.getParameterCount()) {
					throw new SQLException("参数错误:" + pmd.getParameterCount());
				}
				for (int i = 0; i < args.length; i++) {
					stmt.setObject(i + 1, args[i]);
				}
			}
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("insertForKey.插入返回主键错误：" + sql, e);
			throw new SQLException(e);
		} finally {
			closeDbAll(rs, stmt);
		}
		return result;
	}

	/**
	 * 更新数据
	 *
	 * @param <T>
	 *
	 * @param conn
	 * @param sql
	 * @param args
	 * @return entity.getClass(), SqlTools.getSelectSql(entity)
	 * @throws SQLException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> T getByConnection(Connection conn, Class<T> entityClass, String sql, Object... args) throws SQLException {
		T result = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			ParameterMetaData pmd = stmt.getParameterMetaData();
			if (args != null) {
				if (args.length < pmd.getParameterCount()) {
					throw new SQLException("参数错误:" + pmd.getParameterCount());
				}
				for (int i = 0; i < args.length; i++) {
					stmt.setObject(i + 1, args[i]);
				}
			}
			rs = stmt.executeQuery();
			result = (T) new BeanHandler(entityClass).handle(rs);
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			closeDbAll(rs, stmt);
		}
		return result;
	}

	public int[] batchUpdateByConnection(Connection conn, String sql, Object[][] params) throws SQLException {
		if (conn == null) {
			throw new SQLException("Null connection");
		}

		if (sql == null) {
			throw new SQLException("Null SQL statement");
		}

		if (params == null) {
			throw new SQLException("Null parameters. If parameters aren't need, pass an empty array.");
		}

		PreparedStatement stmt = null;
		int[] rows = null;
		try {
			stmt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				for (int j = 0; j < params[i].length; j++) {
					stmt.setObject(j + 1, params[i][j]);
				}
				stmt.addBatch();
			}
			rows = stmt.executeBatch();
		} catch (SQLException e) {
			throw e;
		} finally {
			closeDbAll(null, stmt);
		}
		return rows;
	}

	/**
	 * 查入数据返回自增长主键
	 *
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public int insertForKeys(String sql, Object... args) {
		int key = 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = new DBUtilsHelper(dataSource).getConnection();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ParameterMetaData pmd = stmt.getParameterMetaData();
			if (args != null) {
				if (args.length < pmd.getParameterCount()) {
					throw new SQLException("参数错误:" + pmd.getParameterCount());
				}
				for (int i = 0; i < args.length; i++) {
					stmt.setObject(i + 1, args[i]);
				}
			}
			stmt.executeUpdate();
			rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				key = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error("insertForKeys failed：" + sql, e);
		} finally {
			closeDbAll(rs, stmt, conn);
		}
		return key;
	}

	@SuppressWarnings("rawtypes")
	private ScalarHandler scalarHandler = new ScalarHandler() {
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			Object obj = super.handle(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}
	};

	/**
	 * 获取统计数量
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long count(String sql, Object... params) {
		Number num = null;
		try {
			if (params == null) {
				num = (Number) new DBUtilsHelper(dataSource).getRunner().query(sql, scalarHandler);
			} else {
				num = (Number) new DBUtilsHelper(dataSource).getRunner().query(sql, scalarHandler, params);
			}

		} catch (SQLException se) {
			logger.error("count.统计查询错误" + sql, se);
		}
		return (num == null) ? -1 : num.longValue();
	}

	/**
	 * 执行sql语句
	 *
	 * @param sql
	 * @return
	 */
	public long update(String sql) {
		return update(sql, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gongniu.live.dao.Dao#update(java.lang.String, java.lang.Object[])
	 */
	public long update(String sql, Object... args) {
		int affectedRows = 0;
		try {
			if (args == null) {
				affectedRows = new DBUtilsHelper(dataSource).getRunner().update(sql);
			} else {
				affectedRows = new DBUtilsHelper(dataSource).getRunner().update(sql, args);
			}

		} catch (SQLException se) {
			logger.error("update.修改错误:" + sql, se);
		}
		return affectedRows;
	}

	public Boolean execute(String sql) {
		boolean flag = true;
		try {
			new DBUtilsHelper(dataSource).getRunner().update(sql);
		} catch (SQLException e) {
			flag = false;
			logger.error("execute.修改错误:" + sql, e);
		}
		return flag;
	}

	public Boolean executeWithThrows(String sql) throws SQLException {
		boolean flag = true;
		try {
			new DBUtilsHelper(dataSource).getRunner().update(sql);
		} catch (SQLException e) {
			flag = false;
			logger.error("execute.修改错误:" + sql, e);
			throw e;
		}
		return flag;
	}

	/**
	 * 查询,将每行的结果放入MAP中,然后将MAP放入List中
	 *
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> find(String sql, Object[] params) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (params == null) {
				list = new DBUtilsHelper(dataSource).getRunner().query(sql, new MapListHandler());
			} else {
				list = new DBUtilsHelper(dataSource).getRunner().query(sql, new MapListHandler(), params);
			}
		} catch (SQLException e) {
			logger.error("map 数据查询错误", e);
		}
		return list;
	}

	/**
	 * sql查询,返回List<Map<String,Object>>
	 *
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> find(String sql) {
		return find(sql, null);
	}

	/**
	 * 分页查询
	 *
	 * @param sql
	 * @param page
	 * @param count
	 * @param args
	 * @return
	 */
	public List<Map<String, Object>> findPage(String sql, int page, int count, Object... params) {
		QueryRunner queryRunner = null;
		queryRunner = new DBUtilsHelper(dataSource).getRunner();
		StringBuilder contentSql = new StringBuilder();
		contentSql.append("SELECT * FROM ( SELECT A.*, ROWNUM RN FROM ( ");
		contentSql.append(sql);
		contentSql.append(") A WHERE ROWNUM <= ? ) WHERE RN >= ?");
		// 查询到的数据信息
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			if (params == null) {
				list = queryRunner.query(contentSql.toString(), new MapListHandler(), new Integer[] { page * count, (page - 1) * count + 1 });
			} else {
				list = queryRunner.query(contentSql.toString(), new MapListHandler(), ArrayUtils.addAll(params, new Integer[] { page * count, (page - 1) * count + 1 }), params);
			}

		} catch (SQLException se) {
			logger.error("map 数据查询错误", se);
		}
		return list;
	}

	/**
	 * 分页
	 *
	 * @param clazz
	 * @param selectState
	 * @param fromWhereState
	 * @param order
	 * @param page
	 * @param count
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> PageInfo<T> paginate(Class<T> clazz, String selectState, String fromWhereState, String order, int page, int count, Object... params) {
		ConvertUtils.register(new DateConverter(), java.util.Date.class);
		PageInfo<T> pageInfo = null;
		QueryRunner queryRunner = null;
		queryRunner = new DBUtilsHelper(dataSource).getRunner();
		StringBuilder countSql = new StringBuilder();
		StringBuilder contentSql = new StringBuilder();
		countSql.append(" select count(*) ");
		countSql.append(fromWhereState);
		contentSql.append("SELECT * FROM ( SELECT A.*, ROWNUM RN FROM ( ");
		contentSql.append(selectState + " " + fromWhereState + " order by " + order);
		contentSql.append(") A WHERE ROWNUM <= ? ) WHERE RN >= ?");
		// 总条数
		Number num = 0;
		// 总页数
		int totalPage = 0;
		// 查询到的数据信息
		List<T> list = new ArrayList<T>();
		try {
			if (params == null) {
				num = (Number) queryRunner.query(countSql.toString(), scalarHandler);
			} else {
				num = (Number) queryRunner.query(countSql.toString(), scalarHandler, params);
			}

			totalPage = num.intValue() / count;
			if (num.intValue() % count != 0) {
				totalPage++;
			}
			if (params == null) {
				list = (List<T>) queryRunner.query(contentSql.toString(), new BeanListHandler(clazz), new Integer[] { page * count, (page - 1) * count + 1 });
			} else {
				list = (List<T>) queryRunner.query(contentSql.toString(), new BeanListHandler(clazz),
						ArrayUtils.addAll(params, new Integer[] { page * count, (page - 1) * count + 1 }), params);
			}

		} catch (SQLException se) {
			logger.error("map 数据查询错误", se);
		}
		pageInfo = new PageInfo<T>(list, totalPage, num.intValue());

		return pageInfo;
	}

	/**
	 * 分页查询
	 *
	 * @param clazz
	 * @param selectState
	 * @param fromWhereState
	 * @param page
	 * @param count
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> PageInfo<T> paginate(Class<T> clazz, String selectState, String fromWhereState, int page, int count, Object... params) {
		ConvertUtils.register(new DateConverter(), java.util.Date.class);
		PageInfo<T> pageInfo = null;
		QueryRunner queryRunner = null;
		queryRunner = new DBUtilsHelper(dataSource).getRunner();
		StringBuilder countSql = new StringBuilder();
		StringBuilder contentSql = new StringBuilder();
		countSql.append(" select count(*) ");
		countSql.append(fromWhereState);
		contentSql.append("SELECT * FROM ( SELECT A.*, ROWNUM RN FROM ( ");
		contentSql.append(selectState + " " + fromWhereState);
		contentSql.append(") A WHERE ROWNUM <= ? ) WHERE RN >= ?");
		// 总条数
		Number num = 0;
		// 总页数
		int totalPage = 0;
		// 查询到的数据信息
		List<T> list = new ArrayList<T>();
		try {
			if (params == null) {
				num = (Number) queryRunner.query(countSql.toString(), scalarHandler);
			} else {
				num = (Number) queryRunner.query(countSql.toString(), scalarHandler, params);
			}

			totalPage = num.intValue() / count;
			if (num.intValue() % count != 0) {
				totalPage++;
			}
			if (params == null) {
				list = (List<T>) queryRunner.query(contentSql.toString(), new BeanListHandler(clazz), new Integer[] { page * count, (page - 1) * count + 1 });
			} else {
				list = (List<T>) queryRunner.query(contentSql.toString(), new BeanListHandler(clazz),
						ArrayUtils.addAll(params, new Integer[] { page * count, (page - 1) * count + 1 }), params);
			}

		} catch (SQLException se) {
			logger.error("map 数据查询错误", se);
		}
		pageInfo = new PageInfo<T>(list, totalPage, num.intValue());

		return pageInfo;
	}

	public <T> PageInfo<T> paginate(Class<T> clazz, String sql, int page, int count, Object... params) {
		ConvertUtils.register(new DateConverter(), java.util.Date.class);
		PageInfo<T> pageInfo = null;
		QueryRunner queryRunner = null;
		queryRunner = new DBUtilsHelper(dataSource).getRunner();
		StringBuilder countSql = new StringBuilder();
		StringBuilder contentSql = new StringBuilder();
		countSql.append(" SELECT COUNT(*) FROM (");
		countSql.append(sql);
		countSql.append(")");
		contentSql.append("SELECT * FROM ( SELECT A.*, ROWNUM RN FROM ( ");
		contentSql.append(sql);
		contentSql.append(") A WHERE ROWNUM <= ? ) WHERE RN >= ?");
		// 总条数
		Number num = 0;
		// 总页数
		int totalPage = 0;
		// 查询到的数据信息
		List<T> list = new ArrayList<T>();
		try {
			if (params == null) {
				num = (Number) queryRunner.query(countSql.toString(), scalarHandler);
			} else {
				num = (Number) queryRunner.query(countSql.toString(), scalarHandler, params);
			}

			totalPage = num.intValue() / count;
			if (num.intValue() % count != 0) {
				totalPage++;
			}
			if (params == null) {
				list = (List<T>) queryRunner.query(contentSql.toString(), new BeanListHandler(clazz), new Integer[] { page * count, (page - 1) * count + 1 });
			} else {
				list = (List<T>) queryRunner.query(contentSql.toString(), new BeanListHandler(clazz),
						ArrayUtils.addAll(params, new Integer[] { page * count, (page - 1) * count + 1 }), params);
			}

		} catch (SQLException se) {
			logger.error("map 数据查询错误", se);
		}
		pageInfo = new PageInfo<T>(list, totalPage, num.intValue());

		return pageInfo;
	}

	/**
	 * 分页查询
	 *
	 * @param clazz
	 * @param selectState
	 * @param fromWhereState
	 * @param page
	 * @param count
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> PageInfo<T> paginateAll(Class<T> clazz, String selectItems, String fromWhereState, int page, int count, Object... params) {
		return paginate(clazz, selectItems, fromWhereState, page, count, params);

	}

	/**
	 *
	 * @param <T>分页查询 ,返回对象LIST
	 * @param clazz
	 * @param sql
	 * @param page
	 * @param count
	 * @param params
	 * @return
	 */
	public <T> List<T> findPage(Class<T> clazz, String sql, int page, int pageSize, Object... params) {
		if (page <= 1) {
			page = 0;
		}
		return query(clazz, sql + " LIMIT ?,?", ArrayUtils.addAll(params, new Integer[] { page, pageSize }));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List<T> query(Class<T> clazz, String sql, Object... params) {
		try {
			return (List<T>) new DBUtilsHelper(dataSource).getRunner().query(sql, isPrimitive(clazz) ? columnListHandler : new BeanListHandler(clazz), params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void query(String sql, final Map<String, Map<String, Integer>> map, Object... params) {
		try {
			new DBUtilsHelper(dataSource).getRunner().query(sql, new ResultSetHandler() {

				@Override
				public Object handle(ResultSet rs) throws SQLException {
					Map tempMap = null;
					ResultSetMetaData metaData = rs.getMetaData();
					int index = 1;
					while (rs.next()) {
						index = 1;
						tempMap = new HashMap<>();
						String firstValue = String.valueOf(rs.getInt(index++));
						Integer secondValue = rs.getInt(index);
						tempMap.put(metaData.getColumnLabel(index++), secondValue);
						Integer thirdValue = rs.getInt(index);
						tempMap.put(metaData.getColumnLabel(index++), thirdValue);
						map.put(firstValue, tempMap);
					}
					return null;
				}
			}, params);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("serial")
	private List<Class<?>> PrimitiveClasses = new ArrayList<Class<?>>() {
		{
			add(Long.class);
			add(Integer.class);
			add(String.class);
			add(java.util.Date.class);
			add(java.sql.Date.class);
			add(java.sql.Timestamp.class);
		}
	};

	// 返回单一列时用到的handler
	@SuppressWarnings("rawtypes")
	private final static ColumnListHandler columnListHandler = new ColumnListHandler() {
		@Override
		protected Object handleRow(ResultSet rs) throws SQLException {
			Object obj = super.handleRow(rs);
			if (obj instanceof BigInteger)
				return ((BigInteger) obj).longValue();
			return obj;
		}
	};

	// 判断是否为原始类型
	private boolean isPrimitive(Class<?> cls) {
		return cls.isPrimitive() || PrimitiveClasses.contains(cls);
	}

	/**
	 * 查询,将每行返回的结果保存在bean中,然后将bean保存在list中.
	 *
	 * @param clazz
	 * @param sql
	 * @return
	 */
	public <T> List<T> find(Class<T> clazz, String sql) {
		return find(clazz, sql, null);

	}

	/**
	 *
	 * @param clazz 只能为基本类型的包装类
	 * @param sql
	 * @param columnName 要取的列的名字
	 * @return
	 */
	public <T> List<T> findForPrimitiveClass(Class<T> clazz, String sql, String columnName) {
		try {
			return new DBUtilsHelper(dataSource).getRunner().query(sql, new ColumnListHandler<T>(columnName));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 查询,将每行返回的结果保存在bean中,然后将bean保存在list中.
	 *
	 * @param clazz
	 * @param sql
	 * @param param
	 * @return
	 */
	public <T> List<T> find(Class<T> clazz, String sql, Object param) {
		return find(clazz, sql, new Object[] { param });
	}

	/**
	 * 查询,将每行返回的结果保存在bean中,然后将bean保存在list中.
	 *
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> find(Class<T> clazz, String sql, Object[] params) {
		List<T> list = new ArrayList<T>();
		try {
			if (params == null) {
				list = (List<T>) new DBUtilsHelper(dataSource).getRunner().query(sql, new BeanListHandler(clazz));
			} else {
				list = (List<T>) new DBUtilsHelper(dataSource).getRunner().query(sql, new BeanListHandler(clazz), params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Error occured while attempting to query data", e);
		}
		return list;
	}

	/**
	 * 查询出结果集中的第一条记录，并封装成对象
	 *
	 * @param clazz 类名
	 * @param sql sql语句
	 * @return 对象
	 */
	public <T> T findFirst(Class<T> clazz, String sql) {
		return findFirst(clazz, sql, null);
	}

	/**
	 * 查询出结果集中的第一条记录，并封装成对象
	 *
	 * @param clazz 类名
	 * @param sql sql语句
	 * @param param 参数
	 * @return 对象
	 */
	public <T> T findFirst(Class<T> clazz, String sql, Object param) {
		return findFirst(clazz, sql, new Object[] { param });
	}

	/**
	 * 查询第一条记录,交返回对象
	 *
	 * @param clazz
	 * @param sql
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T findFirst(Class<T> clazz, String sql, Object[] params) {
		Object object = null;
		try {
			if (params == null) {
				object = new DBUtilsHelper(dataSource).getRunner().query(sql, new BeanHandler(clazz));
			} else {
				object = new DBUtilsHelper(dataSource).getRunner().query(sql, new BeanHandler(clazz), params);
			}
		} catch (SQLException e) {
			logger.error("返回一条记录错误：findFirst" + e.getMessage());
			e.printStackTrace();
		}
		return (T) object;
	}

	/**
	 * 查询出结果集中的第一条记录，并封装成Map对象
	 *
	 * @param sql sql语句
	 * @return 封装为Map的对象
	 */
	public Map<String, Object> findFirst(String sql) {
		return findFirst(sql, null);
	}

	/**
	 * 查询出结果集中的第一条记录，并封装成Map对象
	 *
	 * @param sql sql语句
	 * @param params 参数数组
	 * @return 封装为Map的对象
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findFirst(String sql, Object[] params) {
		Map<String, Object> map = null;
		try {
			if (params == null) {
				map = new DBUtilsHelper(dataSource).getRunner().query(sql, new MapHandler());
			} else {
				map = new DBUtilsHelper(dataSource).getRunner().query(sql, new MapHandler(), params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("findFirst.查询一条记录错误" + sql, e);
		}
		return map;
	}

	/**
	 * 查询某一条记录，并将指定列的数据转换为Object
	 *
	 * @param sql sql语句 列名
	 * @return 结果对象
	 */
	public Object findBy(String sql, String params) {
		return findBy(sql, params, null);
	}

	/**
	 * 查询某一条记录，并将指定列的数据转换为Object
	 *
	 * @param sql sql语句
	 * @param columnName 列名
	 * @param params 参数数组
	 * @return 结果对象
	 */
	public Object findBy(String sql, String columnName, Object[] params) {
		Object object = null;
		try {
			if (params == null) {
				object = new DBUtilsHelper(dataSource).getRunner().query(sql, new ScalarHandler(columnName));
			} else {
				object = new DBUtilsHelper(dataSource).getRunner().query(sql, new ScalarHandler(columnName), params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("findBy。错误" + sql, e);
		}
		return object;
	}

	/**
	 * 查询某一条记录，并将指定列的数据转换为Object
	 *
	 * @param sql sql语句
	 * @param columnIndex 列索引
	 * @param params 参数数组
	 * @return 结果对象
	 */
	public Object findBy(String sql, int columnIndex, Object[] params) {
		Object object = null;
		try {
			if (params == null) {
				object = new DBUtilsHelper(dataSource).getRunner().query(sql, new ScalarHandler(columnIndex));
			} else {
				object = new DBUtilsHelper(dataSource).getRunner().query(sql, new ScalarHandler(columnIndex), params);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("findBy.错误" + sql, e);
		}
		return object;
	}

	/**
	 * 查询某一条记录，并将指定列的数据转换为Object
	 *
	 * @param sql sql语句
	 * @param columnIndex 列索引
	 * @return 结果对象
	 */
	public Object findBy(String sql, int columnIndex) {
		return findBy(sql, columnIndex, null);
	}

	/**
	 * 查询某一条记录，并将指定列的数据转换为Object
	 *
	 * @param sql sql语句
	 * @param columnIndex 列索引
	 * @param param 参数
	 * @return 结果对象
	 */
	public Object findBy(String sql, int columnIndex, Object param) {
		return findBy(sql, columnIndex, new Object[] { param });
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.gongniu.live.dao.Dao#batchUpdate(java.lang.String,
	 * java.lang.Object[][]) 批量修改记录
	 */
	public int[] batchUpdate(String sql, Object[][] objs) {
		int[] affectedRows = new int[0];
		try {
			affectedRows = new DBUtilsHelper(dataSource).getRunner().batch(sql, objs);
		} catch (SQLException se) {
			se.printStackTrace();
			logger.error("update.批次修改错误:" + sql, se);
		}
		return affectedRows;

	}

	/**
	 * 关闭DB相关连接
	 *
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	private void closeDbAll(ResultSet rs, Statement stmt) {
		if (rs != null) { // 关闭记录集
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) { // 关闭声明
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 关闭DB相关连接
	 *
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	private void closeDbAll(ResultSet rs, Statement stmt, Connection conn) {
		if (rs != null) { // 关闭记录集
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (stmt != null) { // 关闭声明
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) { // 关闭连接对象
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
