package com.ab.us.framework.service.push.constant;

/**
 * 设备平台枚举信息
 * Created by ZhongChongtao on 2017/5/8.
 */
public enum DevicePlatform {
    IOS("苹果手机", "iOS"),
    ANDROID("安卓手机", "android");

    private String name;
    private String code;


    DevicePlatform(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
