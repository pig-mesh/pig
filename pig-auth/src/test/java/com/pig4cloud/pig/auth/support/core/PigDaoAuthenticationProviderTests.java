package com.pig4cloud.pig.auth.support.core;

import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.auth.support.filter.AuthSecurityConfigProperties;
import com.pig4cloud.pig.auth.support.filter.ValidateCodeFilter;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.RedisUtils;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import com.pig4cloud.pig.auth.support.sms.OAuth2ResourceOwnerSmsAuthenticationProvider;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.service.PigUserDetailsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PigDaoAuthenticationProviderTests {

	private MockedStatic<SpringUtil> spring;

	private PigDaoAuthenticationProvider provider;

	private PigUserDetailsService users;

	@BeforeEach
	void setUp() {
		spring = mockStatic(SpringUtil.class);
		spring.when(() -> SpringUtil.getBean("securityMessageSource")).thenReturn(new StaticMessageSource());
		users = mock(PigUserDetailsService.class);
		when(users.support(any(), any())).thenReturn(true);
		when(users.loadUserByUsername(any())).thenReturn(
				User.withUsername("admin").password("{noop}correct-password").authorities("ROLE_ADMIN").build());
		spring.when(() -> SpringUtil.getBeansOfType(PigUserDetailsService.class)).thenReturn(Map.of("users", users));
		provider = new PigDaoAuthenticationProvider();
	}

	@AfterEach
	void tearDown() {
		RequestContextHolder.resetRequestAttributes();
		SecurityContextHolder.clearContext();
		spring.close();
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "password", "client_credentials", "authorization_code", "refresh_token", "mobile", "app",
			"unknown" })
	void formLoginAlwaysChecksPassword(String grantType) throws Exception {
		for (String password : new String[] { null, "", "wrong-password" }) {
			assertThat(formLogin(grantType, password).getStatus()).isEqualTo(401);
			assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
		}
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "password", "client_credentials", "mobile" })
	void validFormPasswordStillWorks(String grantType) throws Exception {
		assertThat(formLogin(grantType, "correct-password").getStatus()).isEqualTo(204);
	}

	@Test
	void smsTokenStillAuthenticatesWithoutPassword() {
		bindRequest("mobile");
		var token = smsToken();
		assertThat(token.isAuthenticated()).isFalse();
		assertThat(token.getCredentials()).isNull();
		assertThat(new ProviderManager(provider).authenticate(token).isAuthenticated()).isTrue();
	}

	@Test
	void smsStillRejectsDisabledUsers() {
		bindRequest("mobile");
		when(users.loadUserByUsername(any())).thenReturn(User.withUsername("admin")
			.password("{noop}correct-password")
			.authorities("ROLE_ADMIN")
			.disabled(true)
			.build());
		assertThatThrownBy(() -> new ProviderManager(provider).authenticate(smsToken()))
			.isInstanceOf(DisabledException.class);
	}

	@ParameterizedTest
	@NullAndEmptySource
	@ValueSource(strings = { "wrong", "expired", "123456" })
	void smsRequiresValidCodeBeforeAuthenticating(String code) throws Exception {
		var request = bindRequest(SecurityConstants.MOBILE);
		request.setRequestURI(SecurityConstants.OAUTH_TOKEN_URL);
		request.setServletPath(SecurityConstants.OAUTH_TOKEN_URL);
		request.addHeader("Authorization", "Basic dGVzdDp0ZXN0");
		request.addParameter("mobile", "13800000000");
		if (code != null) {
			request.addParameter("code", code);
		}
		var properties = new AuthSecurityConfigProperties();
		properties.setIgnoreClients(List.of("test"));
		var filter = new ValidateCodeFilter(properties);
		var authenticated = new AtomicBoolean();
		var response = new MockHttpServletResponse();
		jakarta.servlet.FilterChain chain = (req, res) -> authenticated
			.set(new ProviderManager(provider).authenticate(smsToken()).isAuthenticated());
		String key = CacheConstants.DEFAULT_CODE_KEY + "13800000000";
		try (var redis = mockStatic(RedisUtils.class)) {
			redis.when(() -> RedisUtils.hasKey(key)).thenReturn(!"expired".equals(code));
			redis.when(() -> RedisUtils.get(key)).thenReturn("123456");
			if ("123456".equals(code)) {
				filter.doFilter(request, response, chain);
				assertThat(authenticated).isTrue();
			}
			else {
				assertThatThrownBy(() -> filter.doFilter(request, response, chain))
					.isInstanceOf(OAuth2AuthenticationException.class);
				assertThat(authenticated).isFalse();
				verify(users, never()).loadUserByUsername(any());
			}
		}
	}

	private UsernamePasswordAuthenticationToken smsToken() {
		var sms = new OAuth2ResourceOwnerSmsAuthenticationProvider(new ProviderManager(provider),
				mock(OAuth2AuthorizationService.class), mock(OAuth2TokenGenerator.class));
		return sms.buildToken(Map.of(SecurityConstants.SMS_PARAMETER_NAME, "13800000000"));
	}

	private MockHttpServletRequest bindRequest(String grantType) {
		var request = new MockHttpServletRequest("POST", "/oauth2/form");
		request.setServletPath("/oauth2/form");
		request.addParameter("client_id", "pig");
		if (grantType != null) {
			request.addParameter("grant_type", grantType);
		}
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
		return request;
	}

	private MockHttpServletResponse formLogin(String grantType, String password) throws Exception {
		SecurityContextHolder.clearContext();
		var request = bindRequest(grantType);
		request.addParameter("username", "admin");
		if (password != null) {
			request.addParameter("password", password);
		}
		var filter = new UsernamePasswordAuthenticationFilter(new ProviderManager(provider));
		filter.setFilterProcessesUrl("/oauth2/form");
		filter.setAuthenticationSuccessHandler((req, res, auth) -> res.setStatus(204));
		filter.setAuthenticationFailureHandler((req, res, ex) -> res.setStatus(401));
		var response = new MockHttpServletResponse();
		filter.doFilter(request, response, new MockFilterChain());
		return response;
	}

}
