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

package com.pig4cloud.pigx.auth.handler;

import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.KeyStrResolver;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.common.security.handler.AuthenticationFailureHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author lengleng
 * @date 2022-04-11
 *
 * 登录操作次数锁定功能
 */
@Slf4j
@Component
@AllArgsConstructor
public class PigxAuthenticationFailureLockEventHandler implements AuthenticationFailureHandler {

	private final RedisTemplate<String, String> redisTemplate;

	private final KeyStrResolver tenantKeyStrResolver;

	private final RemoteUserService userService;

	/**
	 * 密码错误超过 5 此自动锁定
	 * <p>
	 * @param authenticationException 登录的authentication 对象
	 * @param authentication 登录的authenticationException 对象
	 * @param request 请求
	 * @param response 响应
	 */
	@Async
	@Override
	@SneakyThrows
	public void handle(AuthenticationException authenticationException, Authentication authentication,
			HttpServletRequest request, HttpServletResponse response) {
		// 只处理账号密码错误异常
		if (!(authenticationException instanceof BadCredentialsException)) {
			return;
		}

		String username = authentication.getName();
		String key = String.format("%s:%s:%s", CacheConstants.LOGIN_ERROR_TIMES, tenantKeyStrResolver.key(), username);
		Long deltaTimes = ParamResolver.getLong("LOGIN_ERROR_TIMES", 5L);
		Long times = redisTemplate.opsForValue().increment(key);

		// 自动过期时间
		Long deltaTime = ParamResolver.getLong("DELTA_TIME", 1L);
		redisTemplate.expire(key, deltaTime, TimeUnit.HOURS);

		if (deltaTimes <= times) {
			userService.lockUser(username, SecurityConstants.FROM_IN);
		}
	}

}
