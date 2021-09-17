package com.anjiplus.template.gaea.business.filter;

import com.alibaba.fastjson.JSONObject;
import com.anji.plus.gaea.bean.ResponseBean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 简单的鉴权
 *
 * @author raodeming
 * @date 2021/6/24.
 */
@Component
@Order(Integer.MIN_VALUE + 99)
public class TokenFilter implements Filter {

	private static final Pattern PATTERN = Pattern.compile(".*().*");

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String uri = request.getRequestURI();

		String method = request.getMethod();

		if (HttpMethod.POST.name().equalsIgnoreCase(method) || HttpMethod.PUT.name().equalsIgnoreCase(method)
				|| HttpMethod.DELETE.name().equalsIgnoreCase(method)) {
			ResponseBean responseBean = ResponseBean.builder().code("50001").message("在线体验版本，不允许此操作。").build();
			response.getWriter().print(JSONObject.toJSONString(responseBean));
			return;
		}

		// 执行
		filterChain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}

	/**
	 * 根据名单，生成正则
	 * @param skipUrlList
	 * @return
	 */
	private Pattern fitByList(List<String> skipUrlList) {
		if (skipUrlList == null || skipUrlList.size() == 0) {
			return PATTERN;
		}
		StringBuffer patternString = new StringBuffer();
		patternString.append(".*(");

		skipUrlList.stream().forEach(url -> {
			patternString.append(url.trim());
			patternString.append("|");
		});
		if (skipUrlList.size() > 0) {
			patternString.deleteCharAt(patternString.length() - 1);
		}
		patternString.append(").*");

		return Pattern.compile(patternString.toString());
	}

	private void error(HttpServletResponse response) throws IOException {
		ResponseBean responseBean = ResponseBean.builder().code("50014").message("The Token has expired").build();
		response.getWriter().print(JSONObject.toJSONString(responseBean));
	}

}
