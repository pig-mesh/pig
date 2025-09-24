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

package com.pig4cloud.pig.auth.endpoint;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.admin.api.feign.RemoteClientDetailsService;
import com.pig4cloud.pig.admin.api.vo.TokenVo;
import com.pig4cloud.pig.auth.support.handler.PigAuthenticationFailureEventHandler;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RedisUtils;
import com.pig4cloud.pig.common.core.util.RetOps;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.common.security.util.OAuth2EndpointUtils;
import com.pig4cloud.pig.common.security.util.OAuth2ErrorCodesExpand;
import com.pig4cloud.pig.common.security.util.OAuthClientException;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

/**
 * OAuth2 令牌端点控制器，提供令牌相关操作
 *
 * @author lengleng
 * @date 2025/05/30
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(description = "oauth", name = "OAuth2 令牌端点控制器管理模块")
public class PigTokenEndpoint {

	private final HttpMessageConverter<OAuth2AccessTokenResponse> accessTokenHttpResponseConverter = new OAuth2AccessTokenResponseHttpMessageConverter();

	private final AuthenticationFailureHandler authenticationFailureHandler = new PigAuthenticationFailureEventHandler();

	private final OAuth2AuthorizationService authorizationService;

	private final RemoteClientDetailsService clientDetailsService;

	private final CacheManager cacheManager;

	/**
	 * 授权码模式：认证页面
	 * @param modelAndView 视图模型对象
	 * @param error 表单登录失败处理回调的错误信息
	 * @return 包含登录页面视图和错误信息的ModelAndView对象
	 */
	@GetMapping("/token/login")
	@Operation(summary = "授权码模式：认证页面", description = "授权码模式：认证页面")
	public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
		modelAndView.setViewName("ftl/login");
		modelAndView.addObject("error", error);
		return modelAndView;
	}

	/**
	 * 授权码模式：确认页面
	 * @param principal 用户主体信息
	 * @param modelAndView 模型和视图对象
	 * @param clientId 客户端ID
	 * @param scope 请求的权限范围
	 * @param state 状态参数
	 * @return 包含确认页面信息的ModelAndView对象
	 */
	@GetMapping("/oauth2/confirm_access")
	@Operation(summary = "授权码模式：确认页面", description = "授权码模式：确认页面")
	public ModelAndView confirm(Principal principal, ModelAndView modelAndView,
			@RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
			@RequestParam(OAuth2ParameterNames.SCOPE) String scope,
			@RequestParam(OAuth2ParameterNames.STATE) String state) {
		SysOauthClientDetails clientDetails = RetOps.of(clientDetailsService.getClientDetailsById(clientId))
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
	 * 注销并删除令牌
	 * @param authHeader 认证头信息，包含Bearer token
	 * @return 返回操作结果，包含布尔值表示是否成功
	 */
	@DeleteMapping("/token/logout")
	@Operation(summary = "注销并删除令牌", description = "注销并删除令牌")
	public R<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		if (StrUtil.isBlank(authHeader)) {
			return R.ok();
		}

		String tokenValue = authHeader.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
		return removeToken(tokenValue);
	}

	/**
	 * 检查令牌有效性
	 * @param token 待验证的令牌
	 * @param response HTTP响应对象
	 * @param request HTTP请求对象
	 * @throws InvalidBearerTokenException 令牌无效或缺失时抛出异常
	 */
	@SneakyThrows
	@GetMapping("/token/check_token")
	@Operation(summary = "检查令牌有效性", description = "检查令牌有效性")
	public void checkToken(String token, HttpServletResponse response, HttpServletRequest request) {
		ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

		if (StrUtil.isBlank(token)) {
			httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
			this.authenticationFailureHandler.onAuthenticationFailure(request, response,
					new InvalidBearerTokenException(OAuth2ErrorCodesExpand.TOKEN_MISSING));
			return;
		}
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

		// 如果令牌不存在 返回401
		if (authorization == null || authorization.getAccessToken() == null) {
			this.authenticationFailureHandler.onAuthenticationFailure(request, response,
					new InvalidBearerTokenException(OAuth2ErrorCodesExpand.INVALID_BEARER_TOKEN));
			return;
		}

		Map<String, Object> claims = authorization.getAccessToken().getClaims();
		OAuth2AccessTokenResponse sendAccessTokenResponse = OAuth2EndpointUtils.sendAccessTokenResponse(authorization,
				claims);
		this.accessTokenHttpResponseConverter.write(sendAccessTokenResponse, MediaType.APPLICATION_JSON, httpResponse);
	}

	/**
	 * 删除令牌
	 * @param token 令牌
	 * @return 删除结果
	 */
	@Inner
	@DeleteMapping("/token/remove/{token}")
	@Operation(summary = "删除令牌", description = "删除令牌")
	public R<Boolean> removeToken(@PathVariable("token") String token) {
		OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		if (authorization == null) {
			return R.ok();
		}

		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getAccessToken();
		if (accessToken == null || StrUtil.isBlank(accessToken.getToken().getTokenValue())) {
			return R.ok();
		}
		// 清空用户信息（立即删除）
		cacheManager.getCache(CacheConstants.USER_DETAILS).evictIfPresent(authorization.getPrincipalName());
		// 清空access token
		authorizationService.remove(authorization);
		// 处理自定义退出事件，保存相关日志
		SpringContextHolder.publishEvent(new LogoutSuccessEvent(new PreAuthenticatedAuthenticationToken(
				authorization.getPrincipalName(), authorization.getRegisteredClientId())));
		return R.ok();
	}

	/**
	 * 分页查询令牌列表
	 * @param params 请求参数，包含分页参数current和size
	 * @return 分页结果，包含令牌信息列表
	 */
	@Inner
	@PostMapping("/token/page")
	@Operation(summary = "分页查询令牌列表", description = "分页查询令牌列表")
	public R<Page> tokenList(@RequestBody Map<String, Object> params) {
		// 根据分页参数获取对应数据
		String username = MapUtil.getStr(params, SecurityConstants.USERNAME);
		String pattern = String.format("%s::*", CacheConstants.PROJECT_OAUTH_ACCESS);
		int current = MapUtil.getInt(params, CommonConstants.CURRENT);
		int size = MapUtil.getInt(params, CommonConstants.SIZE);
		Page result = new Page(current, size);

		// 获取总数
		List<String> allKeys = RedisUtils.scan(pattern);
		result.setTotal(allKeys.size());

		List<String> pageKeys = RedisUtils.findKeysForPage(pattern, current - 1, size);
		List<OAuth2Authorization> pagedAuthorizations = RedisUtils.multiGet(pageKeys);

		// 转换为TokenVo
		List<TokenVo> tokenVoList = pagedAuthorizations.stream()
			.filter(Objects::nonNull)
			.map(this::convertToTokenVo)
			.filter(tokenVo -> {
				if (StrUtil.isBlank(username)) {
					return true;
				}
				return StrUtil.startWithAnyIgnoreCase(tokenVo.getUsername(), username);
			})
			.toList();

		if (StrUtil.isNotBlank(username)) {
			result.setTotal(tokenVoList.size());
		}

		result.setRecords(tokenVoList);
		return R.ok(result);
	}

	/**
	 * 将OAuth2Authorization转换为TokenVo
	 * @param authorization OAuth2授权对象
	 * @return TokenVo对象
	 */
	private TokenVo convertToTokenVo(OAuth2Authorization authorization) {
		TokenVo tokenVo = new TokenVo();
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
		return tokenVo;
	}

}
