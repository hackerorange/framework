package com.ab.us.framework.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.ab.us.framework.web.aop.WebServiceResponseLogger;

/**
 * Created by ZhongChongtao on 2017/2/12.<br/>
 * web相关配置，包括API跨域（用于测试页面） AccountParam注解翻译 webLogger接口访问日志显示
 */
@Configuration
// @EnableAutoConfiguration
public class GlobalWebConfiguration extends WebMvcConfigurerAdapter {

	// @Override
	// public void addInterceptors(InterceptorRegistry registry) {
	// registry.addInterceptor( new WebLogInterceptor() ).addPathPatterns( "/**"
	// );
	// registry.addInterceptor( new WebLastInterceptor() ).addPathPatterns(
	// "/**" );
	// }

	//
	// @Override
	// public void addArgumentResolvers(List<HandlerMethodArgumentResolver>
	// argumentResolvers) {
	// super.addArgumentResolvers(argumentResolvers);
	// }
	//
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin( "*" );
		corsConfiguration.addAllowedHeader( "*" );
		corsConfiguration.addAllowedMethod( "*" );
		source.registerCorsConfiguration( "/**", corsConfiguration );
		return new CorsFilter( source );
	}

	@Bean
	public WebServiceResponseLogger webServiceResponseLogger() {
		return new WebServiceResponseLogger();
	}

}