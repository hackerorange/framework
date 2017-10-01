package com.ab.us.framework.web.annotation;

import com.ab.us.framework.web.aop.RequestValidate;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author Zhongchongtao
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Import(RequestValidate.class)
public @interface RequestModel {
}
