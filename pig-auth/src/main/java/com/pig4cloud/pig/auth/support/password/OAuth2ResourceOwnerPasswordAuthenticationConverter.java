package com.pig4cloud.pig.auth.support.password;

import com.pig4cloud.pig.auth.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.util.OAuth2EndpointUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;

/**
 * @author jumuning
 * @date 2022-06-02
 *
 * 密码认证转换器
 */
public class OAuth2ResourceOwnerPasswordAuthenticationConverter
		extends OAuth2ResourceOwnerBaseAuthenticationConverter<OAuth2ResourceOwnerPasswordAuthenticationToken> {

	/**
	 * 支持密码模式
	 * @param grantType 授权类型
	 */
	@Override
	public boolean support(String grantType) {
		return SecurityConstants.PASSWORD.equals(grantType);
	}

	@Override
	public OAuth2ResourceOwnerPasswordAuthenticationToken buildToken(Authentication clientPrincipal,
			Set requestedScopes, Map additionalParameters) {
		return new OAuth2ResourceOwnerPasswordAuthenticationToken(
				new AuthorizationGrantType(SecurityConstants.PASSWORD), clientPrincipal, requestedScopes,
				additionalParameters);
	}

	/**
	 * 校验扩展参数 密码模式密码必须不为空
	 * @param request 参数列表
	 */
	@Override
	public void checkParams(HttpServletRequest request) {
		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
		// username (REQUIRED)
		String username = parameters.getFirst(SecurityConstants.DETAILS_USERNAME);
		if (!StringUtils.hasText(username) || parameters.get(SecurityConstants.DETAILS_USERNAME).size() != 1) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.DETAILS_USERNAME,
					OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// password (REQUIRED)
		String password = parameters.getFirst(SecurityConstants.PASSWORD);
		if (!StringUtils.hasText(password) || parameters.get(SecurityConstants.PASSWORD).size() != 1) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.PASSWORD,
					OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}
	}

}
