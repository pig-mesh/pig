package com.pig4cloud.pig.common.core.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.method.annotation.ExtendedServletRequestDataBinder;

/**
 * priority 标头配置
 *
 * @author lengleng
 * @date 2025/04/16
 */
@ControllerAdvice
public class PriorityHeaderInitBinder {

	/**
	 * 初始化绑定器
	 * <a href="https://github.com/spring-projects/spring-framework/issues/34039">
	 * 处理 spring 6.2 严格遵循RFC 导致 的请求头和请求参数冲突问题
	 * </a>
	 *
	 * @param binder 绑定规则
	 */
	@InitBinder
	public void initBinder(ExtendedServletRequestDataBinder binder) {
		binder.addHeaderPredicate(header -> false);
	}
}
