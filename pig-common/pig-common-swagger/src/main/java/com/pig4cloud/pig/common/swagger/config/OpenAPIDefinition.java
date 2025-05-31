/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.pig4cloud.pig.common.swagger.config;

import com.pig4cloud.pig.common.swagger.support.SwaggerProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger配置类，用于配置OpenAPI定义
 * <p>
 * 支持通过配置控制Swagger的启用状态，并提供OAuth2安全认证配置
 * </p>
 *
 * @author lengleng
 * @date 2025/05/31
 */
@RequiredArgsConstructor
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
public class OpenAPIDefinition extends OpenAPI implements InitializingBean, ApplicationContextAware {

	@Setter
	private String path;

	/**
	 * 应用上下文
	 */
	private ApplicationContext applicationContext;

	/**
	 * 创建并配置OAuth2安全方案
	 * @param swaggerProperties Swagger配置属性
	 * @return 配置好的SecurityScheme对象
	 */
	private SecurityScheme securityScheme(SwaggerProperties swaggerProperties) {
		OAuthFlow clientCredential = new OAuthFlow();
		clientCredential.setTokenUrl(swaggerProperties.getTokenUrl());
		clientCredential.setScopes(new Scopes().addString(swaggerProperties.getScope(), swaggerProperties.getScope()));
		OAuthFlows oauthFlows = new OAuthFlows();
		oauthFlows.password(clientCredential);
		SecurityScheme securityScheme = new SecurityScheme();
		securityScheme.setType(SecurityScheme.Type.OAUTH2);
		securityScheme.setFlows(oauthFlows);
		return securityScheme;
	}

	/**
	 * 初始化Swagger配置
	 * @throws Exception 初始化过程中可能抛出的异常
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		SwaggerProperties swaggerProperties = applicationContext.getBean(SwaggerProperties.class);
		this.info(new Info().title(swaggerProperties.getTitle()));
		// oauth2.0 password
		this.schemaRequirement(HttpHeaders.AUTHORIZATION, this.securityScheme(swaggerProperties));
		// servers
		List<Server> serverList = new ArrayList<>();
		serverList.add(new Server().url(swaggerProperties.getGateway() + "/" + path));
		this.servers(serverList);
		// 支持参数平铺
		SpringDocUtils.getConfig().addSimpleTypesForParameterObject(Class.class);
	}

	/**
	 * 设置应用上下文
	 * @param applicationContext 应用上下文对象
	 * @throws BeansException 如果设置上下文时发生错误
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
