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

package com.pig4cloud.pigx.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.security.component.PigxCommenceAuthExceptionEntryPoint;
import com.pig4cloud.pigx.common.security.component.PigxWebResponseExceptionTranslator;
import com.pig4cloud.pigx.common.security.grant.ResourceOwnerCustomeAppTokenGranter;
import com.pig4cloud.pigx.common.security.service.PigxCustomTokenServices;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author lengleng
 * @date 2018/6/22 认证服务器配置
 */
@Configuration
@AllArgsConstructor
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private final ClientDetailsService pigxClientDetailsServiceImpl;

	private final AuthenticationManager authenticationManagerBean;

	private final UserDetailsService pigxUserDetailsService;

	private final AuthorizationCodeServices authorizationCodeServices;

	private final AuthenticationManager authenticationManager;

	private final TokenStore redisTokenStore;

	private final TokenEnhancer tokenEnhancer;

	private final ObjectMapper objectMapper;

	@Override
	@SneakyThrows
	public void configure(ClientDetailsServiceConfigurer clients) {
		clients.withClientDetails(pigxClientDetailsServiceImpl);
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
		oauthServer.allowFormAuthenticationForClients()
				.authenticationEntryPoint(new PigxCommenceAuthExceptionEntryPoint(objectMapper))
				.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST).tokenServices(tokenServices())
				.tokenStore(redisTokenStore).tokenEnhancer(tokenEnhancer)
				.authorizationCodeServices(authorizationCodeServices).authenticationManager(authenticationManagerBean)
				.reuseRefreshTokens(false).pathMapping("/oauth/confirm_access", "/token/confirm_access")
				.exceptionTranslator(new PigxWebResponseExceptionTranslator());

		// 注入自定义认证类型
		setTokenGranter(endpoints);
	}

	/**
	 * 自定义 APP 认证类型
	 * @param endpoints AuthorizationServerEndpointsConfigurer
	 */
	private void setTokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
		// 获取默认授权类型
		TokenGranter tokenGranter = endpoints.getTokenGranter();
		ArrayList<TokenGranter> tokenGranters = new ArrayList<>(Arrays.asList(tokenGranter));
		ResourceOwnerCustomeAppTokenGranter resourceOwnerCustomeAppTokenGranter = new ResourceOwnerCustomeAppTokenGranter(
				authenticationManager, endpoints.getTokenServices(), endpoints.getClientDetailsService(),
				endpoints.getOAuth2RequestFactory());
		tokenGranters.add(resourceOwnerCustomeAppTokenGranter);
		CompositeTokenGranter compositeTokenGranter = new CompositeTokenGranter(tokenGranters);
		endpoints.tokenGranter(compositeTokenGranter);
	}

	@Bean
	public PigxCustomTokenServices tokenServices() {
		PigxCustomTokenServices tokenServices = new PigxCustomTokenServices();
		tokenServices.setTokenStore(redisTokenStore);
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setReuseRefreshToken(false);
		tokenServices.setClientDetailsService(pigxClientDetailsServiceImpl);
		tokenServices.setTokenEnhancer(tokenEnhancer);
		addUserDetailsService(tokenServices, pigxUserDetailsService);
		return tokenServices;
	}

	private void addUserDetailsService(PigxCustomTokenServices tokenServices, UserDetailsService userDetailsService) {
		if (userDetailsService != null) {
			PreAuthenticatedAuthenticationProvider provider = new PreAuthenticatedAuthenticationProvider();
			provider.setPreAuthenticatedUserDetailsService(new UserDetailsByNameServiceWrapper<>(userDetailsService));
			tokenServices.setAuthenticationManager(new ProviderManager(Collections.singletonList(provider)));
		}
	}

}
