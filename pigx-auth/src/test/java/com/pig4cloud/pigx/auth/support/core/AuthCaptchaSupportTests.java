package com.pig4cloud.pigx.auth.support.core;

import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import static org.assertj.core.api.Assertions.assertThat;

class AuthCaptchaSupportTests {

	private final HttpSessionRequestCache requestCache = new HttpSessionRequestCache();

	private final AuthCaptchaSupport authCaptchaSupport = new AuthCaptchaSupport();

	@Test
	void shouldResolveClientAndTenantFromSavedRequest() {
		MockHttpServletRequest authorizationRequest = new MockHttpServletRequest("GET", "/oauth2/authorize");
		authorizationRequest.addParameter(OAuth2ParameterNames.CLIENT_ID, "demo-client");
		authorizationRequest.addParameter(CommonConstants.TENANT_ID, "2");
		MockHttpServletResponse authorizationResponse = new MockHttpServletResponse();
		requestCache.saveRequest(authorizationRequest, authorizationResponse);

		MockHttpServletRequest loginRequest = new MockHttpServletRequest("GET", "/token/login");
		loginRequest.setSession(authorizationRequest.getSession(false));
		MockHttpServletResponse loginResponse = new MockHttpServletResponse();

		assertThat(authCaptchaSupport.resolveAuthorizationClientId(loginRequest, loginResponse, true))
			.isEqualTo("demo-client");
		assertThat(authCaptchaSupport.resolveAuthorizationTenantId(loginRequest, loginResponse, true)).isEqualTo("2");
	}

	@Test
	void shouldPreferCurrentRequestParametersOverSavedRequest() {
		MockHttpServletRequest authorizationRequest = new MockHttpServletRequest("GET", "/oauth2/authorize");
		authorizationRequest.addParameter(OAuth2ParameterNames.CLIENT_ID, "saved-client");
		authorizationRequest.addParameter(CommonConstants.TENANT_ID, "2");
		MockHttpServletResponse authorizationResponse = new MockHttpServletResponse();
		requestCache.saveRequest(authorizationRequest, authorizationResponse);

		MockHttpServletRequest formRequest = new MockHttpServletRequest("POST", "/oauth2/form");
		formRequest.setSession(authorizationRequest.getSession(false));
		formRequest.addParameter(OAuth2ParameterNames.CLIENT_ID, "current-client");
		formRequest.addParameter(CommonConstants.TENANT_ID, "9");
		MockHttpServletResponse formResponse = new MockHttpServletResponse();

		assertThat(authCaptchaSupport.resolveAuthorizationClientId(formRequest, formResponse, true))
			.isEqualTo("current-client");
		assertThat(authCaptchaSupport.resolveAuthorizationTenantId(formRequest, formResponse, true)).isEqualTo("9");
	}

}
