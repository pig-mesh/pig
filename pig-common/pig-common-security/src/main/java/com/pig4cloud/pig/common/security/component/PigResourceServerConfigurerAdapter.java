/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.security.component;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author lengleng
 * @date 2019/03/08
 *
 * <p>
 * 1. 支持remoteTokenServices 负载均衡 2. 支持 获取用户全部信息 3. 接口对外暴露，不校验 Authentication Header 头
 */
@Slf4j
@EnableWebSecurity
public class PigResourceServerConfigurerAdapter {

	@Autowired
	protected ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

	@Autowired
	private PermitAllUrlProperties permitAllUrl;

	@Autowired
	private PigBearerTokenExtractor pigBearerTokenExtractor;

	@Autowired
	private OpaqueTokenIntrospector customOpaqueTokenIntrospector;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.authorizeRequests(authorizeRequests -> authorizeRequests
				.antMatchers(ArrayUtil.toArray(permitAllUrl.getUrls(), String.class)).permitAll().anyRequest()
				.authenticated())
				.oauth2ResourceServer(
						oauth2 -> oauth2.opaqueToken(token -> token.introspector(customOpaqueTokenIntrospector))
								.authenticationEntryPoint(resourceAuthExceptionEntryPoint)
								.bearerTokenResolver(pigBearerTokenExtractor))
				.headers().frameOptions().disable().and().csrf().disable();

		return http.build();
	}

}
