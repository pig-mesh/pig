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

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.EncFlagTypeEnum;
import com.pig4cloud.pigx.common.core.util.WebUtils;
import com.pig4cloud.pigx.gateway.config.GatewayConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author lengleng
 * @date 2020/1/8 密码解密工具类
 * <p>
 * 参考 ModifyRequestBodyGatewayFilterFactory 实现
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("all")
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory {

	private final List<HttpMessageReader<?>> messageReaders = HandlerStrategies.withDefaults().messageReaders();

	private static final String PASSWORD = "password";

	private static final String KEY_ALGORITHM = "AES";

	private final RedisTemplate redisTemplate;

	private final GatewayConfigProperties gatewayConfig;

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			// 1. 不是登录请求，直接向下执行
			if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), SecurityConstants.OAUTH_TOKEN_URL)) {
				return chain.filter(exchange);
			}

			// 2. 刷新token类型，直接向下执行
			String grantType = request.getQueryParams().getFirst("grant_type");
			if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
				return chain.filter(exchange);
			}

			// 3. 判断客户端是否需要解密，明文传输直接向下执行
			if (!isEncClient(request)) {
				return chain.filter(exchange);
			}

			// 4. 前端加密密文解密逻辑
			Class inClass = String.class;
			Class outClass = String.class;
			ServerRequest serverRequest = ServerRequest.create(exchange, messageReaders);

			// 解密生成新的报文
			Mono<?> modifiedBody = serverRequest.bodyToMono(inClass).flatMap(decryptAES());

			BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, outClass);
			HttpHeaders headers = new HttpHeaders();
			headers.putAll(exchange.getRequest().getHeaders());
			headers.remove(HttpHeaders.CONTENT_LENGTH);

			headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
			CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);
			return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
				ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
				return chain.filter(exchange.mutate().request(decorator).build());
			}));
		};
	}

	/**
	 * 根据请求的clientId 查询客户端配置是否是加密传输
	 * @param request 请求上下文
	 * @return true 加密传输 、 false 原文传输
	 */
	private boolean isEncClient(ServerHttpRequest request) {
		String header = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		String clientId = WebUtils.extractClientId(header).orElse(null);
		// 获取租户拼接区分租户的key
		String tenantId = request.getHeaders().getFirst(CommonConstants.TENANT_ID);
		String key = String.format("%s:%s:%s", StrUtil.isBlank(tenantId) ? CommonConstants.TENANT_ID_1 : tenantId,
				CacheConstants.CLIENT_FLAG, clientId);

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		Object val = redisTemplate.opsForValue().get(key);

		// 当配置不存在时，默认需要解密
		if (val == null) {
			return true;
		}

		JSONObject information = JSONUtil.parseObj(val.toString());
		if (StrUtil.equals(EncFlagTypeEnum.NO.getType(), information.getStr(CommonConstants.ENC_FLAG))) {
			return false;
		}
		return true;
	}

	/**
	 * 原文解密
	 * @return
	 */
	private Function decryptAES() {
		return s -> {
			// 获取请求密码并解密
			Map<String, String> inParamsMap = HttpUtil.decodeParamMap((String) s, CharsetUtil.CHARSET_UTF_8);
			if (inParamsMap.containsKey(PASSWORD)) {
				String password = SmUtil.sm4(HexUtil.decodeHex(gatewayConfig.getEncodeKey())).decryptStr(inParamsMap.get(PASSWORD));
				// 返回修改后报文字符
				inParamsMap.put(PASSWORD, password);
			}
			else {
				log.error("非法请求数据:{}", s);
			}

			// 使用
			return Mono.just(HttpUtil.toParams(inParamsMap, Charset.defaultCharset(), true));
		};
	}

	/**
	 * 报文转换
	 * @return
	 */
	private ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers,
			CachedBodyOutputMessage outputMessage) {
		return new ServerHttpRequestDecorator(exchange.getRequest()) {
			@Override
			public HttpHeaders getHeaders() {
				long contentLength = headers.getContentLength();
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.putAll(super.getHeaders());
				if (contentLength > 0) {
					httpHeaders.setContentLength(contentLength);
				}
				else {
					httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
				}
				return httpHeaders;
			}

			@Override
			public Flux<DataBuffer> getBody() {
				return outputMessage.getBody();
			}
		};
	}

}
