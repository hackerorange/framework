package com.ab.us.framework.web.constant;

import com.ab.us.framework.web.validate.FieldValidateExecutor;
import com.ab.us.framework.web.validate.impl.NotEmptyValidate;

public enum ValidateFeature implements FieldValidateExecutor {
	NOT_EMPTY(new NotEmptyValidate()),

	;

	private FieldValidateExecutor validator;

	ValidateFeature(FieldValidateExecutor validator) {
		this.validator = validator;
	}

	@Override
	public boolean validate(Object object) {
		return validator.validate( object );
	}
}
