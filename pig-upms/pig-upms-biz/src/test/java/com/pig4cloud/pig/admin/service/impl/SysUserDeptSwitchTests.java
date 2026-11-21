package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.pig4cloud.pig.admin.api.entity.SysDept;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import java.util.Locale;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.security.service.PigUser;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SysUserDeptSwitchTests {
	private SysUserServiceImpl service;
	private Cache cache;
	private Object previousContext;
	private Locale previousLocale;
	private StaticApplicationContext context;

	@BeforeEach
	void setUp() {
		previousLocale = LocaleContextHolder.getLocale();
		previousContext = ReflectionTestUtils.getField(SpringContextHolder.class, "applicationContext");
		context = new StaticApplicationContext();
		var messages = new ResourceBundleMessageSource();
		messages.setBasename("i18n/messages");
		context.getBeanFactory().registerSingleton("pigMessageSource", messages);
		var securityMessages = new ResourceBundleMessageSource();
		securityMessages.setBasename("errors/messages");
		context.getBeanFactory().registerSingleton("securityMessageSource", securityMessages);
		new SpringContextHolder().setApplicationContext(context);
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), "user"), SysUser.class);
		service = mock(SysUserServiceImpl.class, CALLS_REAL_METHODS);
		var cacheManager = mock(CacheManager.class);
		cache = mock(Cache.class);
		ReflectionTestUtils.setField(service, "cacheManager", cacheManager);
		when(cacheManager.getCache(CacheConstants.USER_DETAILS)).thenReturn(cache);
		var user = mock(PigUser.class);
		when(user.getId()).thenReturn(1001L);
		when(user.getUsername()).thenReturn("dept-user");
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));
		var dept = new SysDept();
		dept.setDeptId(10L);
		doReturn(List.of(dept)).when(service).getUserDeptList();
		doReturn(true).when(service).update(any(Wrapper.class));
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
		LocaleContextHolder.setLocale(previousLocale);
		ReflectionTestUtils.setField(SpringContextHolder.class, "applicationContext", previousContext);
		context.close();
	}

	@Test
	void switchesToAssociatedDepartmentAndEvictsUserCache() {
		assertThat(service.updateUserDept(10L).getData()).isTrue();
		var condition = ArgumentCaptor.forClass(Wrapper.class);
		verify(service).update(condition.capture());
		var update = (LambdaUpdateWrapper<?>) condition.getValue();
		assertThat(update.getSqlSegment()).contains("user_id");
		assertThat(update.getSqlSet()).contains("dept_id");
		assertThat(update.getParamNameValuePairs().values()).containsExactlyInAnyOrder(10L, 1001L);
		verify(cache).evict("dept-user");
	}

	@Test
	void rejectsUnassociatedOrMissingDepartmentWithoutChangingState() {
		for (Locale locale : List.of(Locale.CHINA, Locale.ENGLISH)) {
			LocaleContextHolder.setLocale(locale);
			for (Long deptId : new Long[] { 20L, null }) {
				var result = service.updateUserDept(deptId);
				assertThat(result.getCode()).isEqualTo(CommonConstants.FAIL);
				assertThat(result.getMsg()).isEqualTo(Locale.CHINA.equals(locale) ? "权限不足,不允许访问"
						: "Access is denied");
			}
		}
		verify(service, never()).update(any(Wrapper.class));
		verifyNoInteractions(cache);
	}
}
