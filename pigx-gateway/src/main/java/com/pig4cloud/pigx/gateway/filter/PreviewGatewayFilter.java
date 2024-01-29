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

package com.pig4cloud.pigx.gateway.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2018/8/21 演示环境过滤处理
 */
@Slf4j
@Component
public class PreviewGatewayFilter extends AbstractGatewayFilterFactory {

	private static final String TOKEN = "token";

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();

			// GET，直接向下执行
			if (StrUtil.equalsIgnoreCase(request.getMethod().name(), HttpMethod.GET.name())
					|| StrUtil.containsIgnoreCase(request.getURI().getPath(), TOKEN)) {
				return chain.filter(exchange);
			}

			log.warn("演示环境不能操作-> {},{}", request.getMethod().name(), request.getURI().getPath());
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.LOCKED);
			return response.setComplete();
		};
	}

}
