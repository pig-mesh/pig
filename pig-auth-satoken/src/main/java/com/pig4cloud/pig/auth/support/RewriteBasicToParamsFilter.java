package com.pig4cloud.pig.auth.support;

import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.core.util.WebUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * 将 Basic 重写为 Params 过滤器 兼容性处理， sa-token 新版will 适配
 *
 * @author lengleng
 * @date 2024/07/23
 */
@Configuration(proxyBeanMethods = false)
public class RewriteBasicToParamsFilter extends OncePerRequestFilter implements Ordered {

	/**
	 * 过滤器
	 * @param request 请求
	 * @param response 响应
	 * @param filterChain 过滤链
	 * @throws ServletException servlet 异常
	 * @throws IOException ioException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 判断 http 是否包含 basic 认证的
		String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

		// 如果不是以 Basic 作为前缀，则视为无效
		if (StrUtil.isBlank(authorization) || !authorization.startsWith("Basic ")) {
			filterChain.doFilter(request, response);
			return;
		}

		Map<String, String[]> parameterMap = request.getParameterMap();
		// 解析出 clientId 和 clientSecret 放到参数中
		parameterMap.put(SaOAuth2Consts.Param.client_id, new String[] { WebUtils.getClientId(request) });
		parameterMap.put(SaOAuth2Consts.Param.client_secret, new String[] { WebUtils.getClientSecret(request) });
		request.getParameterMap().putAll(parameterMap);
		filterChain.doFilter(request, response);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 100;
	}

}
