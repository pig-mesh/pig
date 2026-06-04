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

import com.pig4cloud.pig.admin.api.dto.SysLogDTO;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import com.pig4cloud.pig.common.log.util.SysLogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author zhangran
 * @date 2022-06-02
 *
 * 事件机制处理退出相关
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PigLogoutSuccessEventHandler implements ApplicationListener<LogoutSuccessEvent> {

	private final OAuth2AuthorizationService authorizationService;

	private final CacheManager cacheManager;

	@Override
	public void onApplicationEvent(LogoutSuccessEvent event) {
		Authentication authentication = (Authentication) event.getSource();
		if (authentication instanceof PreAuthenticatedAuthenticationToken) {
			// 来自 removeToken()，token 已删，仅记日志
			handle(authentication);
		}
		else {
			// 来自 Spring Security 标准登出，token 未删，需批量清理
			removeTokensByUsername(authentication.getName());
			handle(authentication);
		}
	}

	/**
	 * 处理退出成功方法
	 * <p>
	 * @param authentication 登录对象
	 */
	public void handle(Authentication authentication) {
		log.info("用户：{} 退出成功", authentication.getPrincipal());
		SysLogDTO logVo = SysLogUtils.getSysLog();
		logVo.setTitle("退出成功");
		// 发送异步日志事件
		Long startTime = System.currentTimeMillis();
		Long endTime = System.currentTimeMillis();
		logVo.setTime(endTime - startTime);

		// 设置对应的token
		logVo.setParams(WebUtils.getRequest().getHeader(HttpHeaders.AUTHORIZATION));

		// 这边设置ServiceId
		if (authentication instanceof PreAuthenticatedAuthenticationToken) {
			logVo.setServiceId(authentication.getCredentials().toString());
		}
		logVo.setCreateBy(authentication.getName());
	}

	/**
	 * 通过 principal name 批量删除该用户的全部 token。 Redis key
	 * 格式：token::username::{username}::{clientId}::{tokenId}
	 * @param username 用户名
	 */
	private void removeTokensByUsername(String username) {
		Set<String> keys = RedisUtils.keys("token::username::" + username + "*");
		for (String keyName : keys) {
			String[] parts = keyName.split("::");
			if (parts.length < 5) {
				continue;
			}
			String tokenValue = parts[4];
			OAuth2Authorization authorization = authorizationService.findByToken(tokenValue,
					OAuth2TokenType.ACCESS_TOKEN);
			if (authorization == null) {
				continue;
			}
			Cache userCache = cacheManager.getCache(CacheConstants.USER_DETAILS);
			if (userCache != null) {
				userCache.evict(authorization.getPrincipalName());
			}
			authorizationService.remove(authorization);
			log.debug("已清理用户 {} 的 token: {}", username, tokenValue);
		}
	}

}
