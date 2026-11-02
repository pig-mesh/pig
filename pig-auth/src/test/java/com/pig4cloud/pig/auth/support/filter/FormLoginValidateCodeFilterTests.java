package com.pig4cloud.pig.auth.support.filter;

import com.pig4cloud.pig.auth.support.core.AuthCaptchaSupport;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FormLoginValidateCodeFilterTests {

	private final AuthCaptchaSupport captchaSupport = mock(AuthCaptchaSupport.class);

	private final FormLoginValidateCodeFilter filter = new FormLoginValidateCodeFilter(captchaSupport);

	private final FilterChain chain = mock(FilterChain.class);

	private final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/oauth2/form");

	private final MockHttpServletResponse response = new MockHttpServletResponse();

	@AfterEach
	void cleanup() {
		RequestContextHolder.resetRequestAttributes();
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "mobile", "client_credentials", "authorization_code", "refresh_token", "app", "implicit" })
	void rejectsNonPasswordGrantsBeforeCaptchaOrAuthentication(String grantType) throws Exception {
		if (grantType != null) {
			request.setParameter("grant_type", grantType);
		}
		request.setParameter("username", "SMS@13800138000");
		applyFilter();
		assertThat(response.getStatus()).isEqualTo(302);
		assertThat(response.getRedirectedUrl()).contains("/token/login?error=");
		verifyNoInteractions(captchaSupport, chain);
	}

	@Test
	void rejectsDuplicateGrantParameters() throws Exception {
		request.setParameter("grant_type", "password", "mobile");
		applyFilter();
		assertThat(response.getStatus()).isEqualTo(302);
		verifyNoInteractions(captchaSupport, chain);
	}

	@Test
	void allowsPasswordWhenCaptchaDisabled() throws Exception {
		request.setParameter("grant_type", "password");
		applyFilter();
		verify(chain).doFilter(request, response);
		verify(captchaSupport, never()).validateCode(any());
	}

	@Test
	void validatesCaptchaForPasswordWhenEnabled() throws Exception {
		request.setParameter("grant_type", "password");
		when(captchaSupport.resolveAuthorizationClientId(request, response, true)).thenReturn("pig");
		when(captchaSupport.isCaptchaEnabled("pig")).thenReturn(true);
		applyFilter();
		verify(captchaSupport).validateCode(request);
		verify(chain).doFilter(request, response);
	}

	@Test
	void leavesMobileTokenRequestsUntouched() throws Exception {
		request.setServletPath("/oauth2/token");
		request.setParameter("grant_type", "mobile");
		filter.doFilter(request, response, chain);
		verify(chain).doFilter(request, response);
		verifyNoInteractions(captchaSupport);
	}

	private void applyFilter() throws Exception {
		request.setServletPath("/oauth2/form");
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
		filter.doFilter(request, response, chain);
	}

}
