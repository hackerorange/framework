package com.ab.us.framework.service.push.constant;

/**
 * Created by ZhongChongtao on 2017/5/11.
 */
public enum ShortMessageCategory {
    /**
     * 验证码
     */
    IDENTIFYING_CODE("验证码", 0),
    /**
     * 提醒短信
     */
    NOTICE_MESSAGE("提醒短信", 1),
    /**
     * 营销短信
     */
    BUSINESS_MESSAGE("营销短信", 2);

    private String name;
    private int code;

    ShortMessageCategory(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }


    public int getCode() {
        return code;
    }
}
