package com.ab.us.framework.redis.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisOperations;

import com.ab.us.framework.redis.annotation.CacheExpireTime;

/**
 * 自定义的redis缓存管理器
 * 支持方法上配置过期时间
 * 支持热加载缓存：缓存即将过期时主动刷新缓存
 *
 * @author zhongchongtao
 */
@SuppressWarnings("WeakerAccess")
public class CustomizedRedisCacheManager extends RedisCacheManager {

    private static final Map<String, CacheExpireTime> expireTimeMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(CustomizedRedisCacheManager.class);
    /**
     * 缓存主动在失效前强制刷新缓存的时间
     * 单位：秒
     */
    private long preloadSecondTime = 0;

    @SuppressWarnings("unused")
    public CustomizedRedisCacheManager(RedisOperations redisOperations) {
        super(redisOperations);
    }

    @SuppressWarnings("unused")
    public CustomizedRedisCacheManager(RedisOperations redisOperations, Collection<String> cacheNames) {
        super(redisOperations, cacheNames);
    }

    public static void addExpireTime(String cacheName, CacheExpireTime cacheExpireTime) {
        if (expireTimeMap.containsKey(cacheName)) {
            logger.warn("缓存过期时间重复定义,缓存名称为:" + cacheName);
            return;
        }
        synchronized (expireTimeMap) {
            if (expireTimeMap.containsKey(cacheName)) {
                logger.warn("缓存过期时间重复定义,缓存名称为:" + cacheName);
                return;
            }
            expireTimeMap.put(cacheName, cacheExpireTime);
        }
    }

    @Override
    public Cache getCache(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        //如果有设置
        if (expireTimeMap.containsKey(name)) {
            CacheExpireTime cacheExpireTime = expireTimeMap.get(name);
            Long expirationSecondTime = cacheExpireTime.expireType().getExpireTime("", cacheExpireTime.defaultExpireTime());
            //noinspection unchecked
            return new RedisCache(
                    name,//缓存名称
                    (this.isUsePrefix() ? this.getCachePrefix().prefix(name) : null),//是否使用前缀
                    this.getRedisOperations(),//StringRedisTemplate
                    expirationSecondTime//过期时间
            );
        }
        return super.getCache(name);
    }
}
