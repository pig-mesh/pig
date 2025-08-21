package com.pig4cloud.pig.auth.support.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import com.pig4cloud.pig.common.core.util.RedisUtils;
import com.pig4cloud.pig.common.core.util.WebUtils;

/**
 * 登录前处理器
 *
 * @author lengleng
 * @date 2024/4/3
 */

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 验证码过滤器：用于处理登录请求中的验证码校验
 *
 * @author lengleng
 * @date 2025/05/30
 */
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

	private final AuthSecurityConfigProperties authSecurityConfigProperties;

	/**
	 * 过滤器内部处理逻辑，用于验证码校验
	 * @param request HTTP请求
	 * @param response HTTP响应
	 * @param filterChain 过滤器链
	 * @throws ServletException Servlet异常
	 * @throws IOException IO异常
	 */
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
		if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 如果是密码模式 && 客户端不需要校验验证码
		boolean isIgnoreClient = authSecurityConfigProperties.getIgnoreClients().contains(WebUtils.getClientId());
		if (StrUtil.equalsAnyIgnoreCase(grantType, SecurityConstants.PASSWORD, SecurityConstants.CLIENT_CREDENTIALS,
				SecurityConstants.AUTHORIZATION_CODE) && isIgnoreClient) {
			filterChain.doFilter(request, response);
			return;
		}

		// 校验验证码 1. 客户端开启验证码 2. 短信模式
		try {
			checkCode();
			filterChain.doFilter(request, response);
		}
		catch (ValidateCodeException validateCodeException) {
			throw new OAuth2AuthenticationException(validateCodeException.getMessage());
		}
	}

	/**
	 * 校验验证码
	 */
	private void checkCode() throws ValidateCodeException {
		Optional<HttpServletRequest> request = WebUtils.getRequest();
		String code = request.get().getParameter("code");

		if (StrUtil.isBlank(code)) {
			throw new ValidateCodeException("验证码不能为空");
		}

		String randomStr = request.get().getParameter("randomStr");

		// https://gitee.com/log4j/pig/issues/IWA0D
		String mobile = request.get().getParameter("mobile");
		if (StrUtil.isNotBlank(mobile)) {
			randomStr = mobile;
		}

		String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;
		if (!RedisUtils.hasKey(key)) {
			throw new ValidateCodeException("验证码不合法");
		}

		String saveCode = RedisUtils.get(key);

		if (StrUtil.isBlank(saveCode)) {
			RedisUtils.delete(key);
			throw new ValidateCodeException("验证码不合法");
		}

		if (!StrUtil.equals(saveCode, code)) {
			RedisUtils.delete(key);
			throw new ValidateCodeException("验证码不合法");
		}
	}

}
