/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.common.security.mobile;

import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lengleng
 * @date 2018/1/9 手机号登录验证filter
 */
public class MobileAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String SPRING_SECURITY_FORM_MOBILE_KEY = "mobile";

	@Getter
	@Setter
	private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;

	@Getter
	@Setter
	private boolean postOnly = true;

	@Getter
	@Setter
	private AuthenticationEntryPoint authenticationEntryPoint;

	public MobileAuthenticationFilter() {
		super(new AntPathRequestMatcher(SecurityConstants.MOBILE_TOKEN_URL, "POST"));
	}

	@Override
	@SneakyThrows
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		if (postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}

		String mobile = obtainMobile(request);

		if (mobile == null) {
			mobile = "";
		}

		mobile = mobile.trim();

		MobileAuthenticationToken mobileAuthenticationToken = new MobileAuthenticationToken(mobile);

		setDetails(request, mobileAuthenticationToken);

		Authentication authResult = null;
		try {
			authResult = this.getAuthenticationManager().authenticate(mobileAuthenticationToken);

			logger.debug("Authentication success: " + authResult);
			SecurityContextHolder.getContext().setAuthentication(authResult);

		}
		catch (Exception failed) {
			SecurityContextHolder.clearContext();
			logger.debug("Authentication request failed: " + failed);

			try {
				authenticationEntryPoint.commence(request, response,
						new UsernameNotFoundException(failed.getMessage(), failed));
			}
			catch (Exception e) {
				logger.error("authenticationEntryPoint handle error:{}", failed);
			}
		}

		return authResult;
	}

	private String obtainMobile(HttpServletRequest request) {
		return request.getParameter(mobileParameter);
	}

	private void setDetails(HttpServletRequest request, MobileAuthenticationToken authRequest) {
		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
	}

}
