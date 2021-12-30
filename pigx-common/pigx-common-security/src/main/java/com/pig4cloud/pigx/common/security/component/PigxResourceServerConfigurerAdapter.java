/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.common.security.component;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.client.RestTemplate;

/**
 * @author lengleng
 * @date 2018/6/22
 * <p>
 * 1. 支持remoteTokenServices 负载均衡 2. 支持 获取用户全部信息
 */
@Slf4j
public class PigxResourceServerConfigurerAdapter extends ResourceServerConfigurerAdapter {

	@Autowired
	protected AuthenticationEntryPoint resourceAuthExceptionEntryPoint;

	@Autowired
	protected RemoteTokenServices remoteTokenServices;

	@Autowired
	private PermitAllUrlResolver permitAllUrlResolver;

	@Autowired
	private TokenExtractor tokenExtractor;

	@Autowired
	private RestTemplate lbRestTemplate;

	/**
	 * 默认的配置，对外暴露
	 * @param httpSecurity
	 */
	@Override
	@SneakyThrows
	public void configure(HttpSecurity httpSecurity) {
		// 允许使用iframe 嵌套，避免swagger-ui 不被加载的问题
		httpSecurity.headers().frameOptions().disable();
		ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = httpSecurity
				.authorizeRequests();
		// 配置对外暴露接口
		permitAllUrlResolver.registry(registry);
		registry.anyRequest().authenticated().and().csrf().disable();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		remoteTokenServices.setRestTemplate(lbRestTemplate);
		resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint).tokenExtractor(tokenExtractor)
				.tokenServices(remoteTokenServices);
	}

}
