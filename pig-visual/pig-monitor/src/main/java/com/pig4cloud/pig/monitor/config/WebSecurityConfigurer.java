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

package com.pig4cloud.pig.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * WebSecurityConfigurer
 *
 * @author lishangbu
 * @date 2019/2/1
 */
@EnableWebSecurity
public class WebSecurityConfigurer {

	private final String adminContextPath;

	public WebSecurityConfigurer(AdminServerProperties adminServerProperties) {
		this.adminContextPath = adminServerProperties.getContextPath();
	}

	/**
	 * spring security 默认的安全策略
	 * @param http security注入点
	 * @return SecurityFilterChain
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setTargetUrlParameter("redirectTo");
		successHandler.setDefaultTargetUrl(adminContextPath + "/");
		http.headers()
			.frameOptions()
			.disable()
			.and()
			.authorizeRequests()
			.antMatchers(adminContextPath + "/assets/**", adminContextPath + "/login",
					adminContextPath + "/instances/**", adminContextPath + "/actuator/**")
			.permitAll()
			.anyRequest()
			.authenticated()
			.and()
			.formLogin()
			.loginPage(adminContextPath + "/login")
			.successHandler(successHandler)
			.and()
			.logout()
			.logoutUrl(adminContextPath + "/logout")
			.and()
			.httpBasic()
			.and()
			.csrf()
			.disable();
		return http.build();
	}

}
