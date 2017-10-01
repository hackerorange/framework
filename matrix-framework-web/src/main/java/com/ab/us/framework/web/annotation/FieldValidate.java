//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.ab.us.framework.web.annotation;

import java.lang.annotation.*;

import com.ab.us.framework.web.constant.ValidateFeature;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface FieldValidate {

	/**
	 * 校验类型
	 * 
	 * @return 要校验的对象的类型
	 */
	ValidateFeature[] value() default { };

}
