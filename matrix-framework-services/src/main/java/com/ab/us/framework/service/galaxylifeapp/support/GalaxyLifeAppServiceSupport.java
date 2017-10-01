package com.ab.us.framework.service.galaxylifeapp.support;

import java.io.File;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ab.us.framework.core.utils.GalaxyAppLifeSnValidate;
import com.ab.us.framework.core.utils.StringUtil;
import com.ab.us.framework.core.utils.TypeChecker;
import com.ab.us.framework.service.dictionray.SystemDictionaryCacheService;
import com.ab.us.framework.service.entity.SystemDictionary;
import com.ab.us.framework.service.galaxylifeapp.domain.GalaxyLifeAppModel;
import com.alibaba.fastjson.JSONObject;

@Component
public class GalaxyLifeAppServiceSupport {

	private final static String prefix = "<";

	private final static String subfix = ">";

	private final static String PLAT_FORM_ID = "Galaxy_LifeApp";

	/**
	 * 根据methodCode获得 调用接口url地址 & method
	 */
	public GalaxyLifeAppModel getGalaxyLifeAppModelByMethodCode(String methodCode) {

		SystemDictionary systemDictionary = systemDictionaryCacheService
				.getDictionaryByTypeAndCode("SYSTEM_SERVICE_ACTION_URL", methodCode);

		if (TypeChecker.isNull(systemDictionary)) {
			return null;
		}

		String url = this.resolveUrl(systemDictionary.getRemark1(), prefix, subfix);

		String method = systemDictionary.getRemark2();

		return new GalaxyLifeAppModel(url, method);
	}

	/**
	 * 翻译相关url变量
	 */
	public String resolveUrl(String url, String prefix, String subfix) {

		List<SystemDictionary> systemDictionarys = systemDictionaryCacheService
				.getDictionaryListByType("SYSTEM_SERVICE_WEBSITE");

		for (SystemDictionary systemDictionary : systemDictionarys) {

			String webSiteName = systemDictionary.getDictCode();

			String webSiteUrl = systemDictionary.getDictName();

			if (TypeChecker.isEmpty(webSiteUrl)) {
				continue;
			}

			url = StringUtil.replaceAll(url, prefix + webSiteName + subfix, webSiteUrl);
		}

		return url;
	}

	/**
	 * 替换url中的"{","}"直接的变量
	 */
	public String resolveParameterValues(String url, JSONObject params) {

		Set<String> keyset = params.keySet();

		for (String key : keyset) {

			url = StringUtil.replaceAll(url, "{" + key + "}", params.get(key).toString());
		}

		return url;
	}

	/**
	 * 初始化调用请求参数 mode method sn hashcode platformId
	 */
	public JSONObject initGalaxyLifeAppParam(GalaxyLifeAppModel galaxyLifeAppModel, JSONObject params) {

		GalaxyAppLifeSnValidate galaxyAppLifeSnValidate = new GalaxyAppLifeSnValidate(galaxyLifeAppModel.getMethod());
		params.put("sn", galaxyAppLifeSnValidate.getSn());
		params.put("hashcode", galaxyAppLifeSnValidate.getHashcode());
		params.put("platformId", this.PLAT_FORM_ID);
		params.put("method", galaxyAppLifeSnValidate.getMethod());
		params.put("mode", "json");
		
		//特殊处理file类型参数
		Set<String> keyset = params.keySet();
		for(String key : keyset){
			if(params.get(key).getClass().isInstance(File.class)){
				FileSystemResource resource = new FileSystemResource((File)params.get(key)); 
				params.put(key, resource);
			}
		}
		
		return params;
	}

	public static void main(String[] args) {

		/*
		JSONObject paraMap = new JSONObject();
		paraMap.put("deviceAppId", "1");
		paraMap.put("deviceType", "ios");
		paraMap.put("appVersion", "2.0");
		GalaxyAppLifeSnValidate galaxyAppLifeSnValidate = new GalaxyAppLifeSnValidate("QueryNetworkAreaAndRiskTags");
		paraMap.put("sn", galaxyAppLifeSnValidate.getSn());
		paraMap.put("hashcode", galaxyAppLifeSnValidate.getHashcode());
		paraMap.put("platformId", "Galaxy_LifeApp");
		paraMap.put("method", "QueryNetworkAreaAndRiskTags");
		paraMap.put("mode", "json");

		String url = "http://10.10.140.133:5161/GalaxyLifeAppServices/webservice/sn/protable/netwrokWebServiceAction.do?";
		
		
		Set<String> keyset = paraMap.keySet();
		
		StringBuffer sb = new StringBuffer("");
		
		sb.append(url);

		for (String key : keyset) {

			sb.append(key+"="+paraMap.get(key)+"&");
		}
		
		String jsonObject = restTemplate.postForObject(sb.toString(), null, String.class);
		*/
		
		//String result = RestTemplateUtils.get(url, paraMap);

		JSONObject paraMap = new JSONObject();
		paraMap.put("deviceAppId", "1");
		paraMap.put("deviceType", "ios");
		paraMap.put("appVersion", "2.0");
		GalaxyAppLifeSnValidate galaxyAppLifeSnValidate = new GalaxyAppLifeSnValidate("UploadAccidentVehicleResource");
		paraMap.put("sn", galaxyAppLifeSnValidate.getSn());
		paraMap.put("hashcode", galaxyAppLifeSnValidate.getHashcode());
		paraMap.put("platformId", "Galaxy_LifeApp");
		paraMap.put("method", "UploadAccidentVehicleResource");
		paraMap.put("mode", "json");
		
		paraMap.put("fileSize", "879394");
		paraMap.put("streamStatus", "E");
		paraMap.put("streamSeek", "0");
		String path = "D:/640.png";
		File file = new File(path);
		paraMap.put("streamPath", path);
		FileSystemResource resource = new FileSystemResource(file); 
		paraMap.put("stream", resource);
		paraMap.put("streamFileName", "640.png");
		paraMap.put("reportId", "KK101404231725326851");
		
		paraMap.put("userImei", "123");
		paraMap.put("damageType", "00");
		paraMap.put("photoType", "13");
		paraMap.put("bigType", "0");
		paraMap.put("smallType", "0");
		paraMap.put("longitude", "0");
		paraMap.put("latitude", "0");
		paraMap.put("fileType", "0");
		paraMap.put("takePhotoTm", "");
		
		String url = "http://10.10.140.133:5141/GalaxyLifeAppSelfSurvey/webservice/sn/protable/appLifeSelfSurveyWebServiceAction.do?";
		
		//String result =  RestTemplateUtils.post(url, paraMap, MediaType.MULTIPART_FORM_DATA, String.class);
		
		
		
		RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data;charset=UTF-8");
        headers.setContentType(type);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String, Object>();
        
        for (String key : paraMap.keySet()) {
			if (paraMap.get(key) instanceof List) {
				for (Iterator<String> it = ((List<String>) paraMap.get(key)).iterator(); it.hasNext();) {
					String value = it.next();
					map.add(key, value);
				}
			} else {
				map.add(key, paraMap.get(key));
			}
		}

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		
		System.out.println(responseEntity.getBody().toString());
		
	}

	@Autowired
	private SystemDictionaryCacheService systemDictionaryCacheService;

}
