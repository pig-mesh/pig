package com.pig4cloud.pigx.codegen.config;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.ssssssss.magicapi.core.context.MagicUser;
import org.ssssssss.magicapi.core.interceptor.Authorization;
import org.ssssssss.magicapi.core.interceptor.AuthorizationInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Enumeration;

/**
 * magic 设计器鉴权处理
 *
 * @author lengleng
 * @date 2022/7/10
 *
 * 自定义权限
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class CustomMagicDesignerAuthorizationInterceptor implements AuthorizationInterceptor {

	private final static String MAGIC_AUTHORITY = "gen_api_designer";

	private final ResourceServerTokenServices tokenServices;

	/**
	 * 配置是否需要登录
	 */
	@Override
	public boolean requireLogin() {
		return true;
	}

	/**
	 * 是否拥有页面按钮的权限
	 */
	@Override
	public boolean allowVisit(MagicUser magicUser, HttpServletRequest request, Authorization authorization) {
		String accessToken = extractHeaderToken(request);
		if (StrUtil.isBlank(accessToken)) {
			 throw new AccessDeniedException("权限不足");
		}

		OAuth2Authentication oAuth2Authentication = tokenServices.loadAuthentication(accessToken);
		Collection<GrantedAuthority> authorities = oAuth2Authentication.getAuthorities();
		// 查询权限
		return authorities.stream().anyMatch(authority -> MAGIC_AUTHORITY.equals(authority.getAuthority()));
	}

	private String extractHeaderToken(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaders(HttpHeaders.AUTHORIZATION);
		while (headers.hasMoreElements()) { // typically there is only one (most servers
											// enforce that)
			String value = headers.nextElement();
			if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
				String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
				// Add this here for the auth details later. Would be better to change the
				// signature of this method.
				request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE,
						value.substring(0, OAuth2AccessToken.BEARER_TYPE.length()).trim());
				int commaIndex = authHeaderValue.indexOf(',');
				if (commaIndex > 0) {
					authHeaderValue = authHeaderValue.substring(0, commaIndex);
				}
				return authHeaderValue;
			}
		}

		return null;
	}

}
