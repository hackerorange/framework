package com.ab.us.framework.redis.constant;

import com.ab.us.framework.core.spring.SpringApplicationContextHolder;
import com.ab.us.framework.redis.executor.ExpireTimeCalculator;

/**
 * Created by ZhongChongtao on 2017/3/2.
 */
public enum ExpireType implements ExpireTimeCalculator {
    NEXT_DAY("nextDayExpireTimeCalculator"), STATIC("staticExpireTimeCalculator");

    private String expireTimeExecutor;

    ExpireType(String expireTimeExecutor) {
        this.expireTimeExecutor = expireTimeExecutor;
    }

    @Override
    public Long getExpireTime(Object object,Long def) {
        return SpringApplicationContextHolder.getBean(this.expireTimeExecutor, ExpireTimeCalculator.class).getExpireTime(object,def);
    }
}
