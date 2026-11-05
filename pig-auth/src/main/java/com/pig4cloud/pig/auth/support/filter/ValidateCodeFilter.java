package com.pig4cloud.pig.auth.support.filter;

/**
 * 登录前处理器
 *
 * @author lengleng
 * @date 2024/4/3
 */

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.auth.support.core.AuthCaptchaSupport;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

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
		if (StrUtil.equals(grantType, SecurityConstants.REFRESH_TOKEN)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			if (StrUtil.equals(SecurityConstants.GRANT_MOBILE, grantType)) {
				String mobile = request.getParameter(SecurityConstants.GRANT_MOBILE);
				if (StrUtil.isBlank(mobile)) {
					throw new OAuth2AuthenticationException(SecurityConstants.GRANT_MOBILE);
				}
				// 短信是身份凭据，不受客户端图形验证码开关控制。
				if (authCaptchaSupport.isSmsLogin(mobile)) {
					authCaptchaSupport.validateSmsCode(request);
				}
			}
			else if (isCheckCaptchaClient()) {
				authCaptchaSupport.validateCode(request);
			}
			filterChain.doFilter(request, response);
		}
		catch (ValidateCodeException validateCodeException) {
			throw new OAuth2AuthenticationException(validateCodeException.getMessage());
		}
	}

	private boolean isCheckCaptchaClient() {
		// 使用已认证客户端，兼容 Basic 和 client_secret_post，不信任额外的 client_id 参数。
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String clientId = authentication instanceof OAuth2ClientAuthenticationToken client && client.isAuthenticated()
				&& Objects.nonNull(client.getRegisteredClient()) ? client.getRegisteredClient().getClientId() : null;
		return authCaptchaSupport.isCaptchaEnabled(clientId);
	}

}
