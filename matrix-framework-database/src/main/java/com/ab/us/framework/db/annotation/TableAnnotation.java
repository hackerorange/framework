package com.ab.us.framework.db.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ab.us.framework.db.entity.DataSchema;

/**
 * @ClassName: TableAnnotation
 * @Description: TODO(表名注解)
 * @author xusisheng
 * @date 2017-01-20 15:50:49
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TableAnnotation {

	String tableName();

	public String postfix() default "";

	public DataSchema schema() default DataSchema.UNIVERSESUN_COMMON;
}
