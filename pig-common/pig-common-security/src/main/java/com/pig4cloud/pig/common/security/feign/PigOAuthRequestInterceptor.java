package com.pig4cloud.pig.common.security.feign;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.pig4cloud.pig.common.security.util.NonWebTokenContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import java.util.Collection;
import java.util.Objects;

/**
 * oauth2 feign token传递
 * <p>
 * 重新 OAuth2FeignRequestInterceptor ，官方实现部分常见不适用
 *
 * @author lengleng
 * @date 2022/5/29
 */
@Slf4j
@RequiredArgsConstructor
public class PigOAuthRequestInterceptor implements RequestInterceptor {

    private final BearerTokenResolver tokenResolver;

    /**
     * Create a template with the header of provided name and extracted extract </br>
     * <p>
     * 1. 如果使用 非web 请求，header 区别 </br>
     * <p>
     * 2. 根据authentication 还原请求token
     *
     * @param template
     */
    @Override
    public void apply(RequestTemplate template) {
        Collection<String> fromHeader = template.headers().get(SecurityConstants.FROM);
        // 带from 请求直接跳过
        if (CollUtil.isNotEmpty(fromHeader) && fromHeader.contains(SecurityConstants.FROM_IN)) {
            return;
        }

        String token = null;
        HttpServletRequest request = WebUtils.getRequest();

        // 优先尝试从 Web 请求中解析 token
        if (Objects.nonNull(request)) {
            token = tokenResolver.resolve(request);
        }

        // 如果 request 中没有 token，从 NonWebTokenContextHolder 获取（适配消息队列等非 Web 场景）
        if (StrUtil.isBlank(token)) {
            token = NonWebTokenContextHolder.getToken();
        }

        if (StrUtil.isBlank(token)) {
            return;
        }

        template.header(HttpHeaders.AUTHORIZATION,
                String.format("%s %s", OAuth2AccessToken.TokenType.BEARER.getValue(), token));

    }

}
