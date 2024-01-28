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

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.pig4cloud.pig.gateway.config.GatewayConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * The type Validate code gateway filter.
 *
 * @author lengleng
 * @date 2018 /7/4 验证码处理
 */
@Slf4j
@RequiredArgsConstructor
public class ValidateCodeGatewayFilter extends AbstractGatewayFilterFactory<Object> {

	private final GatewayConfigProperties configProperties;

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 应用网关过滤器
	 * @param config 配置对象
	 * @return 网关过滤器
	 */
	@Override
	public GatewayFilter apply(Object config) {

		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			// 不是登录请求，直接向下执行
			if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), SecurityConstants.OAUTH_TOKEN_URL)) {
				return chain.filter(exchange);
			}

			// 客户端配置跳过，直接向下执行
			boolean isIgnoreClient = configProperties.getIgnoreClients().contains(WebUtils.getClientId(request));
			if (isIgnoreClient) {
				return chain.filter(exchange);
			}

			// 构建缓存body，可重复读获取form data
			return ServerWebExchangeUtils.cacheRequestBody(exchange, (serverHttpRequest) -> {
				// get cacheRequestBody
				DataBuffer cachedRequestBody = exchange.getAttribute("cachedRequestBody");
				CharBuffer charBuffer = StandardCharsets.UTF_8
					.decode(Objects.requireNonNull(cachedRequestBody).asByteBuffer());
				Map<String, String> requestBodyMap = HttpUtil.decodeParamMap(charBuffer.toString(),
						CharsetUtil.CHARSET_UTF_8);
				// 刷新请求跳过，直接向下执行
				if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, requestBodyMap.get("grant_type"))) {
					return chain.filter(exchange.mutate().request(serverHttpRequest).build());
				}

				// 根据 randomStr 参数判断验证码是否正常
				String code = requestBodyMap.get("code");
				String randomStr = requestBodyMap.getOrDefault("randomStr",
						requestBodyMap.get(SecurityConstants.SMS_PARAMETER_NAME));
				checkCode(code, randomStr);

				return chain.filter(exchange.mutate().request(serverHttpRequest).build());
			});
		};
	}

	/**
	 * 检查验证码，错误扔出 ValidateCodeException GlobalExceptionHandler统一处理
	 * @param code 验证码
	 * @param randomStr 请求参数
	 * @throws ValidateCodeException 验证码异常
	 */
	@SneakyThrows
	private void checkCode(String code, String randomStr) {
		if (CharSequenceUtil.isBlank(code)) {
			throw new ValidateCodeException("验证码不能为空");
		}

		String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;

		Object codeObj = redisTemplate.opsForValue().get(key);

		if (ObjectUtil.isEmpty(codeObj) || !code.equals(codeObj)) {
			throw new ValidateCodeException("验证码不合法");
		}

		redisTemplate.delete(key);
	}

}
