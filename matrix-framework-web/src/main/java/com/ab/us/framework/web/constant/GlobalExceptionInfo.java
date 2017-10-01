package com.ab.us.framework.web.constant;


import com.ab.us.framework.web.response.BaseResponse;


/**
 * @author zhongchongtao
 */
public enum GlobalExceptionInfo implements BaseResponse {

    MISSING_PARAM(-100050, "缺少请求参数:{0}"),//
    ILLEGAL_ARGUMENT(-100060, "非法参数"),
    ILLEGAL_BODY(-100070, "请求JSON不可读"),
    ILLEGAL_REQUEST_METHOD(-100800, "不支持的请求请求类型[{0}]");


    private int code;
    private String message;

    GlobalExceptionInfo(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
