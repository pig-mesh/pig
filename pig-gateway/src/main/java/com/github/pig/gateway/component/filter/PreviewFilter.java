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

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.util.R;
import com.github.pig.common.util.exception.CheckedException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.io.IoUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;

/**
 * @author lengleng
 * @date 2018年05月10日
 * 演示环境控制
 */
@Slf4j
@RefreshScope
@Configuration
@ConditionalOnProperty(value = "security.validate.preview", havingValue = "true")
public class PreviewFilter extends ZuulFilter {
    private static final String TOKEN = "token";

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Integer.MIN_VALUE;
    }

    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        if (StrUtil.equalsIgnoreCase(request.getMethod(), HttpMethod.GET.name()) ||
                StrUtil.containsIgnoreCase(request.getRequestURI(), TOKEN)){
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        R<String> result = new R<>();
        result.setCode(479);
        result.setMsg("演示环境，没有权限操作");

        ctx.setResponseStatusCode(479);
        ctx.setSendZuulResponse(false);
        ctx.getResponse().setContentType("application/json;charset=UTF-8");
        ctx.setResponseBody(JSONObject.toJSONString(result));
        return null;
    }
}
