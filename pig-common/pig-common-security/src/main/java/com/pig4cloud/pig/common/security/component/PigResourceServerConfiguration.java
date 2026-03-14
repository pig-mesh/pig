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

package com.pig4cloud.pig.common.security.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.method.PrePostTemplateDefaults;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.annotation.AnnotationTemplateExpressionDefaults;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author lengleng
 * @date 2022-06-04
 * <p>
 * 资源服务器认证授权配置
 */
@Slf4j
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class PigResourceServerConfiguration {

    protected final ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;

    @Lazy
    private final OpaqueTokenIntrospector customOpaqueTokenIntrospector;

    @Lazy
    private final PermitAllUrlProperties permitAllUrl;

    private final PigBootCorsProperties corsProperties;

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        PathPatternRequestMatcher[] permitMatchers = permitAllUrl.getIgnoreUrls()
                .stream()
                .map(url -> PathPatternRequestMatcher.withDefaults().matcher(url))
                .toList()
                .toArray(new PathPatternRequestMatcher[]{});

        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers(permitMatchers)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(
                        oauth2 -> oauth2.opaqueToken(token -> token.introspector(customOpaqueTokenIntrospector))
                                .authenticationEntryPoint(resourceAuthExceptionEntryPoint))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable);

        // 仅支持单体版本：根据配置启用CORS跨域支持（仅在security.cors.enabled=true时生效）
        if (Boolean.TRUE.equals(corsProperties.getEnabled())) {
            http.cors(cors -> cors.configurationSource(request -> {
                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsProperties.getAllowedOriginPatterns().forEach(corsConfiguration::addAllowedOriginPattern);
                corsProperties.getAllowedHeaders().forEach(corsConfiguration::addAllowedHeader);
                corsProperties.getAllowedMethods().forEach(corsConfiguration::addAllowedMethod);
                corsConfiguration.setAllowCredentials(corsProperties.getAllowCredentials());
                source.registerCorsConfiguration(corsProperties.getPathPattern(), corsConfiguration);
                return corsConfiguration;
            }));
        }

        return http.build();
    }

    /**
     * 创建并返回一个支持自定义权限表达式的默认模板实例
     *
     * @return {@link PrePostTemplateDefaults} 权限表达式默认模板实例
     */
    @Bean
    AnnotationTemplateExpressionDefaults prePostTemplateDefaults() {
        return new AnnotationTemplateExpressionDefaults();
    }

}
