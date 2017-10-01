package com.ab.us.framework.redis.executor;

/**
 * @author Zhongchongtao
 */
public interface ExpireTimeCalculator {

    /**
     * 获取过期时间（单位:秒）
     *
     * @param args 相关参数
     * @param def  默认过期时间
     * @return 缓存剩余过期时间
     */
    public Long getExpireTime(Object args, Long def);

}
