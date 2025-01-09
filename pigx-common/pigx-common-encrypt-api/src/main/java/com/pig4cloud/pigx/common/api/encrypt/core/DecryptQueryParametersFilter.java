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

package com.pig4cloud.pigx.common.api.encrypt.core;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.pig4cloud.pigx.common.api.encrypt.bean.CryptoInfoBean;
import com.pig4cloud.pigx.common.api.encrypt.config.ApiEncryptProperties;
import com.pig4cloud.pigx.common.api.encrypt.util.ApiCryptoUtil;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.RepeatBodyRequestWrapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

import static com.pig4cloud.pigx.common.api.encrypt.util.ApiCryptoUtil.KEY_EXTRACTORS;

/**
 * 解密GET查询参数
 *
 * @author lengleng
 * @date 2025/01/08
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(value = ApiEncryptProperties.PREFIX + ".enable", havingValue = "true")
public class DecryptQueryParametersFilter extends OncePerRequestFilter implements Ordered {

    private final ApiEncryptProperties encryptProperties;


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 是否支持解密
        if (!supportDecrypt(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Map<String, String[]> parameterMap = request.getParameterMap();
        if (MapUtil.isEmpty(parameterMap)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 将请求流转换为可多次读取的请求流
        RepeatBodyRequestWrapper requestWrapper = new RepeatBodyRequestWrapper(request);
        // 构建前端对应解密AES 因子
        parameterMap.forEach((k, v) -> {
            String[] values = parameterMap.get(k);
            if (ArrayUtil.isEmpty(values)) {
                return;
            }

            String key = KEY_EXTRACTORS.get(encryptProperties.getDefaultEncryptType()).apply(encryptProperties);
            CryptoInfoBean cryptoInfoBean = new CryptoInfoBean(encryptProperties.getDefaultEncryptType(), key);
            // 解密并覆盖原有值
            String decryptData = ApiCryptoUtil.decryptData(URLUtil.decode(values[0]), cryptoInfoBean);
            parameterMap.put(k, new String[]{decryptData});
        });

        requestWrapper.setParameterMap(parameterMap);
        filterChain.doFilter(requestWrapper, response);
    }

    /**
     * 支持 Decrypt 解密 1. 开启了加密 2. 非 feign 请求 3. GET 方法
     *
     * @param request 请求
     * @return boolean
     */
    private boolean supportDecrypt(HttpServletRequest request) {
        return encryptProperties.isEnable()
                && encryptProperties.getSkipUrl().stream().noneMatch(request.getRequestURI()::contains)
                && StrUtil.equalsAnyIgnoreCase(request.getMethod(), HttpMethod.GET.name())
                && StrUtil.isBlank(request.getHeader(SecurityConstants.FEIGN_USER_AGENT));
    }
}
