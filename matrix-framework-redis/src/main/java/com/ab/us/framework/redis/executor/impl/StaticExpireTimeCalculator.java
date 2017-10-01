package com.ab.us.framework.redis.executor.impl;

import com.ab.us.framework.redis.executor.ExpireTimeCalculator;

/**
 * 固定过期时间（秒）
 *
 * @author Zhongchongtao
 */
public class StaticExpireTimeCalculator implements ExpireTimeCalculator {

    @Override
    public Long getExpireTime(Object object, Long def) {
        return def;
    }
}
