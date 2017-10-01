package com.ab.us.framework.service.shorturl.service.impl;

import java.io.IOException;
import java.net.URLEncoder;

import com.ab.us.framework.service.dictionray.SystemDictionaryCacheService;
import com.ab.us.framework.service.galaxylifeapp.utils.RestTemplateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ab.us.framework.core.utils.GalaxyAppLifeSnValidate;
import com.ab.us.framework.service.shorturl.service.ShortUrlService;
import com.alibaba.fastjson.JSONObject;

public class ShortUrlServiceImpl implements ShortUrlService {

	@Override
	public String getShortUrl(String url) throws IOException {

		String shortUrlWebSite = systemDictionaryCacheService.getDictionaryNameByTypeAndCode("webSite",
				"shortUrlServer");

		if (!shortUrlWebSite.endsWith("/")) {

			shortUrlWebSite += "/";
		}

		StringBuffer sb = new StringBuffer();

		sb.append(shortUrlWebSite)
				.append("shortUrl/api/short?mode=json&deviceType=ios&deviceAppId=1234567890"
						+ "&platformId=Galaxy_LifeApp&marketChannel=C&appChannel=1&appVersion=2.11.0&url=")
				.append(URLEncoder.encode(url));

		GalaxyAppLifeSnValidate galaxyAppLifeSnValidate = new GalaxyAppLifeSnValidate("getShortUrl");

		sb.append("&sn=" + galaxyAppLifeSnValidate.getSn() + "&hashcode=" + galaxyAppLifeSnValidate.getHashcode());

		String result = RestTemplateUtils.get(shortUrlWebSite, null);

		JSONObject jsonObject = JSONObject.parseObject(result);

		String shorturl = "";

		if ("200".equals(jsonObject.get("code") + "")) {
			shorturl = shortUrlWebSite + "shortUrl/" + jsonObject.get("body");
		}
		
		return shorturl;
	}

	@Autowired
	private SystemDictionaryCacheService systemDictionaryCacheService;

}
