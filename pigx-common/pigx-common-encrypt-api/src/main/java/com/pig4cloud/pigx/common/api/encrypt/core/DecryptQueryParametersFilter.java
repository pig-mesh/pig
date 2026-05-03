/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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
import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
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

    /**
     * API 加解密配置，提供启用状态、跳过路径和默认加密算法。
     */
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

        if (MapUtil.isEmpty(request.getParameterMap())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 包装请求，使 body 与 parameterMap 都可重复读取且可写
        RepeatBodyRequestWrapper requestWrapper = new RepeatBodyRequestWrapper(request);
        // 直接在可写的 wrapper parameterMap 上替换值，避免再做一次 HashMap 拷贝
        Map<String, String[]> parameterMap = requestWrapper.getParameterMap();
        EncryptType encryptType = encryptProperties.getDefaultEncryptType();
        String key = KEY_EXTRACTORS.get(encryptType).apply(encryptProperties);
        CryptoInfoBean cryptoInfoBean = new CryptoInfoBean(encryptType, key);

        // 构建默认算法对应的解密信息，对每个参数的所有值（含多值参数）逐一解密
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] values = entry.getValue();
            if (ArrayUtil.isEmpty(values)) {
                continue;
            }

            String[] decryptedValues = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                decryptedValues[i] = ApiCryptoUtil.decryptData(URLUtil.decode(values[i]), cryptoInfoBean);
            }
            entry.setValue(decryptedValues);
        }

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
