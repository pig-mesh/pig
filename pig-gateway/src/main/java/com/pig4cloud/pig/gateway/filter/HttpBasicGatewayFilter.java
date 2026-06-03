/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pig.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author lengleng
 * @date 2018/10/30
 * <p>
 * 自定义basic认证，针对特殊场景使用
 */
@Slf4j
@Component
public class HttpBasicGatewayFilter extends AbstractGatewayFilterFactory {

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			if (hasAuth(exchange)) {
				return chain.filter(exchange);
			}
			else {
				ServerHttpResponse response = exchange.getResponse();
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				response.getHeaders().add(HttpHeaders.WWW_AUTHENTICATE, "Basic Realm=\"pig\"");
				return response.setComplete();
			}
		};
	}

	/**
	 * 简单的basic认证
	 * @param exchange 上下文
	 * @return 是否有权限
	 */
	private boolean hasAuth(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		log.info("Basic认证信息为：{}", auth);
		return true;
	}

}
