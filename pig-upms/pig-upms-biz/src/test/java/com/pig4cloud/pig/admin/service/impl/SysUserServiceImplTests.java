package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.pig4cloud.pig.admin.api.constant.UpmsErrorCodes;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.security.service.PigUser;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SysUserServiceImplTests {

	@Test
	void updateUserInfoRejectsMissingCodeWhenRedisEntryDoesNotExist() {
		SysUserServiceImpl service = spy(new SysUserServiceImpl(null, null, null, null, null, null, null, null, null));
		doReturn(true).when(service).updateById(any(SysUser.class));

		UserDTO userDto = new UserDTO();
		userDto.setPhone("13800138000");
		String cacheKey = CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StringPool.AT
				+ userDto.getPhone();

		PigUser pigUser = mock(PigUser.class);
		when(pigUser.getId()).thenReturn(1L);
		Authentication authentication = mock(Authentication.class);
		when(authentication.getPrincipal()).thenReturn(pigUser);
		SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
		securityContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(securityContext);

		GenericApplicationContext applicationContext = applicationContext(cacheKey);
		new SpringContextHolder().setApplicationContext(applicationContext);
		try {
			R<Boolean> result = service.updateUserInfo(userDto);

			assertThat(result.isOk()).isFalse();
			assertThat(result.getMsg()).isEqualTo("验证码错误");
			verify(service, never()).updateById(any(SysUser.class));
		}
		finally {
			SecurityContextHolder.clearContext();
			SpringContextHolder.clearHolder();
			applicationContext.close();
		}
	}

	@SuppressWarnings("unchecked")
	private GenericApplicationContext applicationContext(String cacheKey) {
		RedisTemplate<String, Object> redisTemplate = mock(RedisTemplate.class);
		ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);
		when(redisTemplate.opsForValue()).thenReturn(valueOperations);
		when(valueOperations.get(cacheKey)).thenReturn(null);

		MessageSource messageSource = mock(MessageSource.class);
		when(messageSource.getMessage(UpmsErrorCodes.SYS_APP_SMS_ERROR, null, LocaleContextHolder.getLocale()))
			.thenReturn("验证码错误");

		GenericApplicationContext applicationContext = new GenericApplicationContext();
		applicationContext.getBeanFactory().registerSingleton("redisTemplate", redisTemplate);
		applicationContext.getBeanFactory().registerSingleton("pigMessageSource", messageSource);
		applicationContext.refresh();
		return applicationContext;
	}

}
