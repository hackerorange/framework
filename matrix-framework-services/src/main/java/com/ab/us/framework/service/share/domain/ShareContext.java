package com.ab.us.framework.service.share.domain;

/**
 * 系统分享信息
 * */
public class ShareContext {
	
	/** 分享图标 **/
	public String shareIcon;
	
	/** 分享标题 **/
	public String shareTitle;
	
	/** 分享描述 **/
	public String shareDesc;
	
	/** 分享文字 **/
	public String shareContent;
	
	/** 分享url **/
	public String shareUrl;
	
	/** 分享图片 **/
	public String shareImage;
	
	public ShareContext(){
		
	}

	public String getShareIcon() {
		return shareIcon;
	}

	public void setShareIcon(String shareIcon) {
		this.shareIcon = shareIcon;
	}

	public String getShareTitle() {
		return shareTitle;
	}

	public void setShareTitle(String shareTitle) {
		this.shareTitle = shareTitle;
	}

	public String getShareDesc() {
		return shareDesc;
	}

	public void setShareDesc(String shareDesc) {
		this.shareDesc = shareDesc;
	}

	public String getShareContent() {
		return shareContent;
	}

	public void setShareContent(String shareContent) {
		this.shareContent = shareContent;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	
}
