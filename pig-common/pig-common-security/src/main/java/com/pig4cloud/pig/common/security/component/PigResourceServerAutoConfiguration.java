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
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.service.PigClientDetailsService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import javax.sql.DataSource;

/**
 * @author lengleng
 * @date 2020-06-23
 */
@EnableConfigurationProperties(PermitAllUrlProperties.class)
public class PigResourceServerAutoConfiguration {

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
	@Primary
	public ResourceServerTokenServices resourceServerTokenServices(
			DataSource dataSource) {
		NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri("http://localhost:8080/auth/oauth2/jwks").build();

		PigClientDetailsService clientDetailsService = new PigClientDetailsService(dataSource);
		clientDetailsService.setSelectClientDetailsSql(SecurityConstants.DEFAULT_SELECT_STATEMENT);
		clientDetailsService.setFindClientDetailsSql(SecurityConstants.DEFAULT_FIND_STATEMENT);
		return new PigLocalResourceServerTokenServices(jwtDecoder, clientDetailsService);
	}

}
