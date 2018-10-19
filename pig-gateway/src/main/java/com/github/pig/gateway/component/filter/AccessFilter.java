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

package com.github.pig.gateway.component.filter;

import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.gateway.util.RibbonVersionHolder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;

/**
 * @author lengleng
 * @date 2017/11/20
 * 在RateLimitPreFilter 之前执行，不然又空指针问题
 */
@Component
public class AccessFilter extends ZuulFilter {
    @Value("${zuul.ribbon.metadata.enabled:false}")
    private boolean canary;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FORM_BODY_WRAPPER_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        String version = requestContext.getRequest().getHeader(SecurityConstants.VERSION);
        if (canary && StrUtil.isNotBlank(version)) {
            RibbonVersionHolder.setContext(version);
        }

        requestContext.set("startTime", System.currentTimeMillis());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            requestContext.addZuulRequestHeader(SecurityConstants.USER_HEADER, authentication.getName());
            requestContext.addZuulRequestHeader(SecurityConstants.ROLE_HEADER, CollectionUtil.join(authentication.getAuthorities(), ","));
        }
        return null;
    }
}
