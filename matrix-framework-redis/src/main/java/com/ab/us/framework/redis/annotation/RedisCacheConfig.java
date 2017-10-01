package com.ab.us.framework.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RedisCacheConfig {

	/**
	 * 缓存路径
	 * 
	 * @return 缓存的路径地址
	 */
	public String location() default "";

}
