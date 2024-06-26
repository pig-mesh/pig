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

package com.pig4cloud.pigx.auth.endpoint;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pigx.admin.api.entity.SysTenant;
import com.pig4cloud.pigx.admin.api.feign.RemoteClientDetailsService;
import com.pig4cloud.pigx.admin.api.feign.RemoteTenantService;
import com.pig4cloud.pigx.admin.api.vo.TokenVO;
import com.pig4cloud.pigx.auth.support.handler.PigxAuthenticationFailureEventHandler;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.KeyStrResolver;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.RetOps;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.OAuth2ErrorCodesExpand;
import com.pig4cloud.pigx.common.security.util.OAuthClientException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2019/2/1 删除token端点
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class PigxTokenEndpoint {

	private final PigxAuthenticationFailureEventHandler authenticationFailureHandler;

	private final OAuth2AuthorizationService authorizationService;

	private final RemoteClientDetailsService clientDetailsService;

	private final RedisTemplate<String, Object> redisTemplate;

	private final RemoteTenantService tenantService;

	private final KeyStrResolver tenantKeyStrResolver;

	private final CacheManager cacheManager;

	/**
	 * 认证页面
	 * @param modelAndView
	 * @param error 表单登录失败处理回调的错误信息
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
		modelAndView.setViewName("ftl/login");
		modelAndView.addObject("error", error);

		R<List<SysTenant>> tenantList = tenantService.list();
		modelAndView.addObject("tenantList", tenantList.getData());
		return modelAndView;
	}

	@GetMapping("/confirm_access")
	public ModelAndView confirm(Principal principal, ModelAndView modelAndView,
			@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
			@RequestParam(OAuth2ParameterNames.SCOPE) String scope,
			@RequestParam(OAuth2ParameterNames.STATE) String state) {
		SysOauthClientDetails clientDetails = RetOps
			.of(clientDetailsService.getClientDetailsById(clientId))
			.getData()
			.orElseThrow(() -> new OAuthClientException("clientId 不合法"));

		Set<String> authorizedScopes = StringUtils.commaDelimitedListToSet(clientDetails.getScope());
		modelAndView.addObject("clientId", clientId);
		modelAndView.addObject("state", state);
		modelAndView.addObject("scopeList", authorizedScopes);
		modelAndView.addObject("principalName", principal.getName());
		modelAndView.setViewName("ftl/confirm");
		return modelAndView;
	}

	/**
	 * 退出并删除token
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public R<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		if (StrUtil.isBlank(authHeader)) {
			return R.ok();
		}

		String tokenValue = authHeader.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		return removeToken(tokenValue);
	}

	/**
	 * 校验token
	 * @param token 令牌
	 * @return
	 */
	@SneakyThrows
	@GetMapping("/check_token")
	public R<OAuth2AccessToken> checkToken(String token, HttpServletResponse response, HttpServletRequest request) {
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

		if (StrUtil.isBlank(token)) {
			httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
			this.authenticationFailureHandler.onAuthenticationFailure(request, response,
					new InvalidBearerTokenException(OAuth2ErrorCodesExpand.TOKEN_MISSING));
			return R.failed();
		}
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

		// 如果令牌不存在 返回401
		if (authorization == null || authorization.getAccessToken() == null) {
			this.authenticationFailureHandler.onAuthenticationFailure(request, response,
					new InvalidBearerTokenException(OAuth2ErrorCodesExpand.INVALID_BEARER_TOKEN));
		}

		// 获取令牌
		return R.ok(Objects.requireNonNull(authorization).getAccessToken().getToken());
	}

	/**
	 * 令牌管理调用
	 * @param token token
	 */
	@Inner
	@DeleteMapping("/{token}")
	public R<Boolean> removeToken(@PathVariable("token") String token) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (authorization == null) {
			return R.ok();
		}

		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		if (accessToken == null || StrUtil.isBlank(accessToken.getToken().getTokenValue())) {
			return R.ok();
		}
		// 清空用户信息
		cacheManager.getCache(CacheConstants.USER_DETAILS).evict(authorization.getPrincipalName());
		// 清空access token
		authorizationService.remove(authorization);
		// 处理自定义退出事件，保存相关日志
		SpringContextHolder.publishEvent(new LogoutSuccessEvent(new PreAuthenticatedAuthenticationToken(
				authorization.getPrincipalName(), authorization.getRegisteredClientId())));
		return R.ok();
	}

	/**
	 * 查询token
	 * @param params 分页参数
	 * @return
	 */
	@Inner
	@PostMapping("/page")
	public R<Page<TokenVO>> tokenList(@RequestBody Map<String, Object> params) {
		// 根据分页参数获取对应数据
		String key = String.format("%s::%s::*", tenantKeyStrResolver.key(), CacheConstants.PROJECT_OAUTH_ACCESS);
		int current = MapUtil.getInt(params, CommonConstants.CURRENT);
		int size = MapUtil.getInt(params, CommonConstants.SIZE);
		Set<String> keys = redisTemplate.keys(key);
		List<String> pages = keys.stream().skip((current - 1) * size).limit(size).collect(Collectors.toList());
		Page<TokenVO> result = new Page(current, size);

		List<TokenVO> tokenVoList = redisTemplate.opsForValue().multiGet(pages).stream().map(obj -> {
			OAuth2Authorization authorization = (OAuth2Authorization) obj;
			TokenVO tokenVo = new TokenVO();
			tokenVo.setClientId(authorization.getRegisteredClientId());
			tokenVo.setId(authorization.getId());
			tokenVo.setUsername(authorization.getPrincipalName());
			OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
			tokenVo.setAccessToken(accessToken.getToken().getTokenValue());

			String expiresAt = TemporalAccessorUtil.format(accessToken.getToken().getExpiresAt(),
					DatePattern.NORM_DATETIME_PATTERN);
			tokenVo.setExpiresAt(expiresAt);

			String issuedAt = TemporalAccessorUtil.format(accessToken.getToken().getIssuedAt(),
					DatePattern.NORM_DATETIME_PATTERN);
			tokenVo.setIssuedAt(issuedAt);

			Map<String, Object> attributes = authorization.getAttributes();
			Authentication authentication = (Authentication) attributes.get(Principal.class.getName());

			if (Objects.isNull(authentication)) {
				return tokenVo;
			}

			PigxUser pigxUser = (PigxUser) authentication.getPrincipal();
			tokenVo.setUserId(pigxUser.getId());
			return tokenVo;
		}).filter(tokenVo -> {
			// 根据用户名过滤
			String username = MapUtil.getStr(params, SecurityConstants.DETAILS_USERNAME);
			if (StrUtil.isBlank(username)) {
				return true;
			}
			return tokenVo.getUsername().contains(username);
		}).collect(Collectors.toList());
		result.setRecords(tokenVoList);
		result.setTotal(keys.size());
		return R.ok(result);
	}

	@Inner
	@GetMapping("/query-token")
	public R queryToken(String token) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		return R.ok(authorization);
	}

}
