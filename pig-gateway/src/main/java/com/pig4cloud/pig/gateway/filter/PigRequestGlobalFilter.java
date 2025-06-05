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

package com.pig4cloud.pig.gateway.filter;

import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.addOriginalRequestUrl;

/**
 * 全局拦截器，作用于所有微服务
 * <p>
 * 1. 清洗请求头中的from参数 2. 重写StripPrefix = 1，支持全局路由
 *
 * @author lengleng
 * @date 2025/05/30
 */
public class PigRequestGlobalFilter implements GlobalFilter, Ordered {

	/**
	 * 处理Web请求并（可选地）通过给定的网关过滤器链委托给下一个过滤器
	 * @param exchange 当前服务器交换对象
	 * @param chain 提供委托给下一个过滤器的方式
	 * @return {@code Mono<Void>} 表示请求处理完成
	 */
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		// 1. 清洗请求头中from 参数
		ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> {
			httpHeaders.remove(SecurityConstants.FROM);
			// 设置请求时间
			httpHeaders.put(CommonConstants.REQUEST_START_TIME,
					Collections.singletonList(String.valueOf(System.currentTimeMillis())));
		}).build();

		// 2. 重写StripPrefix
		addOriginalRequestUrl(exchange, request.getURI());
		String rawPath = request.getURI().getRawPath();
		String newPath = "/" + Arrays.stream(StringUtils.tokenizeToStringArray(rawPath, "/"))
			.skip(1L)
			.collect(Collectors.joining("/"));

		ServerHttpRequest newRequest = request.mutate().path(newPath).build();
		exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newRequest.getURI());

		return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
	}

	@Override
	public int getOrder() {
		return 10;
	}

}
