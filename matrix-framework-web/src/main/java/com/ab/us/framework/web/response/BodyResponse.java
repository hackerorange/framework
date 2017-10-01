package com.ab.us.framework.web.response;

import com.ab.us.framework.core.exception.BaseException;
import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.core.utils.TypeChecker;

/**
 * @author zhongchongtao
 * @since 3.0.0
 */
public class BodyResponse<T> implements BaseResponse {


    public static final String ILLEGAL_ARGUMENT_MESSAGE = "参数非法";
    protected int code = 200;
    protected String message = "操作成功";
    private T data;

    /**
     * 抛出异常信息
     *
     * @param base   异常枚举类型
     * @param params Message参数替换（根据枚举消息，确定替换的内容）
     */
    public static BaseException prepareException(BaseResponse base, String... params) {
        String message = TypeChecker.isEmpty(params) ? base.getMessage() : StringUtil.replaceByIndex(base.getMessage(), params);
        int code = base.getCode();
        return new BaseException(code, message);
    }

    /**
     * 准备有结果的相应信息信息
     *
     * @param base   响应基本信息
     * @param data   响应数据
     * @param params Message参数替换（根据枚举消息，确定替换的内容）
     * @return 相应结果
     */
    public static <T> BodyResponse<T> prepareResponse(BaseResponse base, T data, String... params) {
        BodyResponse<T> bodyResponse = new BodyResponse<>();
        bodyResponse.setData(data);
        bodyResponse.setMessage(base.getMessage());
        String message = TypeChecker.isEmpty(params) ? base.getMessage() : StringUtil.replaceByIndex(base.getMessage(), params);
        bodyResponse.setMessage(message);
        bodyResponse.setCode(base.getCode());
        return bodyResponse;
    }

    /**
     * 准备无结果的相应信息
     *
     * @param base   响应基本信息
     * @param params Message参数替换（根据枚举消息，确定替换的内容）
     * @return 相应结果
     */
    public static BodyResponse prepareNoResultResponse(BaseResponse base, String... params) {
        BodyResponse bodyResponse = new BodyResponse();
        String message = TypeChecker.isEmpty(params) ? base.getMessage() : StringUtil.replaceByIndex(base.getMessage(), params);
        bodyResponse.setMessage(message);
        bodyResponse.setCode(base.getCode());
        return bodyResponse;
    }

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public BodyResponse setData(T data) {
        this.data = data;
        return this;
    }
}
