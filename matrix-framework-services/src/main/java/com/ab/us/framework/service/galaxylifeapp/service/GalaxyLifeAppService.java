package com.ab.us.framework.service.galaxylifeapp.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 系统接口转发接口
 * */
public interface GalaxyLifeAppService {
	
	/**
	 * 调用get接口，返回json String格式数据
	 * */
	public String get(String methodCode, JSONObject params);
	
	/**
	 * 调用post接口，返回json String格式数据
	 * */
	public String post(String methodCode, JSONObject params);
	
	/**
	 * 上传文件使用接口，返回json String格式数据
	 * */
	public String upload(String methodCode, JSONObject params);
	
	/**
	 * 拼装获得跳转页面url
	 * */
	public String expandUrl(String methodCode, JSONObject params);
	
}
