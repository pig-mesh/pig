package com.pig4cloud.pigx.report.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.admin.api.feign.RemoteTokenService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.RetOps;
import io.springboot.plugin.goview.common.domain.AjaxResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * 安全认证
 *
 * @author lengleng
 * @date 2023/4/6
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SecurityRequestFilter extends OncePerRequestFilter {

    private final RemoteTokenService remoteTokenService;

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (StrUtil.equalsAny(requestURI, "/", "/project/getData") || StrUtil.contains(requestURI,"/static")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader("token");
        String tenantId = request.getHeader(CommonConstants.TENANT_ID);

        if (StrUtil.isBlank(accessToken) || StrUtil.isBlank(tenantId)) {
            sendErrorMsg(request, response);
            return;
        }

        Optional<String> principalName = RetOps.of(remoteTokenService.queryToken(accessToken, tenantId, SecurityConstants.FROM_IN)).getDataIf(RetOps.CODE_SUCCESS).map(o -> (String) o.get("principalName"));

        // 用户信息查不到
        if (!principalName.isPresent()) {
            sendErrorMsg(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void sendErrorMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AjaxResult error = AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "require login");
        response.setContentType(ContentType.JSON.getValue());
        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
