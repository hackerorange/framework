package com.ab.us.framework.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: FieldAnnotation
 * @Description: TODO(字段域注解)
 * @author xusisheng
 * @date 2017-01-20 16:51:31
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldAnnotation {

	/**
	 * 字段域名
	 */
	String fieldName();

	/**
	 * 字段类型
	 */
	FieldType fieldType();

	/**
	 * 是否主键
	 */
	boolean pk();

    /**
     * 初始数值，作为Long类型主键时初始值
     *
     * @return 数值
     */
    public long start() default 100000000L;

	/**
	 * 是否查询字段
	 */
	boolean isQueryField() default false;

}
