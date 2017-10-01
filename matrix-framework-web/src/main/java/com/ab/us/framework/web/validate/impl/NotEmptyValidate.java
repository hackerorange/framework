package com.ab.us.framework.web.validate.impl;

import java.util.Collection;

import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.web.validate.FieldValidateExecutor;

public class NotEmptyValidate implements FieldValidateExecutor {

	@Override
	public boolean validate(Object object) {
		// 如果类型是集合的话，判断集合是否为空
		if (object instanceof Collection) {
			Collection collection = (Collection) object;
			if (TypeChecker.isEmpty( collection )) {
				return false;
			}
		}
		// 如果类型是字符串的话，判断是否是空字符串
		if (object instanceof String) {
			String string = (String) object;
			if (StringUtil.isEmpty( string )) {
				return false;
			}
		}
		// 如果对象为null,则返回false
		return object != null;
	}
}
