package com.pig4cloud.pig.auth.support.sms;

import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.util.OAuth2EndpointUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2022-05-31
 *
 * 短信登录转换器
 */
public class OAuth2ResourceOwnerSmsAuthenticationConverter implements AuthenticationConverter {

	@Override
	public Authentication convert(HttpServletRequest request) {

		// grant_type (REQUIRED)
		String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
		if (!SecurityConstants.APP.equals(grantType)) {
			return null;
		}

		MultiValueMap<String, String> parameters = OAuth2EndpointUtils.getParameters(request);

		// scope (OPTIONAL)
		String scope = parameters.getFirst(OAuth2ParameterNames.SCOPE);
		if (StringUtils.hasText(scope) && parameters.get(OAuth2ParameterNames.SCOPE).size() != 1) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ParameterNames.SCOPE,
					OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Set<String> requestedScopes = null;
		if (StringUtils.hasText(scope)) {
			requestedScopes = new HashSet<>(Arrays.asList(StringUtils.delimitedListToStringArray(scope, " ")));
		}

		// phone (REQUIRED)
		String phone = parameters.getFirst(SecurityConstants.SMS_PARAMETER_NAME);
		if (!StringUtils.hasText(phone) || parameters.get(SecurityConstants.SMS_PARAMETER_NAME).size() != 1) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, SecurityConstants.SMS_PARAMETER_NAME,
					OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Authentication clientPrincipal = SecurityContextHolder.getContext().getAuthentication();
		if (clientPrincipal == null) {
			OAuth2EndpointUtils.throwError(OAuth2ErrorCodes.INVALID_REQUEST, OAuth2ErrorCodes.INVALID_CLIENT,
					OAuth2EndpointUtils.ACCESS_TOKEN_REQUEST_ERROR_URI);
		}

		Map<String, Object> additionalParameters = parameters.entrySet().stream()
				.filter(e -> !e.getKey().equals(OAuth2ParameterNames.GRANT_TYPE)
						&& !e.getKey().equals(OAuth2ParameterNames.SCOPE))
				.collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().get(0)));

		// 注入license
		additionalParameters.put(SecurityConstants.DETAILS_LICENSE, SecurityConstants.PROJECT_LICENSE);

		// 这个扩展参数非常重要， PigDaoAuthenticationProvider 是否校验密码
		request.setAttribute(OAuth2ParameterNames.GRANT_TYPE, SecurityConstants.APP);
		return new OAuth2ResourceOwnerSmsAuthenticationToken(new AuthorizationGrantType(SecurityConstants.APP),
				clientPrincipal, requestedScopes, additionalParameters);

	}

}
