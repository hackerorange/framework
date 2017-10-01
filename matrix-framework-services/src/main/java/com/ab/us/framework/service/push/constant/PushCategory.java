package com.ab.us.framework.service.push.constant;

/**
 * Created by ZhongChongtao on 2017/5/8.
 * 推送消息分类
 */
public enum PushCategory {
    SIMPLE_MESSAGE("简单消息", 0),
    APP_MESSAGE("App消息", 1),;
    private String desc;
    private int code;

    PushCategory(String desc, int code) {
        this.desc = desc;
        this.code = code;

    }

    public String getDesc() {
        return desc;
    }

    public int getCode() {
        return code;
    }
}
