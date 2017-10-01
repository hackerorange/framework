package com.ab.us.framework.web.response;

/**
 * 生成response Created by ZhongChongtao on 2017/3/28.
 */
public interface ResponseGenerate {
    /**
     * 生成标准响应信息
     *
     * @return 响应信息
     */
    public BodyResponse generateResponse(Object object,String... params);
}
