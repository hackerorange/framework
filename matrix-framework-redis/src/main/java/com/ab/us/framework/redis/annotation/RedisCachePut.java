package com.ab.us.framework.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * redis缓存设置
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisCachePut {

	/**
	 * 缓存路径
	 * 
	 * @return 缓存路径
	 */
	public String cacheName();

	/**
	 * 缓存的Key
	 * 
	 * @return 缓存的Key
	 */
	public String key();

	/**
	 * redisTemplate名称
	 * 
	 * @return 缓存使用的redisTemplate名称
	 */
	public String redisTemplate() default "";

	/**
	 * 是否是异步处理的方式
	 * 
	 * @return 是异步则为true，否则默认即可
	 */
	public boolean sync() default false;

}
