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

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.gateway.config.GatewayConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyRequestBodyGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author lengleng
 * @date 2019 /2/1 密码解密工具类
 */
@Slf4j
@RequiredArgsConstructor
public class PasswordDecoderFilter extends AbstractGatewayFilterFactory {

	private final ModifyRequestBodyGatewayFilterFactory modifyRequestBodyFilter;

	private static final String PASSWORD = "password";

	private static final String KEY_ALGORITHM = "AES";

	private final GatewayConfigProperties gatewayConfig;

	static {
		// 关闭hutool 强制关闭Bouncy Castle库的依赖
		SecureUtil.disableBouncyCastle();
	}

	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			// 不是登录请求，直接向下执行
			if (!StrUtil.containsAnyIgnoreCase(request.getURI().getPath(), SecurityConstants.OAUTH_TOKEN_URL)) {
				return chain.filter(exchange);
			}

			return modifyRequestBodyFilter
				.apply(new ModifyRequestBodyGatewayFilterFactory.Config().setRewriteFunction(String.class, String.class,
						(webExchange, body) -> Mono.just(modifyRequestPassword(body))))
				.filter(exchange, chain);
		};
	}

	/**
	 * 修改请求报文的密码密文为名为
	 * @param requestBody 请求报文
	 * @return 修改后的报文
	 */
	private String modifyRequestPassword(String requestBody) {
		// 构建前端对应解密AES 因子
		AES aes = new AES(Mode.CFB, Padding.NoPadding,
				new SecretKeySpec(gatewayConfig.getEncodeKey().getBytes(), KEY_ALGORITHM),
				new IvParameterSpec(gatewayConfig.getEncodeKey().getBytes()));

		// 获取请求密码并解密
		Map<String, String> inParamsMap = HttpUtil.decodeParamMap(requestBody, CharsetUtil.CHARSET_UTF_8);
		if (inParamsMap.containsKey(PASSWORD)) {
			String password = aes.decryptStr(inParamsMap.get(PASSWORD));
			// 返回修改后报文字符
			inParamsMap.put(PASSWORD, password);
		}

		return HttpUtil.toParams(inParamsMap, Charset.defaultCharset(), true);
	}

}
