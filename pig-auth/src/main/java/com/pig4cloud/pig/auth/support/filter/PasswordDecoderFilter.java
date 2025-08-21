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

package com.pig4cloud.pig.auth.support.filter;

import java.io.IOException;
import java.util.Map;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.servlet.RepeatBodyRequestWrapper;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 密码解密过滤器：用于处理登录请求中的密码解密
 *
 * @author lengleng
 * @date 2025/05/30
 */
@Component
@RequiredArgsConstructor
public class PasswordDecoderFilter extends OncePerRequestFilter {

	private final AuthSecurityConfigProperties authSecurityConfigProperties;

	private static final String PASSWORD = "password";

	private static final String KEY_ALGORITHM = "AES";

	static {
		// 关闭hutool 强制关闭Bouncy Castle库的依赖
		SecureUtil.disableBouncyCastle();
	}

	/**
	 * 过滤器内部处理逻辑，用于处理登录请求中的密码解密
	 * @param request HTTP请求对象
	 * @param response HTTP响应对象
	 * @param chain 过滤器链
	 * @throws ServletException 如果发生servlet相关异常
	 * @throws IOException 如果发生I/O异常
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		// 不是登录请求，直接向下执行
		if (!StrUtil.containsAnyIgnoreCase(request.getRequestURI(), SecurityConstants.OAUTH_TOKEN_URL)) {
			chain.doFilter(request, response);
			return;
		}

		// 将请求流转换为可多次读取的请求流
		RepeatBodyRequestWrapper requestWrapper = new RepeatBodyRequestWrapper(request);
		Map<String, String[]> parameterMap = requestWrapper.getParameterMap();

		// 构建前端对应解密AES 因子
		AES aes = new AES(Mode.CFB, Padding.NoPadding,
				new SecretKeySpec(authSecurityConfigProperties.getEncodeKey().getBytes(), KEY_ALGORITHM),
				new IvParameterSpec(authSecurityConfigProperties.getEncodeKey().getBytes()));

		parameterMap.forEach((k, v) -> {
			String[] values = parameterMap.get(k);
			if (!PASSWORD.equals(k) || ArrayUtil.isEmpty(values)) {
				return;
			}

			// 解密密码
			String decryptPassword = aes.decryptStr(values[0]);
			parameterMap.put(k, new String[] { decryptPassword });
		});
		chain.doFilter(requestWrapper, response);
	}

}
