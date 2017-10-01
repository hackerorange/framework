package com.ab.us.framework.service.push.domain;

/**
 * Created by ZhongChongtao on 2017/5/11.
 */
public class ShortMessageTarget implements MessageTarget {

    private String uid;
    private String mobile;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
