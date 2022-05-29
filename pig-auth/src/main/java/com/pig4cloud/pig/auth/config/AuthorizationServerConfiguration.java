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

import com.nimbusds.jose.jwk.source.JWKSource;
import com.pig4cloud.pig.auth.support.OAuth2ResourceOwnerPasswordAuthenticationConverter;
import com.pig4cloud.pig.auth.support.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
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
 * @date 2022/5/27 认证服务器配置
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {

	private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/login";


	/**
	 * 定义 Spring Security 的拦截器链，比如我们的 授权url、获取token的url 需要由那个过滤器来处理，此处配置这个。 1.开放oauth2
	 * 相关地址 2.增加密码模式的扩展 方法 addCustomOAuth2ResourceOwnerPasswordAuthenticationProvider
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfigurer<HttpSecurity> authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer<>();

		http.apply(authorizationServerConfigurer.tokenEndpoint(
				(tokenEndpoint) -> tokenEndpoint.accessTokenRequestConverter(new DelegatingAuthenticationConverter(
						Arrays.asList(new OAuth2AuthorizationCodeAuthenticationConverter(),
								new OAuth2RefreshTokenAuthenticationConverter(),
								new OAuth2ClientCredentialsAuthenticationConverter(),
								new OAuth2ResourceOwnerPasswordAuthenticationConverter())))));
		authorizationServerConfigurer.authorizationEndpoint(
				authorizationEndpoint -> authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI));

		RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

		http.requestMatcher(endpointsMatcher)
				.authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
				.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher)).apply(authorizationServerConfigurer);

		SecurityFilterChain securityFilterChain = http.formLogin(Customizer.withDefaults()).build();

		// Custom configuration for Resource Owner Password grant type. Current
		// implementation has no support for Resource Owner
		// Password grant type
		addCustomOAuth2PasswordAuthenticationProvider(http);

		return securityFilterChain;
	}

	@Bean
	public OAuth2TokenGenerator tokenGenerator(JWKSource jwkSource) {
		JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource);
		JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
		OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
		OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
		return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
	}

	@Bean
	public ProviderSettings providerSettings() {
		return ProviderSettings.builder().build();
	}


	/**
	 *  扩展密码模式
	 */
	@SuppressWarnings("all")
	private void addCustomOAuth2PasswordAuthenticationProvider(HttpSecurity http) {

		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
		OAuth2TokenGenerator oAuth2TokenGenerator = http.getSharedObject(OAuth2TokenGenerator.class);
		OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

		OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider =
				new OAuth2ResourceOwnerPasswordAuthenticationProvider(authenticationManager,authorizationService,oAuth2TokenGenerator);

		// This will add new authentication provider in the list of existing authentication providers.
		http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);

	}

}
