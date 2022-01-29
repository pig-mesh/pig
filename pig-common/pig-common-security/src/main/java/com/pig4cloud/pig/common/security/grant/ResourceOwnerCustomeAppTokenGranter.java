package com.pig4cloud.pig.common.security.grant;

import cn.hutool.core.util.StrUtil;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 资源所有者电话令牌授予者
 *
 * @author hzq
 * @since 2021-09-14
 */
public class ResourceOwnerCustomeAppTokenGranter extends AbstractTokenGranter {

	private static final String GRANT_TYPE = "app";

	private final AuthenticationManager authenticationManager;

	public ResourceOwnerCustomeAppTokenGranter(AuthenticationManager authenticationManager,
			AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory) {
		this(authenticationManager, tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
	}

	protected ResourceOwnerCustomeAppTokenGranter(AuthenticationManager authenticationManager,
			AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService,
			OAuth2RequestFactory requestFactory, String grantType) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {

		Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());

		// 手机号
		String mobile = parameters.get("mobile");
		// 验证码/密码
		String code = parameters.get("code");

		if (StrUtil.isBlank(mobile) || StrUtil.isBlank(code)) {
			throw new InvalidGrantException("Bad credentials [ params must be has phone with code ]");
		}

		// Protect from downstream leaks of code
		parameters.remove("code");

		Authentication userAuth = new CustomAppAuthenticationToken(mobile, code, tokenRequest.getGrantType());
		((AbstractAuthenticationToken) userAuth).setDetails(parameters);
		try {
			userAuth = authenticationManager.authenticate(userAuth);
		}
		catch (AccountStatusException | BadCredentialsException ase) {
			// covers expired, locked, disabled cases (mentioned in section 5.2, draft 31)
			throw new InvalidGrantException(ase.getMessage());
		}
		// If the phone/code are wrong the spec says we should send 400/invalid grant

		if (userAuth == null || !userAuth.isAuthenticated()) {
			throw new InvalidGrantException("Could not authenticate user: " + mobile);
		}

		OAuth2Request storedOAuth2Request = getRequestFactory().createOAuth2Request(client, tokenRequest);
		return new OAuth2Authentication(storedOAuth2Request, userAuth);
	}

}
