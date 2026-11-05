package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.admin.api.dto.RegisterUserDTO;
import com.pig4cloud.pig.admin.service.SysUserService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SysRegisterControllerTests {

	private void configureSwitch(String value) {
		ConcurrentMapCacheManager cache = new ConcurrentMapCacheManager(CacheConstants.PARAMS_DETAILS);
		cache.getCache(CacheConstants.PARAMS_DETAILS).put("SITE_RESET_PASSWORD", value);
		cache.getCache(CacheConstants.PARAMS_DETAILS).put("SITE_REGISTER_ENABLE", value);
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		factory.registerSingleton("cacheManager", cache);
		new SpringContextHolder().postProcessBeanFactory(factory);
	}

	@AfterEach
	void cleanup() {
		new SpringContextHolder().postProcessBeanFactory(null);
		SpringContextHolder.clearHolder();
	}

	@Test
	void closedResetSwitchRejectsBeforeCallingService() {
		SysUserService service = mock(SysUserService.class);
		configureSwitch("0");
		assertThat(new SysRegisterController(service).forgetUserPassword(new RegisterUserDTO(), "1234").isOk())
			.isFalse();
		verifyNoInteractions(service);
	}

	@Test
	void openResetSwitchDelegatesToService() {
		SysUserService service = mock(SysUserService.class);
		RegisterUserDTO dto = new RegisterUserDTO();
		when(service.forgetUserPassword(dto, "1234")).thenReturn(R.ok());
		configureSwitch("1");
		assertThat(new SysRegisterController(service).forgetUserPassword(dto, "1234").isOk()).isTrue();
		verify(service).forgetUserPassword(dto, "1234");
	}

	@Test
	void closedSwitchesRejectRegisterAndPasswordReset() {
		SysUserService service = mock(SysUserService.class);
		configureSwitch("0");
		SysRegisterController controller = new SysRegisterController(service);
		assertThat(controller.registerUser(new RegisterUserDTO()).isOk()).isFalse();
		assertThat(controller.resetUserPassword(new RegisterUserDTO()).isOk()).isFalse();
		verifyNoInteractions(service);
	}

	@Test
	void openSwitchesDelegateRegisterAndPasswordReset() {
		SysUserService service = mock(SysUserService.class);
		RegisterUserDTO dto = new RegisterUserDTO();
		when(service.registerUser(dto)).thenReturn(R.ok());
		when(service.resetUserPassword(dto)).thenReturn(R.ok());
		configureSwitch("1");
		SysRegisterController controller = new SysRegisterController(service);
		assertThat(controller.registerUser(dto).isOk()).isTrue();
		assertThat(controller.resetUserPassword(dto).isOk()).isTrue();
	}

}
