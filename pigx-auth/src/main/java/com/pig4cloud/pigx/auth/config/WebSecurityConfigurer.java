/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pigx.auth.config;

import com.pig4cloud.pigx.common.security.component.PigxDaoAuthenticationProvider;
import com.pig4cloud.pigx.common.security.grant.CustomAppAuthenticationProvider;
import com.pig4cloud.pigx.common.security.handler.FormAuthenticationFailureHandler;
import com.pig4cloud.pigx.common.security.handler.MobileLoginSuccessHandler;
import com.pig4cloud.pigx.common.security.handler.SsoLogoutSuccessHandler;
import com.pig4cloud.pigx.common.security.handler.TenantSavedRequestAwareAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * @author lengleng
 * @date 2018/6/22 认证相关配置
 */
@Primary
@Order(90)
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

	@Override
	@SneakyThrows
	protected void configure(HttpSecurity http) {
		http.formLogin().loginPage("/token/login").loginProcessingUrl("/token/form")
				.successHandler(tenantSavedRequestAwareAuthenticationSuccessHandler())
				.failureHandler(authenticationFailureHandler()).and().logout()
				.logoutSuccessHandler(logoutSuccessHandler()).deleteCookies("JSESSIONID").invalidateHttpSession(true)
				.and().authorizeRequests().antMatchers("/token/**", "/actuator/**", "/mobile/**").permitAll()
				.anyRequest().authenticated().and().csrf().disable();
	}

	/**
	 * 自定义 provider 列表注入
	 * @param auth AuthenticationManagerBuilder
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		PigxDaoAuthenticationProvider daoAuthenticationProvider = new PigxDaoAuthenticationProvider();
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		// 处理默认的密码模式认证
		auth.authenticationProvider(daoAuthenticationProvider);
		// 自定义的认证模式
		auth.authenticationProvider(new CustomAppAuthenticationProvider());
	}

	/**
	 * 不拦截静态资源
	 * @param web
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/favicon.ico", "/css/**", "/error");
	}

	@Bean
	@Override
	@SneakyThrows
	public AuthenticationManager authenticationManagerBean() {
		return super.authenticationManagerBean();
	}

	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler() {
		return new FormAuthenticationFailureHandler();
	}

	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new SsoLogoutSuccessHandler();
	}

	@Bean
	public AuthenticationSuccessHandler mobileLoginSuccessHandler() {
		return new MobileLoginSuccessHandler();
	}

	/**
	 * 具备租户传递能力
	 * @return AuthenticationSuccessHandler
	 */
	@Bean
	public AuthenticationSuccessHandler tenantSavedRequestAwareAuthenticationSuccessHandler() {
		return new TenantSavedRequestAwareAuthenticationSuccessHandler();
	}

	/**
	 * 密码处理器
	 * @return 动态密码处理器 {类型}密文
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
