package com.ab.us.framework.service.push.request;

import com.ab.us.framework.service.push.domain.MessageTarget;

import java.util.List;

/**
 * 消息中心请求对象
 *
 * @author Zhongchongtao
 */
public class MessageRequest {
    private String              biz;
    private List<MessageTarget> ius;
    private int                 timeout;
    private int                 level;
    private int                 category;

    public String getBiz() {
        return biz;
    }

    public MessageRequest setBiz(String biz) {
        this.biz = biz;
        return this;
    }

    public List<MessageTarget> getIus() {
        return ius;
    }

    public MessageRequest setIus(List<MessageTarget> ius) {
        this.ius = ius;
        return this;
    }

    public int getTimeout() {
        return timeout;
    }

    public MessageRequest setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public int getLevel() {
        return level;
    }

    public MessageRequest setLevel(int level) {
        this.level = level;
        return this;
    }

    public int getCategory() {
        return category;
    }

    public MessageRequest setCategory(int category) {
        this.category = category;
        return this;
    }
}
