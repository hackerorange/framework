package com.ab.us.framework.redis.support;

import com.ab.us.framework.db.entity.BaseEntity;

/**
 * @author Zhongchongtao
 */
public interface KeySupport {

	/**
	 * 获取下一个用户主键
	 *
	 * @return 下一个用户主键ID
	 */
	public Long nextPk(Class<? extends BaseEntity> tClass);
}
