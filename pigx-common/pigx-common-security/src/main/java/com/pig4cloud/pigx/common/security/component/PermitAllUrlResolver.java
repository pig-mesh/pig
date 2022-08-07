/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.common.security.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2019/4/13
 * <p>
 * 资源暴露处理器
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression("!'${security.oauth2.client.ignore-urls}'.isEmpty()")
@ConfigurationProperties(prefix = "security.oauth2.client")
public class PermitAllUrlResolver implements InitializingBean {

	private static final PathMatcher PATHMATCHER = new AntPathMatcher();

	private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

	private static final String[] DEFAULT_IGNORE_URLS = new String[] { "/error", "/v2/api-docs" };

	private final WebApplicationContext applicationContext;

	@Getter
	@Setter
	private List<String> ignoreUrls = new ArrayList<>();

	@Override
	public void afterPropertiesSet() {
		RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

		for (RequestMappingInfo info : map.keySet()) {
			HandlerMethod handlerMethod = map.get(info);

			// 1. 首先获取类上边 @Inner 注解
			Inner controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Inner.class);

			// 2. 当类上不包含 @Inner 注解则获取该方法的注解
			if (controller == null) {
				Inner method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Inner.class);
				Optional.ofNullable(method).ifPresent(inner -> Objects.requireNonNull(info.getPathPatternsCondition())
						.getPatternValues().forEach(url -> this.filterPath(url, info, map)));
				continue;
			}

			// 3. 当类上包含 @Inner 注解 判断handlerMethod 是否包含在 inner 类中
			Class<?> beanType = handlerMethod.getBeanType();
			Method[] methods = beanType.getDeclaredMethods();
			Method method = handlerMethod.getMethod();
			if (ArrayUtil.contains(methods, method)) {
				Objects.requireNonNull(info.getPathPatternsCondition()).getPatternValues()
						.forEach(url -> filterPath(url, info, map));
			}
		}

	}

	/**
	 * 过滤 Inner 设置
	 * <p>
	 * 0. 暴露安全检查 1. 路径转换： 如果为restful(/xx/{xx}) --> /xx/* ant 表达式 2.
	 * 构建表达式：允许暴露的接口|允许暴露的方法类型,允许暴露的方法类型 URL|GET,POST,DELETE,PUT
	 * </p>
	 * @param url mapping路径
	 * @param info 请求犯法
	 * @param map 路由映射信息
	 */
	private void filterPath(String url, RequestMappingInfo info, Map<RequestMappingInfo, HandlerMethod> map) {
		// 安全检查
		if (SecurityConstants.INNER_CHECK) {
			security(url, info, map);
		}

		List<String> methodList = info.getMethodsCondition().getMethods().stream().map(RequestMethod::name)
				.collect(Collectors.toList());
		String resultUrl = ReUtil.replaceAll(url, PATTERN, "*");
		if (CollUtil.isEmpty(methodList)) {
			ignoreUrls.add(resultUrl);
		}
		else {
			ignoreUrls.add(String.format("%s|%s", resultUrl, CollUtil.join(methodList, StrUtil.COMMA)));
		}

		// 增加默认暴露规则
		for (String ignoreUrl : DEFAULT_IGNORE_URLS) {
			ignoreUrls.add(ignoreUrl);
		}
	}

	/**
	 * 针对Pathvariable 请求安全检查。增加启动好使影响启动效率 请注意
	 * @param url 接口路径
	 * @param rq 当前请求的元信息
	 * @param map springmvc 接口列表
	 */
	private void security(String url, RequestMappingInfo rq, Map<RequestMappingInfo, HandlerMethod> map) {
		// 判断 URL 是否是 rest path 形式
		if (!StrUtil.containsAny(url, StrUtil.DELIM_START, StrUtil.DELIM_END)) {
			return;
		}

		for (RequestMappingInfo info : map.keySet()) {
			Set<RequestMethod> methods = info.getMethodsCondition().getMethods();
			// 如果请求方法不匹配跳过
			if (!CollUtil.containsAny(methods, rq.getMethodsCondition().getMethods())) {
				continue;
			}

			// 如果请求方法路径匹配
			Set<String> patterns = Objects.requireNonNull(info.getPathPatternsCondition()).getPatternValues();
			for (String pattern : patterns) {
				// 跳过自身
				if (StrUtil.equals(url, pattern)) {
					continue;
				}

				if (PATHMATCHER.match(url, pattern)) {
					HandlerMethod rqMethod = map.get(rq);
					HandlerMethod infoMethod = map.get(info);
					log.error("@Inner 标记接口 ==> {}.{} 使用不当，会额外暴露接口 ==> {}.{} 请知悉", rqMethod.getBeanType().getName(),
							rqMethod.getMethod().getName(), infoMethod.getBeanType().getName(),
							infoMethod.getMethod().getName());
				}
			}
		}
	}

	/**
	 * 获取对外暴露的URL，注册到 spring security
	 * @param registry spring security context
	 */
	public void registry(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
		for (String url : getIgnoreUrls()) {
			List<String> strings = StrUtil.split(url, "|");

			// 仅配置对外暴露的URL ，则注册到 spring security的为全部方法
			if (strings.size() == 1) {
				registry.antMatchers(strings.get(0)).permitAll();
				continue;
			}

			// 当配置对外的URL|GET,POST 这种形式，则获取方法列表 并注册到 spring security
			if (strings.size() == 2) {
				for (String method : StrUtil.split(strings.get(1), StrUtil.COMMA)) {
					registry.antMatchers(HttpMethod.valueOf(method), strings.get(0)).permitAll();
				}
				continue;
			}

			log.warn("{} 配置无效，无法配置对外暴露", url);
		}
	}

}
