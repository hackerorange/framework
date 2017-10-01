package com.ab.us.framework.service.share.service;

import com.ab.us.framework.service.share.constant.ShareType;
import com.ab.us.framework.service.share.domain.ShareContext;

/**
 * 分享业务接口
 * */
public interface ShareService {

	/**
	 * 根据分享类型和分享产品id查询获得分享内容
	 * */
	public ShareContext getShareContextByTypeAndId(ShareType shareType , String... relationId);
}
