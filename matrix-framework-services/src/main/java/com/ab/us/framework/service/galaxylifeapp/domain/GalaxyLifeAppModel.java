package com.ab.us.framework.service.galaxylifeapp.domain;

/**
 * 调用GalaxyLifeApp系统model
 */
public class GalaxyLifeAppModel {

	/**
	 * 接口调用url
	 */
	private String url;

	/**
	 * 接口方法名
	 */
	private String method;

	public GalaxyLifeAppModel() {

	}

	public GalaxyLifeAppModel(String url, String method) {
		this.url = url;
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

}
