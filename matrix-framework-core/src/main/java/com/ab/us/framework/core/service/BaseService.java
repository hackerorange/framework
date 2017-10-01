package com.ab.us.framework.core.service;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;

public abstract class BaseService {

	protected Logger logger;

	/**
	 * 如果数据源不是默认数据源，则需要具体服务实现类中实现此函数 调用DAO的setDataSchema(数据源)
	 * 如果需要logger的话，需要在此处指定logger
	 */
	@PostConstruct
	public abstract void Init();

}
