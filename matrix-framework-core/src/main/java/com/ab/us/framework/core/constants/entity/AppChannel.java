package com.ab.us.framework.core.constants.entity;

/**
 * App渠道号枚举，银保版或普通版
 * @author Zhongchongtao
 */
public enum AppChannel {
    BANK("B", "银保版"),
    CUSTOMER("C", "标准版");

    private String code;
    private String name;
    AppChannel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
