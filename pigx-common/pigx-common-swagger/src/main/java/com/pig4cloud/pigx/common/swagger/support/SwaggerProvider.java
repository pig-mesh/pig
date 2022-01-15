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

package com.pig4cloud.pigx.common.swagger.support;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NameUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sywd 聚合接口文档注册，和zuul实现类似
 */
@RequiredArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {

	private static final String API_URI = "/v2/api-docs";

	private final RouteDefinitionRepository routeDefinitionRepository;

	private final SwaggerProperties swaggerProperties;

	private final DiscoveryClient discoveryClient;

	@Override
	public List<SwaggerResource> get() {
		List<RouteDefinition> routes = new ArrayList<>();
		routeDefinitionRepository.getRouteDefinitions()
				// swagger 显示服务名称根据 路由的order 字段进行升序排列
				.sort(Comparator.comparingInt(RouteDefinition::getOrder)).subscribe(routes::add);

		return routes.stream()
				.flatMap(routeDefinition -> routeDefinition.getPredicates().stream()
						.filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
						.filter(predicateDefinition -> !swaggerProperties.getIgnoreProviders()
								.contains(routeDefinition.getId()))
						.map(predicateDefinition -> swaggerResource(routeDefinition.getId(),
								predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0").replace("/**",
										API_URI))))
				// 只显示注册中心 内已经正确启动的服务
				.filter(swaggerResource -> discoveryClient.getServices().stream()
						.anyMatch(s -> s.equals(swaggerResource.getName())))
				.collect(Collectors.toList());
	}

	private static SwaggerResource swaggerResource(String name, String location) {
		SwaggerResource swaggerResource = new SwaggerResource();
		swaggerResource.setName(name);
		swaggerResource.setLocation(location);
		swaggerResource.setSwaggerVersion("2.0");
		return swaggerResource;
	}

}
