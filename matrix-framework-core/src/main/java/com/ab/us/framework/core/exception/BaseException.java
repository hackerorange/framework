package com.ab.us.framework.core.exception;


public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;// 接口响应码
    private String message;// 错误提示信息

    public BaseException() {
        this.code = -100999;
        this.message = "系统正在维护中";
    }

    public BaseException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }


    public BaseException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseException(Throwable ep) {
        super(ep);
    }

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


}
