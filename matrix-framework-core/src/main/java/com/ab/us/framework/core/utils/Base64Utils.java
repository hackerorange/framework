package com.ab.us.framework.core.utils;


public class Base64Utils {
    /**
     * 使用Base64加密算法加密字符串
     */
    public static String encodeStr(String plainText) {
        return org.springframework.util.Base64Utils.encodeToString(plainText.getBytes());
    }

    /**
     * 使用Base64加密
     */
    public static String decodeStr(String encodeStr) {
        return new String(org.springframework.util.Base64Utils.decodeFromString(encodeStr));
    }
}
