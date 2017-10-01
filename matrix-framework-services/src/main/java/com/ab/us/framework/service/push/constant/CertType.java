package com.ab.us.framework.service.push.constant;

/**
 * Created by ZhongChongtao on 2017/5/8.
 */
public enum CertType {
    ID("身份证", 1);

    private String name;
    private int code;

    CertType(String name, int code) {
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
