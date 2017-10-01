package com.ab.us.framework.service.push.request;


/**
 * Created by ZhongChongtao on 2017/5/11.
 */
public class ShortMessageRequest extends MessageRequest {

    private String content;

    public String getContent() {
        return content;
    }

    public ShortMessageRequest setContent(String content) {
        this.content = content;
        return this;
    }
}
