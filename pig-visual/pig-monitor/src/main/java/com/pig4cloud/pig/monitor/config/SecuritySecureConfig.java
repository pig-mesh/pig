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
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import java.util.UUID;

/**
 * 安全配置类：用于配置Spring Security相关设置
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Configuration(proxyBeanMethods = false)
public class SecuritySecureConfig {

	private final AdminServerProperties adminServer;

	private final SecurityProperties security;

	/**
	 * 构造函数，初始化安全管理配置
	 * @param adminServer 管理服务器配置属性
	 * @param security 安全配置属性
	 */
	public SecuritySecureConfig(AdminServerProperties adminServer, SecurityProperties security) {
		this.adminServer = adminServer;
		this.security = security;
	}

	/**
	 * 配置Spring Security过滤器链
	 * @param http HttpSecurity对象，用于配置安全策略
	 * @return 配置好的SecurityFilterChain实例
	 * @throws Exception 配置过程中可能抛出的异常
	 */
	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setTargetUrlParameter("redirectTo");
		successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

		http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests //
			.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher(this.adminServer.path("/assets/**")))
			.permitAll()
			.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher(this.adminServer.path("/actuator/info")))
			.permitAll()
			.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher(adminServer.path("/actuator/health")))
			.permitAll()
			.requestMatchers(PathPatternRequestMatcher.withDefaults().matcher(this.adminServer.path("/login")))
			.permitAll()
			.dispatcherTypeMatchers(DispatcherType.ASYNC)
			.permitAll() // https://github.com/spring-projects/spring-security/issues/11027
			.anyRequest()
			.authenticated())
			.formLogin(
					(formLogin) -> formLogin.loginPage(this.adminServer.path("/login")).successHandler(successHandler))
			.logout((logout) -> logout.logoutUrl(this.adminServer.path("/logout")))
			.httpBasic(Customizer.withDefaults());

		http.addFilterAfter(new CustomCsrfFilter(), BasicAuthenticationFilter.class) // <5>
			.csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
				.ignoringRequestMatchers(
						PathPatternRequestMatcher.withDefaults()
							.matcher(HttpMethod.POST, this.adminServer.path("/instances")), // <6>
						PathPatternRequestMatcher.withDefaults()
							.matcher(HttpMethod.DELETE, this.adminServer.path("/instances/*")), // <6>
						PathPatternRequestMatcher.withDefaults().matcher(this.adminServer.path("/actuator/**")) // <7>
				));

		http.rememberMe((rememberMe) -> rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600));

		return http.build();

	}

	/**
	 * 创建内存用户详情服务
	 * @param passwordEncoder 密码编码器
	 * @return 包含配置用户的InMemoryUserDetailsManager实例
	 */
	@Bean
	public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
		UserDetails user = User.withUsername(security.getUser().getName())
			.password(passwordEncoder.encode(security.getUser().getPassword()))
			.roles("USER")
			.build();
		return new InMemoryUserDetailsManager(user);
	}

	/**
	 * 创建并返回一个BCrypt密码编码器实例
	 * @return BCryptPasswordEncoder实例
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
