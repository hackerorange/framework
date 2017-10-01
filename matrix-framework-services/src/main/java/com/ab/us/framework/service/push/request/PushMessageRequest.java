package com.ab.us.framework.service.push.request;

/**
 * Created by ZhongChongtao on 2017/5/8.
 */
public class PushMessageRequest extends MessageRequest {

    private String title;
    private String description;
    private Object custom_content;
    private int type;

    public PushMessageRequest() {
    }


    public String getTitle() {
        return title;
    }

    public PushMessageRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PushMessageRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Object getCustom_content() {
        return custom_content;
    }

    public PushMessageRequest setCustom_content(Object custom_content) {
        this.custom_content = custom_content;
        return this;
    }

    public int getType() {
        return type;
    }

    public PushMessageRequest setType(int type) {
        this.type = type;
        return this;
    }

}
