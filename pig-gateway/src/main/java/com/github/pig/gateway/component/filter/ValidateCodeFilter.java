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
import com.github.pig.common.bean.config.FilterIgnorePropertiesConfig;
import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.util.AuthUtils;
import com.github.pig.common.util.R;
import com.github.pig.common.util.exception.ValidateCodeException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author lengleng
 * @date 2018/5/10
 * <p>
 * security.validate.code.enabled 默认 为false，开启需要设置为true
 */
@Slf4j
@RefreshScope
@Configuration("validateCodeFilter")
@ConditionalOnProperty(value = "security.validate.code", havingValue = "true")
public class ValidateCodeFilter extends ZuulFilter {
    private static final String EXPIRED_CAPTCHA_ERROR = "验证码已过期，请重新获取";
    private static final String GRANT_TYPE = "grant_type";

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private FilterIgnorePropertiesConfig filterIgnorePropertiesConfig;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_ERROR_FILTER_ORDER + 1;
    }

    /**
     * 是否校验验证码
     * 1. 判断验证码开关是否开启
     * 2. 判断请求是否登录请求
     * 2.1 判断是不是刷新请求(不用单独在建立刷新客户端)
     * 3. 判断终端是否支持
     *
     * @return true/false
     */
    @Override
    public boolean shouldFilter() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();

        if (!StrUtil.containsAnyIgnoreCase(request.getRequestURI(),
                SecurityConstants.OAUTH_TOKEN_URL, SecurityConstants.MOBILE_TOKEN_URL)) {
            return false;
        }

        if (SecurityConstants.REFRESH_TOKEN.equals(request.getParameter(GRANT_TYPE))) {
            return false;
        }

        try {
            String[] clientInfos = AuthUtils.extractAndDecodeHeader(request);
            if (CollUtil.containsAny(filterIgnorePropertiesConfig.getClients(), Arrays.asList(clientInfos))) {
                return false;
            }
        } catch (IOException e) {
            log.error("解析终端信息失败", e);
        }

        return true;
    }

    @Override
    public Object run() {
        try {
            checkCode(RequestContext.getCurrentContext().getRequest());
        } catch (ValidateCodeException e) {
            RequestContext ctx = RequestContext.getCurrentContext();
            R<String> result = new R<>(e);
            result.setCode(478);

            ctx.setResponseStatusCode(478);
            ctx.setSendZuulResponse(false);
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
            ctx.setResponseBody(JSONObject.toJSONString(result));
        }
        return null;
    }

    /**
     * 检查code
     *
     * @param httpServletRequest request
     * @throws ValidateCodeException 验证码校验异常
     */
    private void checkCode(HttpServletRequest httpServletRequest) throws ValidateCodeException {
        String code = httpServletRequest.getParameter("code");
        if (StrUtil.isBlank(code)) {
            throw new ValidateCodeException("请输入验证码");
        }

        String randomStr = httpServletRequest.getParameter("randomStr");
        if (StrUtil.isBlank(randomStr)) {
            randomStr = httpServletRequest.getParameter("mobile");
        }

        String key = SecurityConstants.DEFAULT_CODE_KEY + randomStr;
        if (!redisTemplate.hasKey(key)) {
            throw new ValidateCodeException(EXPIRED_CAPTCHA_ERROR);
        }

        Object codeObj = redisTemplate.opsForValue().get(key);

        if (codeObj == null) {
            throw new ValidateCodeException(EXPIRED_CAPTCHA_ERROR);
        }

        String saveCode = codeObj.toString();
        if (StrUtil.isBlank(saveCode)) {
            redisTemplate.delete(key);
            throw new ValidateCodeException(EXPIRED_CAPTCHA_ERROR);
        }

        if (!StrUtil.equals(saveCode, code)) {
            redisTemplate.delete(key);
            throw new ValidateCodeException("验证码错误，请重新输入");
        }

        redisTemplate.delete(key);
    }
}
