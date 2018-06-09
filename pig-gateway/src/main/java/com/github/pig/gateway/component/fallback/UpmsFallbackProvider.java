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

package com.github.pig.gateway.component.fallback;

import com.github.pig.common.constant.ServiceNameConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author lengleng
 * @date 2018/1/25
 * UPMS 模块异常回调
 */
@Slf4j
@Component
public class UpmsFallbackProvider implements FallbackProvider {

    private static final String UPMS_SERVICE_DISABLE = "权限管理模块不可用";

    @Override
    public ClientHttpResponse fallbackResponse(Throwable cause) {
        return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.SERVICE_UNAVAILABLE;
            }

            @Override
            public int getRawStatusCode() {
                return HttpStatus.SERVICE_UNAVAILABLE.value();
            }

            @Override
            public String getStatusText() {
                return HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase();
            }

            @Override
            public void close() {
            }

            @Override
            public InputStream getBody() {
                if (cause != null && cause.getMessage() != null) {
                    log.error("调用:{} 异常：{}", getRoute(), cause.getMessage());
                    return new ByteArrayInputStream(cause.getMessage().getBytes());
                } else {
                    log.error("调用:{} 异常：{}", getRoute(), UPMS_SERVICE_DISABLE);
                    return new ByteArrayInputStream(UPMS_SERVICE_DISABLE.getBytes());
                }
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
    }

    @Override
    public String getRoute() {
        return ServiceNameConstant.UMPS_SERVICE;
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return fallbackResponse(null);
    }
}
