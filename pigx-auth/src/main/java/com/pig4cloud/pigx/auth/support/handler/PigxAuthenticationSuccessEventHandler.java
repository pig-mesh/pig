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

package com.pig4cloud.pigx.auth.support.handler;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.dto.SysLogDTO;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.KeyStrResolver;
import com.pig4cloud.pigx.common.log.event.SysLogEvent;
import com.pig4cloud.pigx.common.log.util.LogTypeEnum;
import com.pig4cloud.pigx.common.log.util.SysLogUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * @author lengleng
 * @date 2022-06-02
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PigxAuthenticationSuccessEventHandler implements AuthenticationSuccessHandler {

	private static final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();

	private final StringRedisTemplate redisTemplate;

	private final ApplicationEventPublisher publisher;

	private final KeyStrResolver tenantKeyStrResolver;

	/**
	 * Called when a user has been successfully authenticated.
	 * @param request the request which caused the successful authentication
	 * @param response the response
	 * @param authentication the <tt>Authentication</tt> object which was created during
	 * the authentication process.
	 */
	@SneakyThrows
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {

		// 写入登录成功的日志
		OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
		Map<String, Object> map = accessTokenAuthentication.getAdditionalParameters();
		if (MapUtil.isNotEmpty(map)) {
			sendSuccessEventLog(request, accessTokenAuthentication, map);
		}

		// 清除账号历史锁定次数
		clearLoginFailureTimes(map);

		// 输出token
		sendAccessTokenResponse(response, authentication);
	}

	/**
	 * 记录登录成功事件
	 * @param request HttpServletRequest
	 * @param accessTokenAuthentication Authentication
	 * @param map 请求参数
	 */
	private void sendSuccessEventLog(HttpServletRequest request,
			OAuth2AccessTokenAuthenticationToken accessTokenAuthentication, Map<String, Object> map) {
		// 发送异步日志事件

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(accessTokenAuthentication);
		SecurityContextHolder.setContext(context);

		SysLogDTO logVo = SysLogUtils.getSysLog();
		logVo.setTitle("登录成功");
		logVo.setLogType(LogTypeEnum.NORMAL.getType());
		String startTimeStr = request.getHeader(CommonConstants.REQUEST_START_TIME);
		if (StrUtil.isNotBlank(startTimeStr)) {
			Long startTime = Long.parseLong(startTimeStr);
			Long endTime = System.currentTimeMillis();
			logVo.setTime(endTime - startTime);
		}

		logVo.setServiceId(accessTokenAuthentication.getRegisteredClient().getClientId());
		logVo.setCreateBy(MapUtil.getStr(map, SecurityConstants.DETAILS_USERNAME));
		logVo.setTenantId(Long.parseLong(tenantKeyStrResolver.key()));

		publisher.publishEvent(new SysLogEvent(logVo));
	}

	/**
	 * 清空登录失败的记录
	 * @param map
	 */
	private void clearLoginFailureTimes(Map<String, Object> map) {
		String key = String.format("%s:%s:%s", tenantKeyStrResolver.key(), CacheConstants.LOGIN_ERROR_TIMES,
				MapUtil.getStr(map, SecurityConstants.DETAILS_USERNAME));
		redisTemplate.delete(key);
	}

	private void sendAccessTokenResponse(HttpServletResponse response, Authentication authentication)
			throws IOException {

		OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

		OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
		OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
		Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
			.tokenType(accessToken.getTokenType())
			.scopes(accessToken.getScopes());
		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
			builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
		}
		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}
		if (!CollectionUtils.isEmpty(additionalParameters)) {
			builder.additionalParameters(additionalParameters);
		}
		OAuth2AccessTokenResponse accessTokenResponse = builder.build();
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

		// 无状态 注意删除 context 上下文的信息
		SecurityContextHolder.clearContext();
		this.accessTokenHttpResponseConverter.write(accessTokenResponse, null, httpResponse);
	}

}
