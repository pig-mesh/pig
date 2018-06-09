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

package com.github.pig.common.bean.xss;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/5/26
 * https://gitee.com/renrenio/renren-fast
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 没被包装过的HttpServletRequest（特殊场景，需要自己过滤）
     */
    HttpServletRequest orgRequest;
    /**
     * html过滤
     */
    private final static HtmlFilter HTML_FILTER = new HtmlFilter();

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        //非json类型，直接返回
        if (!MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(super.getHeader(HttpHeaders.CONTENT_TYPE))) {
            return super.getInputStream();
        }

        //为空，直接返回
        String json = IOUtils.toString(super.getInputStream(), "utf-8");
        if (StringUtils.isBlank(json)) {
            return super.getInputStream();
        }

        //xss过滤
        json = xssEncode(json);
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes("utf-8"));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return bis.read();
            }
        };
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = xssEncode(parameters[i]);
        }
        return parameters;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new LinkedHashMap<>();
        Map<String, String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = xssEncode(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    private String xssEncode(String input) {
        return HTML_FILTER.filter(input);
    }

    /**
     * 获取最原始的request
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
        if (request instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) request).getOrgRequest();
        }

        return request;
    }

}
