package com.ab.us.framework.redis.executor.impl;

import com.ab.us.framework.redis.executor.ExpireTimeCalculator;

import java.util.Calendar;

/**
 * 每日0点自动更新
 *
 * @author Zhongchongtao
 */
public class NextDayExpireTimeCalculator implements ExpireTimeCalculator {
    @Override
    public Long getExpireTime(Object object,Long def) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        return (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }
}
