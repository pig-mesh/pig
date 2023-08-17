package com.pig4cloud.pig.auth.support.handler;

import cn.hutool.core.util.StrUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

/**
 * @author lengleng
 * @date 2022-06-02
 * <p>
 * sso 退出功能 ，根据客户端传入跳转
 */
public class SsoLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final String REDIRECT_URL = "redirect_url";

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		if (response == null) {
			return;
		}

		// 获取请求参数中是否包含 回调地址
		String redirectUrl = request.getParameter(REDIRECT_URL);
		if (StrUtil.isNotBlank(redirectUrl)) {
			response.sendRedirect(redirectUrl);
		}
		else if (StrUtil.isNotBlank(request.getHeader(HttpHeaders.REFERER))) {
			// 默认跳转referer 地址
			String referer = request.getHeader(HttpHeaders.REFERER);
			response.sendRedirect(referer);
		}
	}

}
