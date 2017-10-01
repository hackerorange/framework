package com.ab.us.framework.service.share.support;

import com.ab.us.framework.service.share.domain.ShareContext;

/**
 * 抽象类处理查询获得相关产品的分享信息
 * @author fengsn
 * @since 2017-09-12
 * */
public abstract class AbstractShareContextExecutor {

	/**
	 * 查询分享信息接口
	 * */
	public ShareContext getShareContext(){
		//step 1  数据库查询相关匹配数据
		
		//step 2 根据id查询分享
		
		//step 3 替换url中的变量数据
		
		//step 4 拼接短链地址
		
		return null;
	}
	
	/**
	 * 查询分享产品的对象model
	 * */
	protected abstract Object getShareProductModel();
	
	public Object queryShareConfig(){
		return null;
	}
}
