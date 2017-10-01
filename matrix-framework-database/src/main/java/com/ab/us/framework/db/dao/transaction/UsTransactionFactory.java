package com.ab.us.framework.db.dao.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ab.us.framework.db.db.DBUtilsHelper;
import com.ab.us.framework.db.util.ClassUtils;

/**
 * @ClassName: UsTransactionFactory
 * @Description: TODO(事务DAO工厂类)
 * @author xusisheng
 * @date 2017-02-21 10:34:40
 *
 */
public class UsTransactionFactory implements InvocationHandler {

	private static final Logger log = LoggerFactory.getLogger(UsTransactionFactory.class);

	private static final String FIELD_NAME = "connect";
	private Object tarjectObject;
	private DataSource ds;

	public Object creatProxyInstance(Object obj, DataSource ds) {
		this.tarjectObject = obj;
		this.ds = ds;
		return Proxy.newProxyInstance(this.tarjectObject.getClass().getClassLoader(),
				this.tarjectObject.getClass().getInterfaces(), this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object,
	 * java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;
		Connection connect = null;
		DBUtilsHelper dbUtiles = null;
		UsTransactionDao transactionDao = null;
		try {
			transactionDao = (UsTransactionDao) this.tarjectObject;
			dbUtiles = new DBUtilsHelper(ds);
			connect = dbUtiles.getNotAutoCommitConnection();
			ClassUtils.setFieldVal(transactionDao, FIELD_NAME, connect);
			result = method.invoke(transactionDao, args);
			dbUtiles.connectionCommit(connect);
		} catch (Exception e) {
			if (dbUtiles != null)
				dbUtiles.connectionRollback(connect);
			log.error("batchTransaction.批次处理返回错误：", e);
			result = new Integer(0);
		} finally {
			if (dbUtiles != null)
				dbUtiles.closeConnection(connect);
		}
		return result;
	}

}
