/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pigx.common.security.component;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.oauth2.server.resource.BearerTokenErrors;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caiqy
 * @date 2020.05.15
 */
public class PigxBearerTokenExtractor implements BearerTokenResolver {

    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
            Pattern.CASE_INSENSITIVE);


    private String bearerTokenHeaderName = HttpHeaders.AUTHORIZATION;

    private final PathMatcher pathMatcher = new AntPathMatcher();

    private final PermitAllUrlProperties urlProperties;

    public PigxBearerTokenExtractor(PermitAllUrlProperties urlProperties) {
        this.urlProperties = urlProperties;
    }

    /**
     * 解析令牌
     *
     * @param request 请求
     * @return {@link String }
     */
    @Override
    public String resolve(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        String relativePath = requestUri.substring(request.getContextPath().length());

        if (urlProperties.isSkipPublicUrl()) {
            boolean match = urlProperties.getIgnoreUrls().stream().anyMatch(url -> pathMatcher.match(url, relativePath));

            if (match) {
                return null;
            }
        }


        final String authorizationHeaderToken = resolveFromAuthorizationHeader(request);

        if (authorizationHeaderToken != null) {
            return authorizationHeaderToken;
        }

        return resolveFromRequestParameters(request);
    }

    /**
     * 从授权标头解析
     *
     * @param request 请求
     * @return {@link String }
     */
    private String resolveFromAuthorizationHeader(HttpServletRequest request) {
        String authorization = request.getHeader(this.bearerTokenHeaderName);
        if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            return null;
        }
        Matcher matcher = authorizationPattern.matcher(authorization);
        if (!matcher.matches()) {
            BearerTokenError error = BearerTokenErrors.invalidToken("Bearer token is malformed");
            throw new OAuth2AuthenticationException(error);
        }
        return matcher.group("token");
    }

    /**
     * 从请求参数解析
     *
     * @param request 请求
     * @return {@link String }
     */
    private static String resolveFromRequestParameters(HttpServletRequest request) {
        String[] values = request.getParameterValues("access_token");
        if (values == null || values.length == 0) {
            return null;
        }
        if (values.length == 1) {
            return values[0];
        }
        BearerTokenError error = BearerTokenErrors.invalidRequest("Found multiple bearer tokens in the request");
        throw new OAuth2AuthenticationException(error);
    }

}
