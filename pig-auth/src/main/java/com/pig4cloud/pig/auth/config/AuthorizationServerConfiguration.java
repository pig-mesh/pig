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

import com.pig4cloud.pig.auth.support.CustomeOAuth2AccessTokenGenerator;
import com.pig4cloud.pig.auth.support.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.pig4cloud.pig.auth.support.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.component.PigDaoAuthenticationProvider;
import com.pig4cloud.pig.common.security.service.PigUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.oauth2.server.authorization.web.authentication.DelegatingAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2AuthorizationCodeAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2ClientCredentialsAuthenticationConverter;
import org.springframework.security.oauth2.server.authorization.web.authentication.OAuth2RefreshTokenAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;

/**
 * @author lengleng
 * @date 2022/5/27
 *
 * 认证服务器配置
 */
@Configuration
public class AuthorizationServerConfiguration {

	private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/login";

	/**
	 * 定义 Spring Security 的拦截器链，比如我们的 授权url、获取token的url 需要由那个过滤器来处理，此处配置这个。
	 *
	 * 1.开放oauth2 相关地址</br>
	 * 2.增加密码模式的扩展 方法 addCustomOAuth2ResourceOwnerPasswordAuthenticationProvider
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			OAuth2AuthorizationService authorizationService) throws Exception {
		OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

		http.apply(authorizationServerConfigurer.tokenEndpoint(
				(tokenEndpoint) -> tokenEndpoint.accessTokenRequestConverter(new DelegatingAuthenticationConverter(
						Arrays.asList(new OAuth2AuthorizationCodeAuthenticationConverter(),
								new OAuth2RefreshTokenAuthenticationConverter(),
								new OAuth2ClientCredentialsAuthenticationConverter(),
								new OAuth2ResourceOwnerPasswordAuthenticationConverter())))));
		authorizationServerConfigurer.authorizationEndpoint(
				authorizationEndpoint -> authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));

		authorizationServerConfigurer.authorizationService(authorizationService);

		RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

		http.requestMatcher(endpointsMatcher)
				.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
				.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher)).apply(authorizationServerConfigurer);

		SecurityFilterChain securityFilterChain = http.formLogin(Customizer.withDefaults()).build();

		addCustomOAuth2PasswordAuthenticationProvider(http);
		return securityFilterChain;
	}

	@Bean
	public ProviderSettings providerSettings() {
		return ProviderSettings.builder().build();
	}

	/**
	 * 令牌生成实现 client:username:uuid
	 * @return OAuth2TokenGenerator
	 */
	@Bean
	public OAuth2TokenGenerator oAuth2TokenGenerator() {
		CustomeOAuth2AccessTokenGenerator accessTokenGenerator = new CustomeOAuth2AccessTokenGenerator();
		accessTokenGenerator.setAccessTokenCustomizer(accessTokenCustomizer());
		return new DelegatingOAuth2TokenGenerator(accessTokenGenerator, new OAuth2RefreshTokenGenerator());
	}

	/**
	 * 获取令牌的扩展信息
	 * @return OAuth2TokenCustomizer
	 */
	@Bean
	public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> accessTokenCustomizer() {
		return context -> {
			OAuth2TokenClaimsSet.Builder claims = context.getClaims();
			claims.claim(SecurityConstants.DETAILS_LICENSE, SecurityConstants.PROJECT_LICENSE);
			String clientId = context.getAuthorizationGrant().getName();
			claims.claim(SecurityConstants.CLIENT_ID, clientId);
			// 客户端模式不返回具体用户信息
			if (SecurityConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
				return;
			}

			PigUser pigUser = (PigUser) context.getPrincipal().getPrincipal();
			claims.claim(SecurityConstants.DETAILS_USER, pigUser);

		};
	}

	/**
	 * 扩展密码模式
	 */
	@SuppressWarnings("all")
	private void addCustomOAuth2PasswordAuthenticationProvider(HttpSecurity http) {

		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
		OAuth2TokenGenerator oAuth2TokenGenerator = http.getSharedObject(OAuth2TokenGenerator.class);
		OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

		OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
				authenticationManager, authorizationService, oAuth2TokenGenerator);

		// 处理 UsernamePasswordAuthenticationToken
		http.authenticationProvider(new PigDaoAuthenticationProvider());
		// 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
		http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);

	}

}
