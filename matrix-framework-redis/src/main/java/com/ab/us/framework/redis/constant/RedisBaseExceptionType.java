package com.ab.us.framework.redis.constant;

import com.ab.us.framework.core.exception.BaseException;
import com.ab.us.framework.core.exception.ExceptionGenerate;
import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.core.utils.TypeChecker;

/**
 * @author Zhongchongtao
 */
public enum RedisBaseExceptionType implements ExceptionGenerate {
	REDIS_DISTRIBUTE_LOCK_TIME_OUT(-100100, "操作超时[{0}]"), //
	TABLE_NOT_FOUND(-100101, "数据库中没有找到指定的表[{0}]"), //
	TABLE_PK_FOUND(-100102, "实体类中没有找到主键"), //
	REDIS_MISSING_CONFIG(-100110, "缺少Redis配置"),//
	;

	private int		code;
	private String	message;

	RedisBaseExceptionType(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public BaseException generateBaseException(String... params) {
		String tmpMessage = TypeChecker.isEmpty( params ) ? message : StringUtil.replaceByIndex( message, params );
		return new BaseException( code, tmpMessage );
	}
}
