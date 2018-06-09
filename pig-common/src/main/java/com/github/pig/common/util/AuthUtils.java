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

package com.github.pig.common.util;

import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.exception.CheckedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.codec.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author lengleng
 * @date 2018/5/13
 * 认证授权相关工具类
 */
@Slf4j
public class AuthUtils {
    private static final String BASIC_ = "Basic ";

    /**
     * 从header 请求中的clientId/clientsecect
     *
     * @param header header中的参数
     * @throws CheckedException if the Basic header is not present or is not valid
     *                          Base64
     */
    public static String[] extractAndDecodeHeader(String header)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new CheckedException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, CommonConstant.UTF8);

        int delim = token.indexOf(":");

        if (delim == -1) {
            throw new CheckedException("Invalid basic authentication token");
        }
        return new String[]{token.substring(0, delim), token.substring(delim + 1)};
    }

    /**
     * *从header 请求中的clientId/clientsecect
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static String[] extractAndDecodeHeader(HttpServletRequest request)
            throws IOException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(BASIC_)) {
            throw new CheckedException("请求头中client信息为空");
        }

        return extractAndDecodeHeader(header);
    }
}
