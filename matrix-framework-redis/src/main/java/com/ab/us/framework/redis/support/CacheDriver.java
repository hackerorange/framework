package com.ab.us.framework.redis.support;

import com.ab.us.framework.redis.model.CacheModel;

/**
 * 异步缓存更新驱动
 * 
 * @param <T>
 *            异步缓存的对象类型，继承自{@link com.ab.us.framework.redis.model.CacheModel}
 * @param <K>
 *            异步缓存主键类型
 */
public interface CacheDriver<T extends CacheModel<K>, K> {

//	/**
//	 * 获取缓存地址
//	 *
//	 * @return 缓存地址
//	 */
//	public String getCacheLocation();

	/**
	 * 从缓存中获取对象
	 * 
	 * @param key
	 *            主键ID
	 * @return 缓存中的对象
	 */
	public T getCacheModel(K key);

	/**
	 * 与缓存合并，并更新数据库
	 *
	 * @param synchronize
	 *            是否异步处理，如果需要异步，设置为true，否则，设置为false
	 * @param target
	 *            合并的对象
	 * @return 是否更新成功
	 */
	public boolean merge(Boolean synchronize, T target);

	/**
	 * 全量更新（此过程为异步操作，先保存到缓存中，后台同步数据库）
	 *
	 * @param target
	 *            要更新的对象
	 * @return 是否更新成功
	 */
	public boolean update(T target);

	/**
	 *
	 * @param key
	 *            要删除的对象的Key
	 * @return 是否删除成功
	 */
	public boolean delete(K key);

	/**
	 * 创建一个新的对象
	 * 
	 * @param target
	 *            要保存的对象
	 * @return 保存后的对象
	 */
	public T insert(T target);

}
