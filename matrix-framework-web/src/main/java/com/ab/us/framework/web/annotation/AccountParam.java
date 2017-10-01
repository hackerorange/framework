package com.ab.us.framework.web.annotation;

import java.lang.annotation.*;

/**
 * Created by ZhongChongtao on 2017/3/14.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AccountParam {

    /**
     * 是否是必传字段
     *
     * @return 必须登录为true，否则为false
     */
    boolean required() default true;
}
