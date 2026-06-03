package com.pig4cloud.pig.common.security.component;

import cn.hutool.core.net.Ipv4Util;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 避免前端请求伪造 inner header 直接进行服务调用，但又没有按要求配置nginx 过滤
 * <p>
 * 仅针对单体架构下生效，微服务架构下网关已经做了过滤
 *
 * @author lengleng
 * @date 2025/09/26
 */
@Slf4j
@ConditionalOnProperty(value = "security.micro", havingValue = "false")
public class PigInnerHeaderRemoveFilter extends OncePerRequestFilter {

    /**
     * 执行过滤器内部处理逻辑
     *
     * @param request     HTTP请求对象
     * @param response    HTTP响应对象
     * @param filterChain 过滤器链
     * @throws ServletException 如果发生servlet相关异常
     * @throws IOException      如果发生I/O异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        // 获取客户端IP地址
        String clientIP = JakartaServletUtil.getClientIP(request);

        // 允许 127.0.0.1 请求直接通过
        if (Ipv4Util.LOCAL_IP.equals(clientIP)) {
            filterChain.doFilter(request, response);
            return;
        }

        String fromHeader = request.getHeader(SecurityConstants.FROM);
        // 非 127.0.0.1 请求，没有配置 from  header ，直接跳过
        if (StrUtil.isBlank(fromHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 非 127.0.0.1，配置了 from  header ，直接拒绝访问
        log.warn("请求 IP {}，疑似进行系统注入操作，请注意", clientIP);
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}
