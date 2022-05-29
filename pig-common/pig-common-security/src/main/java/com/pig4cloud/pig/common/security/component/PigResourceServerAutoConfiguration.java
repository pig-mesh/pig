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

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.service.PigUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author lengleng
 * @date 2020-06-23
 */
@EnableConfigurationProperties(PermitAllUrlProperties.class)
@RequiredArgsConstructor
public class PigResourceServerAutoConfiguration {


	private final RemoteUserService remoteUserService;

	private final CacheManager cacheManager;

	/**
	 * 配置授权服务器连接数据库，增加自定义序列化数据
	 */
	@Bean
	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
														   RegisteredClientRepository registeredClientRepository) {
		JdbcOAuth2AuthorizationService service = new JdbcOAuth2AuthorizationService(jdbcTemplate,
				registeredClientRepository);
		JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(
				registeredClientRepository);

		ObjectMapper objectMapper = new ObjectMapper();
		ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		objectMapper.registerModules(securityModules);
		objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

		// You will need to write the Mixin for your class so Jackson can marshall it.
		objectMapper.addMixIn(LinkedHashSet.class, LinkedHashSet.class);
		objectMapper.addMixIn(PigUser.class, PigUser.class);

		rowMapper.setObjectMapper(objectMapper);
		service.setAuthorizationRowMapper(rowMapper);

		return service;
	}


	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
		return new JdbcRegisteredClientRepository(jdbcTemplate);
	}

	/**
	 * 如果是授权码的流程，可能客户端申请了多个权限，比如：获取用户信息，修改用户信息， 此Service处理的是用户给这个客户端哪些权限，比如只给获取用户信息的权限
	 */
	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
																		 RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}


	@Bean("pms")
	public PermissionService permissionService() {
		return new PermissionService();
	}

	@Bean
	public PigBearerTokenExtractor pigBearerTokenExtractor(PermitAllUrlProperties urlProperties) {
		return new PigBearerTokenExtractor(urlProperties);
	}

	@Bean
	public ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint(ObjectMapper objectMapper) {
		return new ResourceAuthExceptionEntryPoint(objectMapper);
	}

	@Bean
	public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService) {
		return new CustomOpaqueTokenIntrospector(authorizationService);
	}

	@Bean
	public UserDetailsService userDetailsService(){
		return new PigUserDetailsServiceImpl(remoteUserService ,cacheManager);
	}

	// @Bean
	public JwtDecoder jwtDecoder() {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/auth/oauth2/jwks").build();
		return jwtDecoder;
	}




}
