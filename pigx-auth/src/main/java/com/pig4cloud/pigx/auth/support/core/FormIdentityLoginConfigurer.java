package com.pig4cloud.pigx.auth.support.core;

import com.pig4cloud.pigx.auth.support.handler.FormAuthenticationFailureHandler;
import com.pig4cloud.pigx.auth.support.handler.SsoLogoutSuccessHandler;
import com.pig4cloud.pigx.auth.support.handler.TenantSavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

/**
 * @author lengleng
 * @data 2022-06-04
 *
 * 基于授权码模式 统一认证登录 spring security & sas 都可以使用 所以抽取成 HttpConfigurer
 */
public final class FormIdentityLoginConfigurer
		extends AbstractHttpConfigurer<FormIdentityLoginConfigurer, HttpSecurity> {

	@Override
	public void init(HttpSecurity http) throws Exception {
		http.formLogin(formLogin -> {
			formLogin.loginPage("/token/login");
			formLogin.loginProcessingUrl("/token/form");
			formLogin.failureHandler(new FormAuthenticationFailureHandler());
			formLogin.successHandler(new TenantSavedRequestAwareAuthenticationSuccessHandler());

		})
			.logout() // SSO登出成功处理
			.logoutSuccessHandler(new SsoLogoutSuccessHandler())
			.deleteCookies("JSESSIONID")
			.invalidateHttpSession(true)
			.and()
			.csrf()
			.disable();
	}

}
