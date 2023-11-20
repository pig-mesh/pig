package com.pig4cloud.pig.auth.support.handler;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.exception.ValidateCodeException;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.core.util.WebUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PigLoginPreFilter extends OncePerRequestFilter {

	private final ObjectMapper objectMapper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String requestUrl = request.getServletPath();

		if (SecurityConstants.OAUTH_TOKEN_URL.equals(requestUrl)) {
			// 刷新token，直接向下执行
			String grantType = request.getParameter("grant_type");
			if (StrUtil.equals(SecurityConstants.REFRESH_TOKEN, grantType)) {
				filterChain.doFilter(request, response);
				return;
			}


			// 校验验证码 1. 客户端开启验证码 2. 短信模式
			if (true) {
				try {
					checkCode();
				}
				catch (ValidateCodeException validateCodeException) {
					response.setCharacterEncoding(CharsetUtil.UTF_8);
					response.setStatus(HttpStatus.PRECONDITION_REQUIRED.value());
					R<String> result = new R<>();
					result.setCode(CommonConstants.FAIL);
					result.setData(validateCodeException.getLocalizedMessage());
					result.setMsg(validateCodeException.getLocalizedMessage());
					PrintWriter printWriter = response.getWriter();
					response.setContentType(ContentType.JSON.getValue());
					printWriter.append(objectMapper.writeValueAsString(result));
					return;
				}
			}
		}
		filterChain.doFilter(request, response);
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
		if (!redisTemplate.hasKey(key)) {
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
