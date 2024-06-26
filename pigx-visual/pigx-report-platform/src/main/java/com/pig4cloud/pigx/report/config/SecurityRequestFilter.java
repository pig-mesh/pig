package com.pig4cloud.pigx.report.config;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.api.feign.RemoteTokenService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.RetOps;
import io.springboot.plugin.goview.common.domain.AjaxResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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

	private final PathMatcher pathMatcher = new AntPathMatcher();

	private final RemoteTokenService remoteTokenService;

	private final SecurityConfiguration securityConfiguration;

	/**
	 * 在请求被处理前执行过滤操作
	 * @param request HttpServletRequest对象
	 * @param response HttpServletResponse对象
	 * @param filterChain 过滤器链条
	 */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// 获取请求URI
		String requestURI = request.getRequestURI();
		List<String> ignoreUrls = securityConfiguration.getIgnoreUrls();

		// 如果请求URI匹配特定的值，允许该请求通过
		if (ignoreUrls.stream().anyMatch(ignoreUrl -> pathMatcher.match(ignoreUrl, requestURI))) {
			filterChain.doFilter(request, response);
			return;
		}

		// 获取请求头中的token和tenantId参数
		String accessToken = request.getHeader("token");
		String tenantId = request.getHeader(CommonConstants.TENANT_ID);

		// 如果token参数不存在，返回错误信息
		if (StrUtil.isBlank(accessToken)) {
			sendErrorMsg(request, response);
			return;
		}

		// 当tenantId参数不存在时，默认为1
		tenantId = StrUtil.isNotBlank(tenantId) ? tenantId : "1";

		// 获取用户信息
		Optional<String> principalName = RetOps
			.of(remoteTokenService.queryToken(accessToken, tenantId))
			.getDataIf(RetOps.CODE_SUCCESS)
			.map(o -> (String) o.get("principalName"));

		// 如果用户信息不存在，返回错误信息
		if (!principalName.isPresent()) {
			sendErrorMsg(request, response);
			return;
		}

		// 允许该请求通过
		filterChain.doFilter(request, response);
	}

	/**
	 * 向客户端发送错误消息
	 * @param request HttpServletRequest对象
	 * @param response HttpServletResponse对象
	 */
	private void sendErrorMsg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding(CharsetUtil.UTF_8);
		response.setContentType(ContentType.JSON.getValue());
		PrintWriter out = response.getWriter();
		out.println(JSONUtil.toJsonStr(AjaxResult.error(HttpStatus.UNAUTHORIZED.value(), "token require")));
		out.flush();
		out.close();
	}

}

@Component
@Configuration
@ConfigurationProperties(prefix = "security.oauth2.client")
class SecurityConfiguration {

	@Getter
	@Setter
	private List<String> ignoreUrls = new ArrayList<>();

}
