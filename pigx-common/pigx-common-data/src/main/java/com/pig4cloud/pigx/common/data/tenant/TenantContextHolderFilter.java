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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.entity.SysTenant;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.util.List;

/**
 * @author lengleng
 * @date 2018/9/13
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextHolderFilter extends GenericFilterBean {

    private final static String UNDEFINED_STR = "undefined";

    private final CacheManager cacheManager;

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
        } else if (StrUtil.isNotBlank(paramTenantId) && !StrUtil.equals(UNDEFINED_STR, paramTenantId)) {
            TenantContextHolder.setTenantId(Long.parseLong(paramTenantId));
        } else {
            TenantContextHolder.setTenantId(CommonConstants.TENANT_ID_1);
        }

        if (!checkTenantStatus(request, response)) return;

        filterChain.doFilter(request, response);
        TenantContextHolder.clear();
    }

    /**
     * 检查租户状态
     *
     * @param request  请求
     * @param response 响应
     * @return boolean
     */
    private boolean checkTenantStatus(HttpServletRequest request, HttpServletResponse response) {
        // 如果是获取租户列表请求跳过
        if (StrUtil.containsAnyIgnoreCase(request.getRequestURI(), "/tenant/list")) {
            return true;
        }

        // 判断租户状态
        Cache cache = cacheManager.getCache(CacheConstants.TENANT_DETAILS);
        if (cache == null) {
            return true;
        }

        List<SysTenant> tenantList = cache.get(SimpleKey.EMPTY, List.class);
        if (CollUtil.isEmpty(tenantList)) {
            return true;
        }

        boolean exist = tenantList.stream().anyMatch(tenant -> NumberUtil.equals(tenant.getId(), TenantContextHolder.getTenantId()));
        if (exist) {
            return true;
        }

        response.setStatus(HttpStatus.UPGRADE_REQUIRED.value());
        return false;
    }

}
