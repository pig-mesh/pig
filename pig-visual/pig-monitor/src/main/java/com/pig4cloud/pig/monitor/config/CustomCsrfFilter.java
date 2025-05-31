package com.pig4cloud.pig.monitor.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

/**
 * 自定义CSRF过滤器，用于处理CSRF令牌相关逻辑
 *
 * @author lengleng
 * @date 2025/05/31
 */
public class CustomCsrfFilter extends OncePerRequestFilter {

	public static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";

	/**
	 * 处理CSRF令牌的过滤器内部逻辑
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param filterChain 过滤器链
	 * @throws ServletException 如果发生servlet相关异常
	 * @throws IOException 如果发生I/O异常
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

		if (csrf != null) {

			Cookie cookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
			String token = csrf.getToken();

			if (cookie == null || token != null && !token.equals(cookie.getValue())) {
				cookie = new Cookie(CSRF_COOKIE_NAME, token);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}

		filterChain.doFilter(request, response);
	}

}
