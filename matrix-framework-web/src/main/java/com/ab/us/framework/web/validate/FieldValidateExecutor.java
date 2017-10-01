package com.ab.us.framework.web.validate;

public interface FieldValidateExecutor {

	/**
	 * 校验对象
	 * 
	 * @param object
	 *            要校验的对象
	 * @return 是否校验通过
	 */
	boolean validate(Object object);

}
