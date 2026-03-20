package com.pig4cloud.pig.auth.support.filter;

import com.pig4cloud.pig.auth.support.core.AuthCaptchaSupport;
import com.pig4cloud.pig.auth.support.handler.FormAuthenticationFailureHandler;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 授权码登录表单验证码过滤器
 *
 * <p>
 * 仅处理 /oauth2/form 请求，复用现有图形验证码能力，对授权码登录页补充验证码校验。
 * </p>
 *
 * @author pig
 * @date 2026-03-20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FormLoginValidateCodeFilter extends OncePerRequestFilter {

    private static final String FORM_LOGIN_URL = "/oauth2/form";

    private final AuthCaptchaSupport authCaptchaSupport;

    private final FormAuthenticationFailureHandler failureHandler = new FormAuthenticationFailureHandler();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!FORM_LOGIN_URL.equals(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authClientId = authCaptchaSupport.resolveAuthorizationClientId(request, response, true);
        String tenantId = authCaptchaSupport.resolveAuthorizationTenantId(request, response, true);

        if (!authCaptchaSupport.isCaptchaEnabled(tenantId, authClientId)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            authCaptchaSupport.validateCode(request);
            filterChain.doFilter(request, response);
        } catch (ValidateCodeException e) {
            log.debug("授权码登录验证码校验失败: {}", e.getMessage());
            failureHandler.onAuthenticationFailure(request, response, new AuthenticationServiceException(e.getMessage()));
        }
    }

}
