package com.ab.us.framework.redis.executor;

import com.ab.us.framework.redis.model.CacheModel;

public interface DataBaseSynchronize<T extends CacheModel<K>, K> {

	/**
	 * 同步对象
	 * 
	 * @param t
	 *            要同步的对象
	 * @return 是否同步成功
	 */
	boolean Synchronize(T t);

	/**
	 * 根据主键，从数据库中获取记录
	 * 
	 * @param key
	 *            主键
	 * @return 从数据库中获取数据
	 */
	T getFromDatabase(K key);

}
