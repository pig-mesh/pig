package com.pig4cloud.pig.common.swagger.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * swagger 开启basic 认证
 *
 * @author Lht
 * @date 2021/8/8
 */
@Slf4j
@RequiredArgsConstructor
public class SwaggerBasicGatewayFilter implements GlobalFilter {

	private static final String API_URI = "/v2/api-docs";

	private static final String BASIC_PREFIX = "Basic ";

	private final SwaggerProperties swaggerProperties;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();

		if (!request.getURI().getPath().contains(API_URI)) {
			return chain.filter(exchange);
		}

		if (hasAuth(exchange)) {
			return chain.filter(exchange);
		}
		else {
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			response.getHeaders().add(HttpHeaders.WWW_AUTHENTICATE, "Basic Realm=\"pig\"");
			return response.setComplete();
		}
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
		if (!StringUtils.hasText(auth) || !auth.startsWith(BASIC_PREFIX)) {
			return Boolean.FALSE;
		}

		String username = swaggerProperties.getBasic().getUsername();
		String password = swaggerProperties.getBasic().getPassword();

		String encodeToString = Base64Utils
				.encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));

		return auth.equals(BASIC_PREFIX + encodeToString);
	}

}
