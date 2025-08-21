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

package com.pig4cloud.pig.common.core.util;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import com.pig4cloud.pig.common.core.exception.CheckedException;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

/**
 * Miscellaneous utilities for web applications.
 *
 * @author L.cm
 */
@UtilityClass
public class WebUtils extends org.springframework.web.util.WebUtils {

	private final String BASIC_ = "Basic ";

	// private final String UNKNOWN = "unknown";

	/**
	 * 判断是否ajax请求 spring ajax 返回含有 ResponseBody 或者 RestController注解
	 * @param handlerMethod HandlerMethod
	 * @return 是否ajax请求
	 */
	public boolean isBody(HandlerMethod handlerMethod) {
		ResponseBody responseBody = ClassUtils.getAnnotation(handlerMethod, ResponseBody.class);
		return responseBody != null;
	}

	/**
	 * 读取cookie
	 * @param name cookie name
	 * @return cookie value
	 */
	public String getCookieVal(String name) {
		if (WebUtils.getRequest().isPresent()) {
			return getCookieVal(WebUtils.getRequest().get(), name);
		}
		return null;
	}

	/**
	 * 读取cookie
	 * @param request HttpServletRequest
	 * @param name cookie name
	 * @return cookie value
	 */
	public String getCookieVal(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		return cookie != null ? cookie.getValue() : null;
	}

	/**
	 * 清除 某个指定的cookie
	 * @param response HttpServletResponse
	 * @param key cookie key
	 */
	public void removeCookie(HttpServletResponse response, String key) {
		setCookie(response, key, null, 0);
	}

	/**
	 * 设置cookie
	 * @param response HttpServletResponse
	 * @param name cookie name
	 * @param value cookie value
	 * @param maxAgeInSeconds maxage
	 */
	public void setCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAgeInSeconds);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	/**
	 * 获取 HttpServletRequest
	 * @return {HttpServletRequest}
	 */
	public Optional<HttpServletRequest> getRequest() {
		return Optional
			.ofNullable(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
	}

	/**
	 * 获取 HttpServletResponse
	 * @return {HttpServletResponse}
	 */
	public HttpServletResponse getResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	/**
	 * 从request 获取CLIENT_ID
	 * @return
	 */
	@SneakyThrows
	public String getClientId(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		return splitClient(header)[0];
	}

	@SneakyThrows
	public String getClientId() {
		if (WebUtils.getRequest().isPresent()) {
			String header = WebUtils.getRequest().get().getHeader(HttpHeaders.AUTHORIZATION);
			return splitClient(header)[0];
		}
		return null;
	}

	@SneakyThrows
	public String getClientSecret(HttpServletRequest request) {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		return splitClient(header)[1];
	}

	@NotNull
	private static String[] splitClient(String header) {
		if (header == null || !header.startsWith(BASIC_)) {
			throw new CheckedException("请求头中client信息为空");
		}
		byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		}
		catch (IllegalArgumentException e) {
			throw new CheckedException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, StandardCharsets.UTF_8);

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new CheckedException("Invalid basic authentication token");
		}
		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}

	/**
	 * 获取 bearer 令牌
	 * @return {@link String }
	 */
	@SneakyThrows
	public String getToken() {
		if (WebUtils.getRequest().isPresent()) {
			// 获取请求
			Optional<HttpServletRequest> requestOpt = WebUtils.getRequest();
			if (requestOpt.isPresent()) {
				HttpServletRequest request = requestOpt.get();
				// 获取Authorization头信息
				String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
				if (authorizationHeader != null && StrUtil.startWithAnyIgnoreCase(authorizationHeader, "Bearer ")) {
					// 返回去掉"Bearer "前缀的Token
					return authorizationHeader.substring(7);
				}
				else {
					// 处理Authorization头信息为空或格式不正确的情况
					return null;
				}
			}
		}
		// 请求不存在时返回null
		return null;
	}

}
