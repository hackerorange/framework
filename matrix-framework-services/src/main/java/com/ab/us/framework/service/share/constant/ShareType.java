package com.ab.us.framework.service.share.constant;

/**
 * 分享类型枚举
 */
public enum ShareType {

	/**
	 * 1）保险产品分享 2）关于的掌上安邦分享 3）推荐掌上安邦分享  4）消息分享 5）积分兑换商品分享 6）优惠券分享 7）资讯分享 8）专题分享
	 * 9）活动分享
	 */
	INSURE("INSURE","保险产品分享 ","aasdsadApi"), 
	ABOUTUS("ABOUTUS","关于的掌上安邦分享","aasdsadApi"), 
	RECOMMEND("RECOMMEND","推荐掌上安邦分享","aasdsadApi"), 
	MESSAGE("MESSAGE","消息分享","aasdsadApi"), 
	EXCHANGEPOINT("EXCHANGEPOINT","积分兑换商品分享","aasdsadApi"), 
	COUPON("COUPON","优惠券分享","aasdsadApi"),
	INFORMATION("INFORMATION","资讯分享","aasdsadApi"),
	TOPIC("TOPIC","专题分享","aasdsadApi"), 
	ACTIVITY("ACTIVITY","活动分享","aasdsadApi");

	private String shareTypeCode;
	
	private String shareTypeDesc;
	
	private String shareTypeExecute;

	private ShareType(String shareTypeCode,String shareTypeDesc,String shareTypeExecute) {
		this.shareTypeCode = shareTypeCode;
		this.shareTypeDesc = shareTypeDesc;
		this.shareTypeExecute = shareTypeExecute;
	}

	public String getShareTypeCode() {
		return shareTypeCode;
	}

	public void setShareTypeCode(String shareTypeCode) {
		this.shareTypeCode = shareTypeCode;
	}

	public String getShareTypeDesc() {
		return shareTypeDesc;
	}

	public void setShareTypeDesc(String shareTypeDesc) {
		this.shareTypeDesc = shareTypeDesc;
	}

	public String getShareTypeExecute() {
		return shareTypeExecute;
	}

	public void setShareTypeExecute(String shareTypeExecute) {
		this.shareTypeExecute = shareTypeExecute;
	}
	
}
