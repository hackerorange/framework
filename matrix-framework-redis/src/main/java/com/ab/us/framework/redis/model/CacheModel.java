package com.ab.us.framework.redis.model;

/**
 * 缓存管理模型，用于同步时，判断数据库版本和当前版本是否一致，一致，咋不进行更新操作
 */
public interface CacheModel<K> {

	/**
	 * 获取主键
	 * 
	 * @return 主键
	 */
	public abstract K getKey();

}
