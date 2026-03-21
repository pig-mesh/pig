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

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.TemporalAccessorUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.admin.api.feign.RemoteClientDetailsService;
import com.pig4cloud.pig.admin.api.vo.TokenVO;
import com.pig4cloud.pig.auth.support.core.AuthCaptchaSupport;
import com.pig4cloud.pig.auth.support.core.AuthErrorCodes;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RetOps;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import com.pig4cloud.pig.common.security.annotation.Inner;
import com.pig4cloud.pig.common.security.util.OAuth2ErrorCodesExpand;
import com.pig4cloud.pig.common.security.util.OAuthClientException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author lengleng
 * @date 2019/2/1 删除token端点
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PigTokenEndpoint {

    private static final String DEFAULT_FORM_CLIENT_ID = "pig";

    private final OAuth2AuthorizationService authorizationService;

    private final RemoteClientDetailsService clientDetailsService;

    private final CacheManager cacheManager;

    private final AuthCaptchaSupport authCaptchaSupport;

    /**
     * 认证页面
     *
     * @param modelAndView
     * @param error        表单登录失败处理回调的错误信息
     * @return ModelAndView
     */
    @GetMapping("/token/login")
    public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error,
                                HttpServletRequest request, HttpServletResponse response) {
        modelAndView.setViewName("ftl/login");
        // Note: XSS prevention is handled by FreeMarker template using ?html directive
        modelAndView.addObject("error", error);
        return modelAndView;
    }

    /**
     * 授权码模式：确认页面
     *
     * @return {@link ModelAndView }
     */
    @GetMapping("/oauth2/confirm_access")
    public ModelAndView confirm(Principal principal, ModelAndView modelAndView,
                                @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId,
                                @RequestParam(OAuth2ParameterNames.SCOPE) String scope,
                                @RequestParam(OAuth2ParameterNames.STATE) String state) {
        SysOauthClientDetails clientDetails = RetOps.of(clientDetailsService.getClientDetailsById(clientId))
                .getData()
                .orElseThrow(() -> new OAuthClientException(OAuth2ErrorCodes.INVALID_CLIENT,
                        MsgUtils.getMessage(AuthErrorCodes.AUTH_CLIENT_INVALID)));

        Set<String> authorizedScopes = StringUtils.commaDelimitedListToSet(clientDetails.getScope());
        // Note: XSS prevention is handled by FreeMarker template using ?html directive
        modelAndView.addObject("clientId", clientId);
        modelAndView.addObject("state", state);
        modelAndView.addObject("scopeList", authorizedScopes);
        modelAndView.addObject("principalName", principal.getName());
        modelAndView.setViewName("ftl/confirm");
        return modelAndView;
    }

    /**
     * 注销并删除令牌
     *
     * @param authHeader auth 标头
     * @return {@link R }<{@link Boolean }>
     */
    @DeleteMapping("/token/logout")
    public R<Boolean> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StrUtil.isBlank(authHeader)) {
            return R.ok();
        }

        String tokenValue = authHeader.replace(OAuth2AccessToken.TokenType.BEARER.getValue(), StrUtil.EMPTY).trim();
        return removeToken(tokenValue);
    }

    /**
     * 校验token
     *
     * @param token 令牌
     * @return
     */
    @SneakyThrows
    @GetMapping("/token/check_token")
    public R<OAuth2AccessToken> checkToken(String token, HttpServletResponse response, HttpServletRequest request) {
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        if (StrUtil.isBlank(token)) {
            httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return R.failed(OAuth2ErrorCodesExpand.TOKEN_MISSING);
        }
        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

        // 如果令牌不存在 返回401
        if (authorization == null || authorization.getAccessToken() == null) {
            httpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
            return R.failed(OAuth2ErrorCodesExpand.INVALID_BEARER_TOKEN);
        }

        // 获取令牌
        return R.ok(Objects.requireNonNull(authorization).getAccessToken().getToken());
    }

    /**
     * 移除指定令牌
     *
     * @param token 需要移除的令牌
     * @return 操作结果，成功返回true
     */
    @Inner
    @DeleteMapping("/token/remove/{token}")
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
     * 分页查询token列表
     *
     * @param params 查询参数，包含分页参数(current,size)和可选用户名过滤条件(username)
     * @return 分页结果，包含token列表和总数
     */
    @Inner
    @PostMapping("/token/page")
    public R<Page<TokenVO>> tokenList(@RequestBody Map<String, Object> params) {
        // 根据username参数获取对应数据
        String username = MapUtil.getStr(params, SecurityConstants.DETAILS_USERNAME);
        String usernameKey = String.format("token::username::%s*::*::*::*", username);
        String key = "token::username::*::*::*::*";
        int current = MapUtil.getInt(params, CommonConstants.CURRENT);
        int size = MapUtil.getInt(params, CommonConstants.SIZE);

        // 根据是否有username参数选择不同的查询key
        String searchKey = StrUtil.isNotBlank(username) ? usernameKey : key;
        Set<String> keys = RedisUtils.keys(searchKey);

        // 分页处理
        List<String> pages = keys.stream().skip((current - 1) * size).limit(size).toList();
        Page<TokenVO> result = new Page(current, size);

        List<TokenVO> tokenVoList = pages.stream().map(keyName -> {
            // 从key名称解析信息: token::username::{username}::{tenantId}::{tokenId}
            String[] keyParts = keyName.split("::");
            if (keyParts.length < 6) {
                return null;
            }

            TokenVO tokenVo = new TokenVO();
            // 从key解析username
            String keyUsername = keyParts[2];
            tokenVo.setUsername(keyUsername);
            tokenVo.setClientId(keyParts[3]);
            tokenVo.setId(keyParts[5]);
            tokenVo.setAccessToken(keyParts[5]);
            // 获取TTL作为过期时间
            Long ttl = RedisUtils.getExpire(keyName);
            // TTL是秒数，转换为过期时间
            long expiresAtMillis = System.currentTimeMillis() + (ttl * 1000);
            String expiresAt = TemporalAccessorUtil.format(java.time.Instant.ofEpochMilli(expiresAtMillis),
                    DatePattern.NORM_DATETIME_PATTERN);
            tokenVo.setExpiresAt(expiresAt);
            return tokenVo;
        }).filter(Objects::nonNull).toList();

        result.setRecords(tokenVoList);
        result.setTotal(keys.size());
        return R.ok(result);
    }

    @Inner
    @GetMapping("/token/query-token")
    public R queryToken(String token) {
        OAuth2Authorization authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
		return R.ok(authorization);
	}

}
