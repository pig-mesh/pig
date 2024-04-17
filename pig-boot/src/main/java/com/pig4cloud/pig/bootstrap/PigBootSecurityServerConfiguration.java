/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pig.bootstrap;

import com.pig4cloud.pig.auth.support.core.PigDaoAuthenticationProvider;
import com.pig4cloud.pig.auth.support.filter.PasswordDecoderFilter;
import com.pig4cloud.pig.auth.support.filter.ValidateCodeFilter;
import com.pig4cloud.pig.auth.support.handler.PigAuthenticationFailureEventHandler;
import com.pig4cloud.pig.auth.support.handler.PigAuthenticationSuccessEventHandler;
import com.pig4cloud.pig.auth.support.password.OAuth2ResourceOwnerPasswordAuthenticationProvider;
import com.pig4cloud.pig.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationProvider;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.component.PermitAllUrlProperties;
import com.pig4cloud.pig.common.security.component.PigBearerTokenExtractor;
import com.pig4cloud.pig.common.security.component.ResourceAuthExceptionEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.stream.Collectors;

/**
 * @author lengleng 认证授权服务器配置
 */
@Configuration
@RequiredArgsConstructor
public class PigBootSecurityServerConfiguration {

	private final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

	private final OpaqueTokenIntrospector customOpaqueTokenIntrospector;

	private final AuthenticationConverter accessTokenRequestConverter;

	private final OAuth2AuthorizationService authorizationService;

	private final PigBearerTokenExtractor pigBearerTokenExtractor;

	private final PasswordDecoderFilter passwordDecoderFilter;

	private final OAuth2TokenGenerator oAuth2TokenGenerator;

	private final ValidateCodeFilter validateCodeFilter;

	private final PermitAllUrlProperties permitAllUrl;

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

		http.addFilterAfter(passwordDecoderFilter, UsernamePasswordAuthenticationFilter.class);
		http.addFilterAfter(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);
		http.apply(authorizationServerConfigurer.tokenEndpoint((tokenEndpoint) -> {// 个性化认证授权端点
			tokenEndpoint.accessTokenRequestConverter(accessTokenRequestConverter) // 注入自定义的授权认证Converter
				.accessTokenResponseHandler(new PigAuthenticationSuccessEventHandler()) // 登录成功处理器
				.errorResponseHandler(new PigAuthenticationFailureEventHandler());// 登录失败处理器
		}).clientAuthentication(oAuth2ClientAuthenticationConfigurer -> // 个性化客户端认证
		oAuth2ClientAuthenticationConfigurer.errorResponseHandler(new PigAuthenticationFailureEventHandler()))// 处理客户端认证异常
			.authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint// 授权码端点个性化confirm页面
				.consentPage(SecurityConstants.CUSTOM_CONSENT_PAGE_URI)))
			.authorizationService(authorizationService)
			.authorizationServerSettings(
					AuthorizationServerSettings.builder().issuer(SecurityConstants.PROJECT_LICENSE).build());

		AntPathRequestMatcher[] requestMatchers = permitAllUrl.getUrls()
			.stream()
			.map(AntPathRequestMatcher::new)
			.collect(Collectors.toList())
			.toArray(new AntPathRequestMatcher[] {});

		http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers(requestMatchers)
			.permitAll()
			.anyRequest()
			.authenticated())
			.oauth2ResourceServer(
					oauth2 -> oauth2.opaqueToken(token -> token.introspector(customOpaqueTokenIntrospector))
						.authenticationEntryPoint(resourceAuthExceptionEntryPoint)
						.bearerTokenResolver(pigBearerTokenExtractor))
			.exceptionHandling(configurer -> configurer.authenticationEntryPoint(resourceAuthExceptionEntryPoint))
			.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
			.csrf(AbstractHttpConfigurer::disable);

		DefaultSecurityFilterChain securityFilterChain = http.build();

		// 注入自定义授权模式实现
		addCustomOAuth2GrantAuthenticationProvider(http);
		return securityFilterChain;
	}

	/**
	 * 注入授权模式实现提供方
	 * <p>
	 * 1. 密码模式 </br>
	 * 2. 短信登录 </br>
	 */
	@SuppressWarnings("unchecked")
	private void addCustomOAuth2GrantAuthenticationProvider(HttpSecurity http) {
		AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
		OAuth2AuthorizationService authorizationService = http.getSharedObject(OAuth2AuthorizationService.class);

		OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider = new OAuth2ResourceOwnerPasswordAuthenticationProvider(
				authenticationManager, authorizationService, oAuth2TokenGenerator);

		OAuth2ResourceOwnerSmsAuthenticationProvider resourceOwnerSmsAuthenticationProvider = new OAuth2ResourceOwnerSmsAuthenticationProvider(
				authenticationManager, authorizationService, oAuth2TokenGenerator);

		// 处理 UsernamePasswordAuthenticationToken
		http.authenticationProvider(new PigDaoAuthenticationProvider());
		// 处理 OAuth2ResourceOwnerPasswordAuthenticationToken
		http.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);
		// 处理 OAuth2ResourceOwnerSmsAuthenticationToken
		http.authenticationProvider(resourceOwnerSmsAuthenticationProvider);
	}

}
