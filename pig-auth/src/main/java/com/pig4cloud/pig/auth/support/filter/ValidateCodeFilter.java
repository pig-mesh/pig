package com.pig4cloud.pig.auth.support.filter;

/**
 * 登录前处理器
 *
 * @author lengleng
 * @date 2024/4/3
 */

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.auth.support.core.AuthCaptchaSupport;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import com.pig4cloud.pig.common.core.util.WebUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author lbw
 * @date 2024-01-06
 * <p>
 * 登录前置处理器： 前端密码传输密文解密，验证码处理
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final AuthCaptchaSupport authCaptchaSupport;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestUrl = request.getServletPath();

        // 不是登录URL 请求直接跳过
        if (!SecurityConstants.OAUTH_TOKEN_URL.equals(requestUrl)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 如果登录URL 但是刷新token的请求，直接向下执行
        String grantType = request.getParameter(OAuth2ParameterNames.GRANT_TYPE);
        if (StrUtil.containsAny(grantType, SecurityConstants.REFRESH_TOKEN)) {
            filterChain.doFilter(request, response);
            return;
        }

        // mobile模式, 如果请求不包含mobile 参数直接
        String mobile = request.getParameter(SecurityConstants.GRANT_MOBILE);
        if (StrUtil.equals(SecurityConstants.GRANT_MOBILE, grantType) && StrUtil.isBlank(mobile)) {
            throw new OAuth2AuthenticationException(SecurityConstants.GRANT_MOBILE);
        }

        // mobile模式, 社交登录模式不校验验证码直接跳过
        if (StrUtil.equals(SecurityConstants.GRANT_MOBILE, grantType) && !StrUtil.contains(mobile, "SMS")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 判断客户端是否跳过检验
        if (!isCheckCaptchaClient(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 校验验证码 1. 客户端开启验证码 2. 短信模式
        try {
            checkCode();
            filterChain.doFilter(request, response);
        } catch (ValidateCodeException validateCodeException) {
            throw new OAuth2AuthenticationException(validateCodeException.getMessage());
        }
    }

    /**
     * 校验验证码
     */
    private void checkCode() throws ValidateCodeException {
        authCaptchaSupport.validateCode(WebUtils.getRequest());
    }

    /**
     * 是否需要校验客户端，根据client 查询客户端配置
     *
     * @param request 请求
     * @return true 需要校验， false 不需要校验
     */
    private boolean isCheckCaptchaClient(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String clientId = WebUtils.extractClientId(header).orElse(null);
        // 获取租户拼接区分租户的key
        String tenantId = request.getHeader(CommonConstants.TENANT_ID);
        return authCaptchaSupport.isCaptchaEnabled(tenantId, clientId);
    }

}
