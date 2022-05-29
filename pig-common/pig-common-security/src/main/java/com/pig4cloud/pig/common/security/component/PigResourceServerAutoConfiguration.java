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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.client.RestTemplate;

/**
 * @author lengleng
 * @date 2022-05-29
 */
@EnableConfigurationProperties(PermitAllUrlProperties.class)
public class PigResourceServerAutoConfiguration {

	/**
	 * 权限表达式判断的具体实现
	 * @return @PreAuthorize("@pms.hasPermission('XXX')")
	 */
	@Bean("pms")
	public PermissionService permissionService() {
		return new PermissionService();
	}

	/**
	 * 解析请求token 的具体实现
	 * @param urlProperties 直接对外暴露的接口 不判断直接返回NULL
	 * @return null / token
	 */
	@Bean
	public PigBearerTokenExtractor pigBearerTokenExtractor(PermitAllUrlProperties urlProperties) {
		return new PigBearerTokenExtractor(urlProperties);
	}

	/**
	 * 资源服务器异常包装
	 * @param objectMapper jackson
	 * @return 处理器
	 */
	@Bean
	public ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint(ObjectMapper objectMapper) {
		return new ResourceAuthExceptionEntryPoint(objectMapper);
	}

	/**
	 * 注入 资源服务器 token 处理
	 * @param authorizationService token存储
	 * @param restTemplate 远程调用的实现 默认只会在第一次获取 jwk 配置
	 * @return OpaqueTokenIntrospector
	 */
	@Bean
	public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2AuthorizationService authorizationService,
			RestTemplate restTemplate) {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri("http://pigx-auth/oauth2/jwks")
				.restOperations(restTemplate).build();
		return new CustomOpaqueTokenIntrospector(authorizationService, jwtDecoder);
	}

}
