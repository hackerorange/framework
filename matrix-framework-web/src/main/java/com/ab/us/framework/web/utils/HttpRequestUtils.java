package com.ab.us.framework.web.utils;

import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.core.utils.TypeChecker;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Created by ZhongChongtao on 2017/2/25.
 */
@SuppressWarnings("Duplicates")
public class HttpRequestUtils {
    /**
     * 获取请求参数，如果没有找到，返回def值
     *
     * @param name 请求参数名称
     * @param def  找不到参数要返回的默认值
     * @return 参数value
     */
    public static String getRequestParamByDef(String name, String def) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (TypeChecker.isNull(requestAttributes)) {
            return def;
        }
        String parameter = requestAttributes.getRequest().getParameter(name);
        if (StringUtil.isEmpty(parameter)) {
            return def;
        }
        return parameter;
    }

    /**
     * 从Http请求中获取真实IP，如果请求头中没有x-real-ip，则从request中获取
     *
     * @return 请求的真实IP
     */
    public static String getRealIp() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String header = requestAttributes.getRequest().getHeader("x-real-ip");
        return StringUtil.isEmpty(header) ? requestAttributes.getRequest().getRemoteAddr() : header;
    }

    /**
     * 获取请求参数，如果没有找到，返回null值
     *
     * @param paramKey 请求参数名称
     * @return 请求参数value值
     */
    public static String getRequestParam(String paramKey) {
        return getRequestParamByDef(paramKey, null);
    }

    /**
     * 获取requestAttribute,如果非web环境，返回null
     *
     * @param attributeKey 从requestAttribute中获取对象
     * @return 查找到的对象
     */
    public static Object getRequestAttribute(String attributeKey) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (TypeChecker.isNull(requestAttributes)) {
            return null;
        }
        return requestAttributes.getAttribute(attributeKey, RequestAttributes.SCOPE_REQUEST);
    }

    /**
     * 向request中添加attribute,作用域为request
     *
     * @param name   attribute名称
     * @param object 值
     */
    public static void addRequestAttribute(String name, Object object) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (TypeChecker.isNull(requestAttributes)) {
            return;
        }
        requestAttributes.setAttribute(name, object, RequestAttributes.SCOPE_REQUEST);
    }
}
