package com.ab.us.framework.service.galaxylifeapp.service.impl;

import com.ab.us.framework.service.galaxylifeapp.domain.GalaxyLifeAppModel;
import com.ab.us.framework.service.galaxylifeapp.service.GalaxyLifeAppService;
import com.ab.us.framework.service.galaxylifeapp.support.GalaxyLifeAppServiceSupport;
import com.ab.us.framework.service.galaxylifeapp.utils.RestTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

@Service
public class GalaxyLifeAppServiceImpl implements GalaxyLifeAppService {

	@Override
	public String get(String methodCode, JSONObject params) {
		// 拼接地址和方法名
		GalaxyLifeAppModel galaxyLifeAppModel = galaxyLifeAppServiceSupport
				.getGalaxyLifeAppModelByMethodCode(methodCode);
		// 接口参数拼接
		params = galaxyLifeAppServiceSupport.initGalaxyLifeAppParam(galaxyLifeAppModel, params);
		// 调用接口返回数据
		return RestTemplateUtils.get(galaxyLifeAppModel.getUrl(), params);
	}

	@Override
	public String post(String methodCode, JSONObject params) {
		// 拼接地址和方法名
		GalaxyLifeAppModel galaxyLifeAppModel = galaxyLifeAppServiceSupport
				.getGalaxyLifeAppModelByMethodCode(methodCode);
		// 接口参数拼接
		params = galaxyLifeAppServiceSupport.initGalaxyLifeAppParam(galaxyLifeAppModel, params);
		// 调用接口返回数据
		return RestTemplateUtils.post(galaxyLifeAppModel.getUrl(), params, MediaType.APPLICATION_FORM_URLENCODED,
				String.class);
	}

	@Override
	public String upload(String methodCode, JSONObject params) {
		// 拼接地址和方法名
		GalaxyLifeAppModel galaxyLifeAppModel = galaxyLifeAppServiceSupport
				.getGalaxyLifeAppModelByMethodCode(methodCode);
		// 接口参数拼接
		params = galaxyLifeAppServiceSupport.initGalaxyLifeAppParam(galaxyLifeAppModel, params);

		return RestTemplateUtils.upload(galaxyLifeAppModel.getUrl(), params, MediaType.MULTIPART_FORM_DATA);
	}

	@Override
	public String expandUrl(String methodCode, JSONObject params) {
		// TODO Auto-generated method stub
		GalaxyLifeAppModel galaxyLifeAppModel = galaxyLifeAppServiceSupport
				.getGalaxyLifeAppModelByMethodCode(methodCode);

		params = galaxyLifeAppServiceSupport.initGalaxyLifeAppParam(galaxyLifeAppModel, params);

		return galaxyLifeAppServiceSupport.resolveParameterValues(galaxyLifeAppModel.getUrl(), params);
	}

	@Autowired
	private GalaxyLifeAppServiceSupport galaxyLifeAppServiceSupport;

}
