package com.ab.us.framework.web.handler;

import com.ab.us.framework.core.exception.BaseException;
import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.web.constant.GlobalExceptionInfo;
import com.ab.us.framework.web.response.BaseResponse;
import com.ab.us.framework.web.response.BodyResponse;
import com.ab.us.framework.web.response.ExceptionResponse;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.lang.reflect.UndeclaredThrowableException;

import static com.ab.us.framework.core.utils.StringUtil.SINGLE_SPLIT_LINE_STRING;
import static com.ab.us.framework.web.constant.GlobalExceptionInfo.*;

/**
 * 全局错误异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BodyResponse handlerMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        logger.error(e.getMessage(), e);
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        exceptionResponse.setCode(GlobalExceptionInfo.MISSING_PARAM.getCode());
        exceptionResponse.setMessage(StringUtil.replaceByIndex(GlobalExceptionInfo.MISSING_PARAM.getMessage(), e.getParameterName()));
        logger.info(SINGLE_SPLIT_LINE_STRING);
        logger.info(String.format("[ %-20S ] : %s", "response", JSONObject.toJSONString(exceptionResponse)));
        return exceptionResponse;
    }

    /**
     * 400 - 非法参数
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public BodyResponse handlerIllegalArgumentException(IllegalArgumentException e) {
        logger.error(e.getMessage(), e);
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        exceptionResponse.setCode(ILLEGAL_ARGUMENT.getCode());
        exceptionResponse.setMessage(ILLEGAL_ARGUMENT.getMessage());
        logger.info(SINGLE_SPLIT_LINE_STRING);
        logger.info(String.format("[ %-20S ] : %s", "response", JSONObject.toJSONString(exceptionResponse)));
        return exceptionResponse;
    }

    /**
     * 处理读取json异常的情况
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BodyResponse handlerHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("请求的RequestBody不可读");
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        exceptionResponse.setCode(ILLEGAL_BODY.getCode());
        exceptionResponse.setMessage(ILLEGAL_BODY.getMessage());
        logger.info(String.format("[ %-20S ] : %s", "response", JSONObject.toJSONString(exceptionResponse)));
        return exceptionResponse;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BodyResponse handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("不支持的请求方法");
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);


        exceptionResponse.setCode(ILLEGAL_REQUEST_METHOD.getCode());
        exceptionResponse.setMessage(StringUtil.replaceByIndex(ILLEGAL_REQUEST_METHOD.getMessage(), e.getMethod()));
        logger.info(String.format("[ %-20S ] : %s", "response", JSONObject.toJSONString(exceptionResponse)));
        return exceptionResponse;
    }


    @ExceptionHandler(Exception.class)
    public BodyResponse handlerAllException(Exception e) {
        BodyResponse exceptionResponse = null;
        // 如果实现了响应生成接口，调用此方法
        Exception exception = e;
        while (exception != null) {
            if (exception instanceof BaseException) {
                logger.error(exception.getMessage());
                exceptionResponse = new ExceptionResponse();
                exceptionResponse.setCode(((BaseException) exception).getCode());
                exceptionResponse.setMessage(exception.getMessage());
                break;
            }
            exception = (Exception) exception.getCause();
        }
        // 所有的异常原因都无法生成响应，生成系统正在维护中响应；
        if (exceptionResponse == null) {
            logger.error(e.getMessage(), e);
            exceptionResponse = new ExceptionResponse();
            exceptionResponse.setCode(BaseResponse.DEFAULT_SYSTEM_ERROR_RESPONSE.getCode());
            exceptionResponse.setMessage(BaseResponse.DEFAULT_SYSTEM_ERROR_RESPONSE.getMessage());
            logger.info("没有找到异常通用处理[{}]", e.getClass().getName());
        }
        logger.info(SINGLE_SPLIT_LINE_STRING);
        logger.info(String.format("[ %-20S ] : %s", "response", JSONObject.toJSONString(exceptionResponse)));
        return exceptionResponse;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public BodyResponse handlerNoHandlerFoundException(NoHandlerFoundException e) {
        logger.error(e.getMessage(), e);
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        exceptionResponse.setMessage("此接口不可用");
        logger.info(SINGLE_SPLIT_LINE_STRING);
        logger.info(String.format("[ %-20S ] : %s", "response", JSONObject.toJSONString(exceptionResponse)));
        return exceptionResponse;
    }

    @ExceptionHandler(UnsatisfiedServletRequestParameterException.class)
    public BodyResponse handlerHttpRequestMethodNotSupportedException(UnsatisfiedServletRequestParameterException e) {
        logger.error(e.getMessage(), e);
        ExceptionResponse exceptionResponse = new ExceptionResponse(e);
        exceptionResponse.setMessage("缺少请求的参数");
        logger.info(SINGLE_SPLIT_LINE_STRING);
        logger.info(String.format("[ %-20S ] : %s", "response", JSONObject.toJSONString(exceptionResponse)));
        return exceptionResponse;
    }

    @ExceptionHandler(JSONException.class)
    public BodyResponse handlerJSONException(JSONException jsonException) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(jsonException);
        exceptionResponse.setMessage("Json处理异常");
        logger.info(SINGLE_SPLIT_LINE_STRING);
        logger.info(String.format("[ %-20S ] : %s", "response", JSONObject.toJSONString(exceptionResponse)));
        return exceptionResponse;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public BodyResponse handlerJSONException(HttpMediaTypeNotSupportedException jsonException) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(jsonException);
        exceptionResponse.setMessage("不支持的请求类型");
        logger.info(SINGLE_SPLIT_LINE_STRING);
        logger.info(String.format("[ %-20S ] : %s", "response", JSONObject.toJSONString(exceptionResponse)));
        return exceptionResponse;
    }


    /**
     * 动态代理抛出的异常
     *
     * @param e 异常
     * @return 响应
     */
    @ExceptionHandler(UndeclaredThrowableException.class)
    public BodyResponse handlerUndeclaredThrowableException(UndeclaredThrowableException e) {
        return handlerAllException((Exception) e.getUndeclaredThrowable());
    }

}
