package com.ab.us.framework.web.aop;

import com.ab.us.framework.core.utils.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

/**
 * Created by ZhongChongtao on 2017/2/11.
 */

@Aspect
public class WebServiceResponseLogger {
    private Logger logger = Logger.getLogger(this.getClass());

    @AfterReturning(value = "within(com.ab.us.framework.web.controller.BaseController+)", returning = "response")
    public void doAfter(Object response) {
        if (logger.isDebugEnabled()) {
            logger.info(StringUtil.SINGLE_SPLIT_LINE_STRING);
            logger.info(String.format("[ %-20S ] : %s", "response", JSONObject.toJSONString(response)));
        }
    }
}
