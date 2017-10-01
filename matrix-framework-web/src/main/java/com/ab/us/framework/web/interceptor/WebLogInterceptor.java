package com.ab.us.framework.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

import static com.ab.us.framework.core.utils.StringUtil.DOUBLE_SPLIT_LINE_STRING;

/**
 * Created by ZhongChongtao on 2017/2/27.
 */
public class WebLogInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        logger.info(DOUBLE_SPLIT_LINE_STRING);
        startTime.set(System.currentTimeMillis());
        logger.info(String.format("[ %-20s ] : %s", "URL", request.getRequestURL().toString()));
        logger.info(String.format("[ %-20s ] : %s", "HTTP_METHOD", request.getMethod()));
        logger.info(String.format("[ %-20s ] : %s", "IP", request.getRemoteAddr()));
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            logger.info(String.format("[ %-20s ] : %s", "CLASS_METHOD", handlerMethod.getBeanType().getName() + "." + handlerMethod.getMethod().getName()));
        }
        //输出请求头参数
        Enumeration<String> params = request.getHeaderNames();
        if (params.hasMoreElements()) {
            logger.info(String.format("[ %-20s ] : ", "REQUEST_HEADERS"));
        }
        while (params.hasMoreElements()) {
            String paraName = params.nextElement();
            Enumeration<String> headers = request.getHeaders(paraName);
            while (headers.hasMoreElements()) {
                logger.info(String.format("  %-20s   : %s", paraName, headers.nextElement()));
            }
        }
        // 输出请求参数
        params = request.getParameterNames();
        if (params.hasMoreElements()) {
            logger.info(String.format("[ %-20s ] : ", "REQUEST_PARAMS"));
        }
        // 遍历请求参数
        while (params.hasMoreElements()) {
            String paraName = params.nextElement();
            for (String value : request.getParameterValues(paraName)) {
                logger.info(String.format("  %-20s   : %s", paraName, value));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info(String.format("[ %-20S ] : %s", "Time-consuming", (System.currentTimeMillis() - startTime.get())));
        logger.info(DOUBLE_SPLIT_LINE_STRING);
    }
}
