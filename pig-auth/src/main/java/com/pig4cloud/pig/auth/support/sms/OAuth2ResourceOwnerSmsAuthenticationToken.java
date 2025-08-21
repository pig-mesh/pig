package com.pig4cloud.pig.auth.support.sms;

import java.io.Serial;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import com.pig4cloud.pig.auth.support.base.OAuth2ResourceOwnerBaseAuthenticationToken;

/**
 * @author lengleng
 * @description 短信登录token信息
 */
public class OAuth2ResourceOwnerSmsAuthenticationToken extends OAuth2ResourceOwnerBaseAuthenticationToken {

	@Serial
	private static final long serialVersionUID = 1L;

	public OAuth2ResourceOwnerSmsAuthenticationToken(AuthorizationGrantType authorizationGrantType,
			Authentication clientPrincipal, Set<String> scopes, Map<String, Object> additionalParameters) {
		super(authorizationGrantType, clientPrincipal, scopes, additionalParameters);
	}

}
