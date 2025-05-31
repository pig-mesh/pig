package com.pig4cloud.pig.common.datasource.config;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.core.Ordered;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

/**
 * 清空上文的DS设置避免污染当前线程的过滤器
 *
 * @author lengleng
 * @date 2025/05/31
 */
public class ClearTtlDataSourceFilter extends GenericFilterBean implements Ordered {

	/**
	 * 过滤器方法，用于清除动态数据源上下文
	 * @param servletRequest 请求对象
	 * @param servletResponse 响应对象
	 * @param filterChain 过滤器链
	 * @throws IOException IO异常
	 * @throws ServletException Servlet异常
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		DynamicDataSourceContextHolder.clear();
		filterChain.doFilter(servletRequest, servletResponse);
		DynamicDataSourceContextHolder.clear();
	}

	@Override
	public int getOrder() {
		return Integer.MIN_VALUE;
	}

}
