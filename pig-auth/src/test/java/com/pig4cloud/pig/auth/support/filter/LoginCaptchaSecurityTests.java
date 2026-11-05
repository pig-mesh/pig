package com.pig4cloud.pig.auth.support.filter;

import com.pig4cloud.pig.auth.endpoint.ImageCodeEndpoint;
import com.pig4cloud.pig.auth.support.core.AuthCaptchaSupport;
import com.pig4cloud.pig.auth.support.core.OauthClientDetailsLoader;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.security.captcha.CaptchaValidator;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginCaptchaSecurityTests {

	private final Map<String, Object> cache = new HashMap<>();

	private final RedisTemplate<String, Object> redis = mock(RedisTemplate.class);

	private final ValueOperations<String, Object> values = mock(ValueOperations.class);

	private final OauthClientDetailsLoader loader = mock(OauthClientDetailsLoader.class);

	private final ValidateCodeFilter filter = new ValidateCodeFilter(
			new AuthCaptchaSupport(new CaptchaValidator(null, redis), loader));

	private final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/oauth2/token");

	private final MockHttpServletResponse response = new MockHttpServletResponse();

	private final FilterChain chain = mock(FilterChain.class);

	@BeforeEach
	void setup() {
		when(redis.opsForValue()).thenReturn(values);
		when(values.get(anyString())).thenAnswer(invocation -> cache.get(invocation.getArgument(0)));
		when(values.getAndDelete(anyString())).thenAnswer(invocation -> cache.remove(invocation.getArgument(0)));
		doAnswer(invocation -> {
			cache.put(invocation.getArgument(0), invocation.getArgument(1));
			return null;
		}).when(values).set(anyString(), any(), anyLong(), any(TimeUnit.class));
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		factory.registerSingleton("redisTemplate", redis);
		StaticMessageSource messages = new StaticMessageSource();
		messages.setUseCodeAsDefaultMessage(true);
		factory.registerSingleton("pigMessageSource", messages);
		new SpringContextHolder().postProcessBeanFactory(factory);
		request.setServletPath("/oauth2/token");
		request.addHeader("TENANT-ID", "1");
	}

	@AfterEach
	void cleanup() {
		SecurityContextHolder.clearContext();
		new SpringContextHolder().postProcessBeanFactory(null);
		SpringContextHolder.clearHolder();
	}

	static Stream<Arguments> validCases() {
		return Stream.of(Arguments.of("SMS", false, false), Arguments.of("SMS", true, false),
				Arguments.of("SMS", false, true), Arguments.of("SMS", true, true), Arguments.of("APP-SMS", true, true),
				Arguments.of("image", false, true), Arguments.of("image", true, true));
	}

	@ParameterizedTest
	@MethodSource("validCases")
	void acceptsOnlyTheExpectedCaptchaAndConsumesIt(String type, boolean post, boolean enabled) throws Exception {
		configureClient(post, enabled);
		String key;
		if (type.equals("image")) {
			request.setParameter("grant_type", "password");
			request.setParameter("randomStr", "image-test");
			new ImageCodeEndpoint().image("image-test", response);
			key = "IMAGE_CODE_KEY:" + "image-test";
		}
		else {
			request.setParameter("grant_type", "mobile");
			request.setParameter("mobile", type + "@13800138000");
			key = "SMS_CODE_KEY:" + type + "@13800138000";
			cache.put(key, "654321");
		}
		request.setParameter("code", cache.get(key).toString());
		filter.doFilter(request, response, chain);
		verify(chain).doFilter(request, response);
		assertThat(cache).doesNotContainKey(key);
	}

	static Stream<Arguments> invalidCases() {
		Stream<Arguments> smsCases = Stream.of(false, true)
			.flatMap(post -> Stream.of(false, true)
				.flatMap(enabled -> Stream.of("missing", "image", "legacy")
					.map(kind -> Arguments.of(post, enabled, kind))));
		return Stream.concat(smsCases,
				Stream.of(Arguments.of(false, true, "password"), Arguments.of(true, true, "password")));
	}

	@ParameterizedTest
	@MethodSource("invalidCases")
	void rejectsMissingOrSubstitutedIdentityProof(boolean post, boolean enabled, String kind) throws Exception {
		configureClient(post, enabled);
		String mobile = "SMS@13800138000";
		request.setParameter("grant_type", kind.equals("password") ? "password" : "mobile");
		request.setParameter("mobile", mobile);
		if (kind.equals("image")) {
			new ImageCodeEndpoint().image(mobile, response);
			assertThat(cache).doesNotContainKey("SMS_CODE_KEY:" + mobile);
			request.setParameter("code", cache.get("IMAGE_CODE_KEY:" + mobile).toString());
		}
		else if (kind.equals("legacy")) {
			cache.put(CacheConstants.DEFAULT_CODE_KEY + mobile, "123456");
			request.setParameter("code", "123456");
		}
		assertThatThrownBy(() -> filter.doFilter(request, response, chain))
			.isInstanceOf(OAuth2AuthenticationException.class);
		verifyNoInteractions(chain);
	}

	private void configureClient(boolean post, boolean enabled) {
		RegisteredClient client = RegisteredClient.withId("test-client")
			.clientId("test-client")
			.authorizationGrantType(new AuthorizationGrantType("password"))
			.build();
		SecurityContextHolder.getContext()
			.setAuthentication(
					new OAuth2ClientAuthenticationToken(client, post ? ClientAuthenticationMethod.CLIENT_SECRET_POST
							: ClientAuthenticationMethod.CLIENT_SECRET_BASIC, "test-secret"));
		// 策略只取已认证客户端，不使用请求额外指定的客户端。
		request.setParameter("client_id", "untrusted-client");
		SysOauthClientDetails details = new SysOauthClientDetails();
		details.setAdditionalInformation("{\"captcha_flag\":\"" + (enabled ? "1" : "0") + "\"}");
		when(loader.getByClientId("test-client")).thenReturn(details);
	}

}
