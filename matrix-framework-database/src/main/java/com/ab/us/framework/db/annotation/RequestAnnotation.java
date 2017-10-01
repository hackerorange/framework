package com.ab.us.framework.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: RequestAnnotation
 * @Description: TODO(请求标签)
 * @author xusisheng
 * @date 2017-02-27 11:15:33
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RequestAnnotation {

    /**
     * 字段针对的方法,如果为All时对所有方法有效
     *
     * @return
     */
    public MethodType methodType() default MethodType.ALL;

    /**
     * 字段类型
     *
     * @return
     */
    public FieldType fieldType() default FieldType.STRING;

    /**
     * 空检查,默认为true不检查
     *
     * @return
     */
    public boolean blank() default true;

    /**
     * 最大长度检查,默认时不检查
     *
     * @return
     */
    public int maxLen() default 0;

    /**
     * 最小长度检查,默认为0时不检查
     *
     * @return
     */
    public int minLen() default 0;

    /**
     * 长度检查,0时不检查
     *
     * @return
     */
    public int len() default 0;

    /**
     * 正则匹配
     *
     * @return
     */
    public String reg() default "";

    /**
     * 是否需要进行特殊字符转义
     *   默认不做处理
     * @return
     */
    public boolean regValidate() default false;
    
    /**
     * html中，\ 转义为 \\
     *   默认不做处理
     * @return
     */
    public boolean xieGangValidate() default false;
    
    /**
     * 验证手机号码
     * @return
     */
    public boolean mobile() default false;

}
