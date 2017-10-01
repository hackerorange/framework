package com.ab.us.framework.service.shorturl.service;

import java.io.IOException;

/**
 * 调用获得短链地址接口
 * */
public interface ShortUrlService {

	public String getShortUrl(String url) throws IOException;
	
}
