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

package com.pig4cloud.pigx.common.gateway.filter;

import com.pig4cloud.pigx.common.gateway.rule.GrayLoadBalancer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancerUriTools;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.DelegatingServiceInstance;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author lengleng
 * @date 2020/1/11
 */
@Slf4j
public class GrayReactiveLoadBalancerClientFilter extends ReactiveLoadBalancerClientFilter {

	private static final int LOAD_BALANCER_CLIENT_FILTER_ORDER = 10150;

	private GatewayLoadBalancerProperties loadBalancerProperties;

	private GrayLoadBalancer grayLoadBalancer;

	public GrayReactiveLoadBalancerClientFilter(GatewayLoadBalancerProperties loadBalancerProperties,
			LoadBalancerProperties properties, GrayLoadBalancer grayLoadBalancer) {
		super(null, loadBalancerProperties);
		this.grayLoadBalancer = grayLoadBalancer;
		this.loadBalancerProperties = loadBalancerProperties;
	}

	@Override
	public int getOrder() {
		return LOAD_BALANCER_CLIENT_FILTER_ORDER;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		URI url = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
		String schemePrefix = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_SCHEME_PREFIX_ATTR);
		if (url == null || (!"lb".equals(url.getScheme()) && !"lb".equals(schemePrefix))) {
			return chain.filter(exchange);
		}
		// preserve the original url
		ServerWebExchangeUtils.addOriginalRequestUrl(exchange, url);

		if (log.isTraceEnabled()) {
			log.trace(ReactiveLoadBalancerClientFilter.class.getSimpleName() + " url before: " + url);
		}

		return choose(exchange).doOnNext(response -> {

			if (!response.hasServer()) {
				throw NotFoundException.create(loadBalancerProperties.isUse404(),
						"Unable to find instance for " + url.getHost());
			}

			URI uri = exchange.getRequest().getURI();

			// if the `lb:<scheme>` mechanism was used, use `<scheme>` as the default,
			// if the loadbalancer doesn't provide one.
			String overrideScheme = null;
			if (schemePrefix != null) {
				overrideScheme = url.getScheme();
			}

			DelegatingServiceInstance serviceInstance = new DelegatingServiceInstance(response.getServer(),
					overrideScheme);

			URI requestUrl = LoadBalancerUriTools.reconstructURI(serviceInstance, uri);

			if (log.isTraceEnabled()) {
				log.trace("LoadBalancerClientFilter url chosen: " + requestUrl);
			}
			exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, requestUrl);
		}).then(chain.filter(exchange));
	}

	private Mono<Response<ServiceInstance>> choose(ServerWebExchange exchange) {
		URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
		ServiceInstance serviceInstance = grayLoadBalancer.choose(uri.getHost(), exchange.getRequest());
		return Mono.just(new DefaultResponse(serviceInstance));
	}

}
