package com.ab.us.framework.db.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ab.us.framework.db.entity.DataSchema;

/**
 * @ClassName: DBUtilsHelper
 * @Description: TODO(DB桥梁)
 * @author xusisheng
 * @date 2017-01-23 14:01:05
 *
 */
public class DBUtilsHelper {
	private static Logger logger = LoggerFactory.getLogger(DBUtilsHelper.class);

	private DataSource ds = null;
	private QueryRunner runner = null;

	public DBUtilsHelper(DataSource ds) {
		if (ds == null) {
			try {
				this.ds = DBPool.getInstance().getDataSource(DataSchema.UNIVERSESUN_COMMON);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			this.ds = ds;
		}
		this.runner = new QueryRunner(this.ds);
	}

	public QueryRunner getRunner() {
		return this.runner;
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			logger.error("getDataSourceConnection,获取连接出错.", e);
		}
		return conn;
	}

	public Connection getNotAutoCommitConnection() {
		Connection conn = null;
		try {
			conn = ds.getConnection();
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			logger.error("getDataSourceConnection,获取连接出错.", e);
		}
		return conn;
	}

	public void closeConnection(Connection conn) {
		if (conn != null) { // 关闭连接对象
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void connectionCommit(Connection conn) {
		if (conn != null) {
			try {
				conn.commit();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void connectionRollback(Connection conn) {
		if (conn != null) {
			try {
				conn.rollback();
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
