package com.pig4cloud.pigx.common.security.handler;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lengleng
 * @date 2020/10/6
 * <p>
 * sso 退出功能 ，根据客户端传入跳转
 */
public class SsoLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final String REDIRECT_URL = "redirect_url";

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {

		// 获取请求参数中是否包含 回调地址
		String redirectUrl = request.getParameter(REDIRECT_URL);
		String referer = request.getHeader(HttpHeaders.REFERER);

		if (StrUtil.isNotBlank(redirectUrl)) {
			response.sendRedirect(redirectUrl);
		}
		else if (StrUtil.isNotBlank(referer)) {
			// 默认跳转referer 地址
			response.sendRedirect(referer);
		}
	}

}
