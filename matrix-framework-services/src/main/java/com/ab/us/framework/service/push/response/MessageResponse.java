package com.ab.us.framework.service.push.response;

/**
 * 消息中心响应对象
 * Created by ZhongChongtao on 2017/5/11.
 */
public class MessageResponse {
    private int code;
    private String message;
    private String payload;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
