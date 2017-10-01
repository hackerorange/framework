package com.ab.us.framework.service.galaxylifeapp.utils;

import com.alibaba.fastjson.JSONObject;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: RestTemplateUtils.java
 * @Description: RestTemplate调用工具类
 * @author: fengsn
 */
public class RestTemplateUtils {

	/**
	 * @ClassName: DefaultResponseErrorHandler
	 * @author: fengsn
	 */
	private static class DefaultResponseErrorHandler implements ResponseErrorHandler {

		@Override
		public boolean hasError(ClientHttpResponse response) throws IOException {
			return response.getStatusCode().value() != HttpServletResponse.SC_OK;
		}

		@Override
		public void handleError(ClientHttpResponse response) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getBody()));
			StringBuilder sb = new StringBuilder();
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			try {
				throw new Exception(sb.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param url
	 * @param params
	 * @author: fengsn
	 * @Description: get方法调用
	 */
	public static String get(String url, JSONObject params) {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		String response = restTemplate.getForObject(expandURL(url, params.keySet()), String.class, params);
		return response;
	}

	/**
	 * @param url
	 * @param params
	 * @param mediaType
	 * @author: fengsn
	 * @Description: 将参数都拼接在url之后
	 */
	public static String post(String url, JSONObject params, MediaType mediaType) {
		RestTemplate restTemplate = new RestTemplate();
		// 拿到header信息
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(mediaType);
		HttpEntity<JSONObject> requestEntity = (mediaType == MediaType.APPLICATION_JSON
				|| mediaType == MediaType.APPLICATION_JSON_UTF8) ? new HttpEntity<JSONObject>(params, requestHeaders)
						: new HttpEntity<JSONObject>(null, requestHeaders);
		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		String result = (mediaType == MediaType.APPLICATION_JSON || mediaType == MediaType.APPLICATION_JSON_UTF8)
				? restTemplate.postForObject(url, requestEntity, String.class)
				: restTemplate.postForObject(expandURL(url, params.keySet()), requestEntity, String.class, params);
		return result;
	}
	
	/**
	 * @param url
	 * @param params
	 * @param mediaType
	 * @param clz
	 * @author: fengsn
	 * @Description: 发送json或者form格式数据
	 */
	public static <T> T post(String url, JSONObject params, MediaType mediaType, Class<T> clz) {
		RestTemplate restTemplate = new RestTemplate();
		// 这是为 MediaType.APPLICATION_FORM_URLENCODED 格式HttpEntity 数据 添加转换器
		// 还有就是，如果是APPLICATION_FORM_URLENCODED方式发送post请求，
		// 也可以直接HttpHeaders requestHeaders = new
		// HttpHeaders(createMultiValueMap(params)，true)，就不用增加转换器了
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
		// 设置header信息
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(mediaType);

		HttpEntity<?> requestEntity = (mediaType == MediaType.APPLICATION_JSON
				|| mediaType == MediaType.APPLICATION_JSON_UTF8)
						? new HttpEntity<JSONObject>(params, requestHeaders)
						: (mediaType == MediaType.APPLICATION_FORM_URLENCODED
								? new HttpEntity<MultiValueMap>(createMultiValueMap(params), requestHeaders)
								: new HttpEntity<>(null, requestHeaders));

		restTemplate.setErrorHandler(new DefaultResponseErrorHandler());
		T result = (mediaType == MediaType.APPLICATION_JSON || mediaType == MediaType.APPLICATION_JSON_UTF8)
				? restTemplate.postForObject(url, requestEntity, clz)
				: restTemplate.postForObject(
						mediaType == MediaType.APPLICATION_FORM_URLENCODED ? url : expandURL(url, params.keySet()),
						requestEntity, clz, params);

		return result;
	}

	private static MultiValueMap<String, Object> createMultiValueMap(JSONObject params) {
		MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		for (String key : params.keySet()) {
			if (params.get(key) instanceof List) {
				for (Iterator<String> it = ((List<String>) params.get(key)).iterator(); it.hasNext();) {
					String value = it.next();
					map.add(key, value);
				}
			} else {
				if(params.get(key) instanceof FileSystemResource){
					map.add(key, params.get(key));
				}else{
					map.add(key, params.get(key)+"");
				}
			}
		}
		return map;
	}
	
	/**
	 * 上传资源使用
	 * */
	public static String upload(String url, JSONObject params, MediaType mediaType) {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);

		MultiValueMap<String, Object> map = createMultiValueMap(params);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(map,
				headers);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST,
				requestEntity, String.class);

		// 调用接口返回数据
		return responseEntity.getBody().toString();
	}
	
	/**
	 * @param url
	 * @param keys
	 * @author: fengsn
	 * @Description: 拼接url
	 */
	private static String expandURL(String url, Set<?> keys) {
		final Pattern QUERY_PARAM_PATTERN = Pattern.compile("([^&=]+)(=?)([^&]+)?");
		Matcher mc = QUERY_PARAM_PATTERN.matcher(url);
		StringBuilder sb = new StringBuilder(url);
		if (mc.find()) {
			sb.append("&");
		} else {
			sb.append("?");
		}

		for (Object key : keys) {
			sb.append(key).append("=").append("{").append(key).append("}").append("&");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

}
