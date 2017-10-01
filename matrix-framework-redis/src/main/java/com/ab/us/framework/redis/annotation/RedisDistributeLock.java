package com.ab.us.framework.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Zhongchongtao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RedisDistributeLock {

    /**
     * 获得锁后，锁的有效期，单位是毫秒，默认为10 秒
     *
     * @return 过期时间
     */
    public long expireTime() default 10000L;

    /**
     * 超时时间，如果没有获得锁，重试的最长时间间隔
     *
     * @return 超时时间
     */
    public long timeout() default 10000L;

    /**
     * 如果没有取得锁，在此竞争锁的间隔时间，默认为0.5秒
     *
     * @return 休眠时间
     */
    public long sleepTime() default 500L;

    /**
     * 锁的名字
     *
     * @return 锁的名字
     */
    public String lockName() default "";

    /**
     * 锁的Key
     *
     * @return 锁的Key
     */
    public String lockKey();


}
