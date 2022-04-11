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

package com.pig4cloud.pigx.common.security.listener;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.common.security.handler.AuthenticationSuccessHandler;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author lengleng
 * @date 2020/03/25 认证成功事件监听器
 */
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

	@Autowired(required = false)
	private List<AuthenticationSuccessHandler> successHandlerList;

	/**
	 * Handle an application event.
	 * @param event the event to respond to
	 */
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		Authentication authentication = (Authentication) event.getSource();
		if (CollUtil.isNotEmpty(successHandlerList) && isUserAuthentication(authentication)) {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest request = requestAttributes.getRequest();
			HttpServletResponse response = requestAttributes.getResponse();
			successHandlerList.forEach(successHandler -> successHandler.handle(authentication, request, response));
		}
	}

	private boolean isUserAuthentication(Authentication authentication) {
		return authentication.getPrincipal() instanceof PigxUser
				|| CollUtil.isNotEmpty(authentication.getAuthorities());
	}

}
