package com.ab.us.framework.web.response;

import com.ab.us.framework.core.exception.BaseException;

/**
 * 异常响应类型，默认值code为80 Created by ZhongChongtao on 2017/3/29.
 */
public class ExceptionResponse extends BodyResponse {

    public ExceptionResponse(Exception e) {
        code = -100999;
        message = e.getMessage();
    }

    /**
     * 生成系统异常响应信息
     */
    public ExceptionResponse() {
        code = -100999;
        message = "系统正在维护中";
    }

    /**
     * 根据 baseException 生成响应信息
     *
     * @param e baseException
     */
    public ExceptionResponse(BaseException e) {
        code = e.getCode();
        message = e.getMessage();
    }

}
