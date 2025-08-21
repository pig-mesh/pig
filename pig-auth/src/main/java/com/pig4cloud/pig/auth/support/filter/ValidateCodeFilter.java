package com.pig4cloud.pig.auth.support.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.auth.config.AuthSecurityConfigProperties;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.core.util.WebUtils;

/**
 * 登录前处理器
 *
 * @author lengleng
 * @date 2024/4/3
 */

import cn.dev33.satoken.oauth2.consts.SaOAuth2Consts;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author lbw
 * @date 2024-01-06
 * <p>
 * 登录前置处理器： 前端密码传输密文解密，验证码处理
 */
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

	private final AuthSecurityConfigProperties authSecurityConfigProperties;

	private final ObjectMapper objectMapper;

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
		String grantType = request.getParameter(SaOAuth2Consts.Param.grant_type);
		if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
			filterChain.doFilter(request, response);
			return;
		}

		// 客户端配置跳过验证码
		boolean isIgnoreClient = authSecurityConfigProperties.getIgnoreClients().contains(WebUtils.getClientId());
		if (isIgnoreClient) {
			filterChain.doFilter(request, response);
			return;
		}

		// 校验验证码 1. 客户端开启验证码 2. 短信模式
		try {
			checkCode();
			filterChain.doFilter(request, response);
		}
		catch (ValidateCodeException validateCodeException) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(ContentType.JSON.getValue());
			objectMapper.writeValue(response.getOutputStream(), R.failed(validateCodeException.getMessage()));

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
		RedisTemplate<String, String> redisTemplate = SpringContextHolder.getBean(RedisTemplate.class);
		if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
			throw new ValidateCodeException("验证码不合法");
		}

		Object codeObj = redisTemplate.opsForValue().get(key);

		if (codeObj == null) {
			throw new ValidateCodeException("验证码不合法");
		}

		String saveCode = codeObj.toString();
		if (StrUtil.isBlank(saveCode)) {
			redisTemplate.delete(key);
			throw new ValidateCodeException("验证码不合法");
		}

		if (!StrUtil.equals(saveCode, code)) {
			redisTemplate.delete(key);
			throw new ValidateCodeException("验证码不合法");
		}

		redisTemplate.delete(key);
	}

}
