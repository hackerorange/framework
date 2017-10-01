package com.ab.us.framework.redis.annotation;

import com.ab.us.framework.redis.constant.ExpireType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ZhongChongtao on 2017/3/2.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheExpireTime {

    String[] cacheNames() default {};

    ExpireType expireType() default ExpireType.NEXT_DAY;

    long defaultExpireTime() default 0;
}
