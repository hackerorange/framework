package com.ab.us.framework.service.push.constant;

/**
 * Created by ZhongChongtao on 2017/5/8.
 * 推送的消息类型
 */
public enum MessageType {
    SYSTEM_MESSAGE("系统消息", 0), USER_MESSAGE("用户消息", 1);;
    private String desc;
    private int code;

    MessageType(String desc, int code) {

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
