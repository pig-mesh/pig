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

package com.pig4cloud.pigx.common.data.tenant;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lengleng
 * @date 2018/9/13
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextHolderFilter extends GenericFilterBean {

	private final static String UNDEFINED_STR = "undefined";

	@Override
	@SneakyThrows
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String headerTenantId = request.getHeader(CommonConstants.TENANT_ID);
		String paramTenantId = request.getParameter(CommonConstants.TENANT_ID);

		log.debug("获取header中的租户ID为:{}", headerTenantId);

		if (StrUtil.isNotBlank(headerTenantId) && !StrUtil.equals(UNDEFINED_STR, headerTenantId)) {
			TenantContextHolder.setTenantId(Long.parseLong(headerTenantId));
		}
		else if (StrUtil.isNotBlank(paramTenantId) && !StrUtil.equals(UNDEFINED_STR, paramTenantId)) {
			TenantContextHolder.setTenantId(Long.parseLong(paramTenantId));
		}
		else {
			TenantContextHolder.setTenantId(CommonConstants.TENANT_ID_1);
		}

		filterChain.doFilter(request, response);
		TenantContextHolder.clear();
	}

}
