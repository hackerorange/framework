package com.ab.us.framework.web.response;

public interface BaseResponse {

    /**
     * 成功响应结果
     */
    BaseResponse DEFAULT_SUCCESS_RESPONSE = new BaseResponse() {
        @Override
        public String getMessage() {
            return "操作成功";
        }

        @Override
        public int getCode() {
            return 100000;
        }
    };

    /**
     * 系统错误响应结果
     */
    BaseResponse DEFAULT_SYSTEM_ERROR_RESPONSE = new BaseResponse() {
        @Override
        public String getMessage() {
            return "网络异常，请稍后再试";
        }

        @Override
        public int getCode() {
            return -100999;
        }
    };

    /**
     * 失败响应结果
     */
    BaseResponse DEFAULT_FAILURE_RESPONSE = new BaseResponse() {
        @Override
        public String getMessage() {
            return "操作失败";
        }

        @Override
        public int getCode() {
            return -100000;
        }
    };


    /**
     * 获取响应消息可以包含{0}
     *
     * @return 响应消息
     */
    public String getMessage();

    /**
     * 获取响应码
     *
     * @return 响应码
     */
    public int getCode();


}
