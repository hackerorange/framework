package com.ab.us.framework.service.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * Created by ZhongChongtao on 2017/5/9.
 */
@ConfigurationProperties(prefix = "com.ab.us.push")
public class UsPushProperties {
    /**
     * 推送接口的URL地址
     *
     * @since 2.11.0
     */
    private Map<String, String> url;

    /**
     * 获取推送URL
     *
     * @return 推送URL
     * @since 2.11.0
     */
    public Map<String, String> getUrl() {
        return url;
    }

    public void setUrl(Map<String, String> url) {
        this.url = url;
    }
}
