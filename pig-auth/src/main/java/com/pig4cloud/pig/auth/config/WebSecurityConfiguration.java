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

package com.pig4cloud.pig.auth.config;

import com.pig4cloud.pig.auth.support.core.FormIdentityLoginConfigurer;
import com.pig4cloud.pig.auth.support.core.PigDaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * 服务安全相关配置
 *
 * @author lengleng
 * @date 2022/1/12
 */
@EnableWebSecurity(debug = true)
public class WebSecurityConfiguration {

	/**
	 * spring security 默认的安全策略
	 * @param http security注入点
	 * @return SecurityFilterChain
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests(authorizeRequests -> authorizeRequests.antMatchers("/token/*").permitAll()// 开放自定义的部分端点
				.anyRequest().authenticated()).headers().frameOptions().sameOrigin()// 避免iframe同源无法登录
				.and().apply(new FormIdentityLoginConfigurer()); // 表单登录个性化
		// 处理 UsernamePasswordAuthenticationToken
		http.authenticationProvider(new PigDaoAuthenticationProvider());
		return http.build();
	}

	/**
	 * 暴露静态资源
	 *
	 * https://github.com/spring-projects/spring-security/issues/10938
	 * @param http
	 * @return
	 * @throws Exception
	 */
	@Bean
	@Order(0)
	SecurityFilterChain resources(HttpSecurity http) throws Exception {
		http.requestMatchers((matchers) -> matchers.antMatchers("/actuator/**", "/css/**", "/error"))
				.authorizeHttpRequests((authorize) -> authorize.anyRequest().permitAll()).requestCache().disable()
				.securityContext().disable().sessionManagement().disable();
		return http.build();
	}

}
