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

package com.pig4cloud.pig.auth.support.handler;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.api.dto.SysLogDTO;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import com.pig4cloud.pig.common.data.resolver.ParamResolver;
import com.pig4cloud.pig.common.log.event.SysLogEvent;
import com.pig4cloud.pig.common.log.util.LogTypeEnum;
import com.pig4cloud.pig.common.log.util.SysLogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 认证失败处理器：处理用户认证失败事件
 *
 * @author lengleng
 * @date 2025/08/19
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("removal")
public class PigAuthenticationFailureEventHandler implements AuthenticationFailureHandler {

	private static final org.springframework.http.converter.json.MappingJackson2HttpMessageConverter errorHttpResponseConverter = new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter();

	private final ApplicationEventPublisher publisher;

	private final RemoteUserService userService;

	/**
	 * Called when an authentication attempt fails.
	 * @param request the request during which the authentication attempt occurred.
	 * @param response the response.
	 * @param exception the exception which was thrown to reject the authentication
	 * request.
	 */
	@Override
	@SneakyThrows
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) {
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		log.info("登录失败，异常：{}", exception.getLocalizedMessage());

		// 密码模式记录错误信息
		if (SecurityConstants.PASSWORD.equals(grantType)) {
			String username = request.getParameter(SecurityConstants.DETAILS_USERNAME);

			// 密码错误记录错误次数
			if (exception instanceof OAuth2AuthenticationException) {
				recordLoginFailureTimes(username);
			}

			// 记录登录失败错误信息
			sendFailureEventLog(request, exception, username);
		}

		// 写出错误信息
		sendErrorResponse(request, response, exception);
	}

	/**
	 * 记录失败日志
	 * @param request HttpServletRequest
	 * @param exception 错误日志
	 * @param username 用户名
	 */
	private void sendFailureEventLog(HttpServletRequest request, AuthenticationException exception, String username) {
		SysLogDTO logVo = SysLogUtils.getSysLog();
		logVo.setTitle("登录失败");
		logVo.setLogType(LogTypeEnum.ERROR.getType());
		logVo.setException(exception.getLocalizedMessage());
		// 发送异步日志事件
		String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
		if (StrUtil.isNotBlank(startTimeStr)) {
			Long startTime = Long.parseLong(startTimeStr);
			Long endTime = System.currentTimeMillis();
			logVo.setTime(endTime - startTime);
		}
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		String clientId = WebUtils.extractClientId(header).orElse(null);
		logVo.setServiceId(clientId);
		logVo.setCreateBy(username);
		publisher.publishEvent(new SysLogEvent(logVo));
	}

	/**
	 * 记录登录失败次数，超过阈值时调用接口锁定用户。
	 * <p>
	 * 若系统参数 LOGIN_ERROR_TIMES {@literal <=} 0，则禁用锁定功能并清除已有失败计数 key。
	 * </p>
	 * @param username 用户名
	 */
	private void recordLoginFailureTimes(String username) {
		String key = String.format("%s%s:%s", CacheConstants.GLOBALLY, CacheConstants.LOGIN_ERROR_TIMES, username);
		Long deltaTimes = ParamResolver.getLong("LOGIN_ERROR_TIMES", 5L);
		if (!isLoginFailureLockEnabled(deltaTimes)) {
			RedisUtils.delete(key);
			return;
		}

		// 使用 RedisUtils 执行原生 Redis 命令进行递增操作
		Long times = RedisUtils.increment(key, 1L);// 增加登录失败次数

		// 自动过期时间
		Long deltaTime = ParamResolver.getLong("DELTA_TIME", 1L);
		RedisUtils.expire(key, deltaTime * 3600); // 转换为秒

		if (deltaTimes <= times) {
			userService.lockUser(username);
		}
	}

	/**
	 * 判断登录失败锁定功能是否启用（参数值大于 0 表示启用）
	 * @param deltaTimes 系统参数 LOGIN_ERROR_TIMES，0 或 null 表示禁用
	 * @return 启用返回 true，禁用返回 false
	 */
	static boolean isLoginFailureLockEnabled(Long deltaTimes) {
		return deltaTimes != null && deltaTimes > 0;
	}

	/**
	 * 写出认证失败响应。
	 * <p>
	 * OAuth2 认证异常携带标准错误码和描述，需要保留错误码给前端识别；其他认证异常只返回本地化错误信息。
	 * @param request 当前认证请求
	 * @param response 当前认证响应
	 * @param exception 认证失败异常
	 * @throws IOException 响应写出失败时抛出
	 */
	private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
		httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
		String errorMessage;

		if (exception instanceof OAuth2AuthenticationException) {
			OAuth2AuthenticationException authorizationException = (OAuth2AuthenticationException) exception;
			// 优先使用 OAuth2 错误描述；没有描述时回退到标准错误码。
			errorMessage = StrUtil.isBlank(authorizationException.getError().getDescription())
					? authorizationException.getError().getErrorCode()
					: authorizationException.getError().getDescription();

			this.errorHttpResponseConverter.write(
					R.failed(authorizationException.getError().getErrorCode(), errorMessage),
					MediaType.APPLICATION_JSON, httpResponse);

		}
		else {
			// 非 OAuth2 异常没有标准错误码，直接返回 Spring Security 的本地化异常消息。
			errorMessage = exception.getLocalizedMessage();
			this.errorHttpResponseConverter.write(R.failed(errorMessage), MediaType.APPLICATION_JSON, httpResponse);
		}
	}

}
