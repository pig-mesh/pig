package com.pig4cloud.pig.auth.support.password;

import static com.pig4cloud.pig.common.core.constant.SecurityConstants.PASSWORD;

import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.pig4cloud.pig.auth.support.base.OAuth2ResourceOwnerBaseAuthenticationConverter;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.security.util.OAuth2EndpointUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * OAuth2 资源所有者密码认证转换器
 *
 * @author lengleng
 * @date 2025/05/30
 */
public class OAuth2ResourceOwnerPasswordAuthenticationConverter
		extends OAuth2ResourceOwnerBaseAuthenticationConverter<OAuth2ResourceOwnerPasswordAuthenticationToken> {

	/**
	 * 支持密码模式
	 * @param grantType 授权类型
	 */
	@Override
	public boolean support(String grantType) {
		return PASSWORD.equals(grantType);
	}

	/**
	 * 构建OAuth2资源所有者密码认证令牌
	 * @param clientPrincipal 客户端主体认证信息
	 * @param requestedScopes 请求的作用域集合
	 * @param additionalParameters 附加参数映射
	 * @return 构建完成的OAuth2资源所有者密码认证令牌
	 */
	@Override
	public OAuth2ResourceOwnerPasswordAuthenticationToken buildToken(Authentication clientPrincipal,
			Set requestedScopes, Map additionalParameters) {
		return new OAuth2ResourceOwnerPasswordAuthenticationToken(new AuthorizationGrantType(PASSWORD), clientPrincipal,
				requestedScopes, additionalParameters);
	}

	/**
	 * 校验扩展参数 密码模式密码必须不为空
	 * @param request 参数列表
	 */
	@Override
	public void checkParams(HttpServletRequest request) {
		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);
		// username (REQUIRED)
		String username = parameters.getFirst(CommonConstants.USERNAME);
		if (!StringUtils.hasText(username) || parameters.get(CommonConstants.USERNAME).size() != 1) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, CommonConstants.USERNAME,
					OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		// password (REQUIRED)
		String password = parameters.getFirst(CommonConstants.PASSWORD);
		if (!StringUtils.hasText(password) || parameters.get(CommonConstants.PASSWORD).size() != 1) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, CommonConstants.PASSWORD,
					OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}
	}

}
