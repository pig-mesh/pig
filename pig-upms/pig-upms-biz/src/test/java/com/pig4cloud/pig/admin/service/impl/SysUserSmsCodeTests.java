package com.pig4cloud.pig.admin.service.impl;

import com.pig4cloud.pig.admin.api.dto.RegisterUserDTO;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.security.captcha.CaptchaValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 短信验证码必须一次性消费，防止 4 位验证码被重复尝试爆破。
 */
class SysUserSmsCodeTests {

	private static final String KEY = "SMS_CODE_KEY:SMS@13800138000";

	private final Map<String, Object> cache = new HashMap<>();

	@AfterEach
	void cleanup() {
		RequestContextHolder.resetRequestAttributes();
		new SpringContextHolder().postProcessBeanFactory(null);
		SpringContextHolder.clearHolder();
	}

	@Test
	void wrongCodeConsumesSmsCodeForRegisterAndForgetPassword() {
		SysUserServiceImpl service = newService();
		RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(new MockHttpServletRequest()));

		cache.put(KEY, "1234");
		assertThat(service.registerUser(dto("0000")).isOk()).isFalse();
		// 猜错一次后验证码已作废，正确的码也不能再用
		assertThat(service.registerUser(dto("1234")).isOk()).isFalse();
		assertThat(cache).doesNotContainKey(KEY);

		cache.put(KEY, "1234");
		assertThat(service.forgetUserPassword(dto(null), "0000").isOk()).isFalse();
		assertThat(service.forgetUserPassword(dto(null), "1234").isOk()).isFalse();
		assertThat(cache).doesNotContainKey(KEY);
	}

	@SuppressWarnings("unchecked")
	private SysUserServiceImpl newService() {
		RedisTemplate<String, Object> redis = mock(RedisTemplate.class);
		ValueOperations<String, Object> values = mock(ValueOperations.class);
		when(redis.opsForValue()).thenReturn(values);
		when(values.getAndDelete(anyString())).thenAnswer(invocation -> cache.remove(invocation.getArgument(0)));

		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		StaticMessageSource messages = new StaticMessageSource();
		messages.setUseCodeAsDefaultMessage(true);
		factory.registerSingleton("pigMessageSource", messages);
		factory.registerSingleton("redisTemplate", redis);
		new SpringContextHolder().postProcessBeanFactory(factory);

		SysUserServiceImpl service = org.mockito.Mockito.mock(SysUserServiceImpl.class,
				org.mockito.Mockito.withSettings().defaultAnswer(org.mockito.Mockito.CALLS_REAL_METHODS));
		org.springframework.test.util.ReflectionTestUtils.setField(service, "captchaValidator",
				new CaptchaValidator(null, redis));
		return service;
	}

	private RegisterUserDTO dto(String code) {
		RegisterUserDTO dto = new RegisterUserDTO();
		dto.setUsername("attacker");
		dto.setPhone("13800138000");
		dto.setCode(code);
		return dto;
	}

}
