package com.ab.us.framework.core.exception.support;


import com.ab.us.framework.core.exception.BaseException;

public interface ExceptionTranslator {

	public BaseException translateException(Throwable error);
}
