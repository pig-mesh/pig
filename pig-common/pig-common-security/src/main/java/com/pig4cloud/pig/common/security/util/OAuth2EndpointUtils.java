package com.pig4cloud.pig.common.security.util;

import cn.hutool.core.map.MapUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.endpoint.PkceParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * OAuth2 端点工具类
 *
 * <p>
 * 提供OAuth2相关端点操作的实用方法
 * </p>
 *
 * @author lengleng
 * @author jumuning
 * @date 2025/05/31
 */
@UtilityClass
public class OAuth2EndpointUtils {

	public final String ACCESS_TOKEN_REQUEST_ERROR_URI = "https://datatracker.ietf.org/doc/html/rfc6749#section-5.2";

	/**
	 * 从HttpServletRequest中获取参数并转换为MultiValueMap
	 * @param request HttpServletRequest对象
	 * @return 包含所有参数的MultiValueMap，key为参数名，value为参数值列表
	 */
	public MultiValueMap<String, String> getParameters(HttpServletRequest request) {
		Map<String, String[]> parameterMap = request.getParameterMap();
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>(parameterMap.size());
		parameterMap.forEach((key, values) -> {
			for (String value : values) {
				parameters.add(key, value);
			}
		});
		return parameters;
	}

	/**
	 * 检查请求是否符合PKCE令牌请求规范
	 * @param request HTTP请求对象
	 * @return 如果请求是授权码模式且包含code和code_verifier参数则返回true，否则返回false
	 */
	public boolean matchesPkceTokenRequest(HttpServletRequest request) {
		return AuthorizationGrantType.AUTHORIZATION_CODE.getValue()
			.equals(request.getParameter(OAuth2ParameterNames.GRANT_TYPE))
				&& request.getParameter(OAuth2ParameterNames.CODE) != null
				&& request.getParameter(PkceParameterNames.CODE_VERIFIER) != null;
	}

	/**
	 * 抛出OAuth2认证异常
	 * @param errorCode 错误码
	 * @param parameterName 参数名称
	 * @param errorUri 错误URI
	 * @throws OAuth2AuthenticationException OAuth2认证异常
	 */
	public void throwError(String errorCode, String parameterName, String errorUri) {
		OAuth2Error error = new OAuth2Error(errorCode, "OAuth 2.0 Parameter: " + parameterName, errorUri);
		throw new OAuth2AuthenticationException(error);
	}

	/**
	 * 发送OAuth2访问令牌响应
	 * @param authentication 用户认证信息
	 * @param claims 扩展信息
	 * @return OAuth2访问令牌响应
	 */
	public OAuth2AccessTokenResponse sendAccessTokenResponse(OAuth2Authorization authentication,
			Map<String, Object> claims) {

		OAuth2AccessToken accessToken = authentication.getAccessToken().getToken();
		OAuth2RefreshToken refreshToken = authentication.getRefreshToken().getToken();

		OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
			.tokenType(accessToken.getTokenType())
			.scopes(accessToken.getScopes());
		if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
			builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
		}
		if (refreshToken != null) {
			builder.refreshToken(refreshToken.getTokenValue());
		}

		if (MapUtil.isNotEmpty(claims)) {
			builder.additionalParameters(claims);
		}
		return builder.build();
	}

}
