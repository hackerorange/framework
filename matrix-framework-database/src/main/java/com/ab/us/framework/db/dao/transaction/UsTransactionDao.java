package com.ab.us.framework.db.dao.transaction;

import java.sql.SQLException;

import com.ab.us.framework.db.entity.BaseEntity;

/**
 * @ClassName: UsTransactionDao
 * @Description: TODO(公牛事务处理接口)
 * @author xusisheng
 * @date 2017-02-21 10:34:40
 *
 */
public interface UsTransactionDao {

	public abstract int aopService(BaseEntity... BaseEntity) throws SQLException;

}
