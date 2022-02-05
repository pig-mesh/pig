package com.pig4cloud.pigx.common.datasource.config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lengleng
 * @date 2020/12/11
 */
public class ClearTtlDsConfiguration implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new ClearTtlDsInterceptor()).addPathPatterns("/**");
	}

}
