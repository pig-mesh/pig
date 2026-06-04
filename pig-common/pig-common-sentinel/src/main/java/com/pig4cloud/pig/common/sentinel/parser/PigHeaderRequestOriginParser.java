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

package com.pig4cloud.pig.common.sentinel.parser;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author lengleng
 * @date 2019-10-11
 * <p>
 * sentinel 请求来源解析，基于客户端 IP 地址
 */
public class PigHeaderRequestOriginParser implements RequestOriginParser {

	/**
	 * Parse the origin from given HTTP request.
	 * @param request HTTP request
	 * @return parsed origin (client IP address)
	 */
	@Override
	public String parseOrigin(HttpServletRequest request) {
		return JakartaServletUtil.getClientIP(request);
	}

}
