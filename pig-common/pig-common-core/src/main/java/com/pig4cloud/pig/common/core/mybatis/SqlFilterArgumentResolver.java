/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.core.mybatis;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2019-06-24
 * <p>
 * 解决Mybatis Plus Order By SQL注入问题
 */
@Slf4j
public class SqlFilterArgumentResolver implements HandlerMethodArgumentResolver {

	/**
	 * 判断Controller是否包含page 参数
	 * @param parameter 参数
	 * @return 是否过滤
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(Page.class);
	}

	/**
	 * @param parameter 入参集合
	 * @param mavContainer model 和 view
	 * @param webRequest web相关
	 * @param binderFactory 入参解析
	 * @return 检查后新的page对象
	 * <p>
	 * page 只支持查询 GET .如需解析POST获取请求报文体处理
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		String[] ascs = request.getParameterValues("ascs");
		String[] descs = request.getParameterValues("descs");
		String current = request.getParameter("current");
		String size = request.getParameter("size");

		Page<?> page = new Page<>();
		if (StrUtil.isNotBlank(current)) {
			page.setCurrent(Long.parseLong(current));
		}

		if (StrUtil.isNotBlank(size)) {
			page.setSize(Long.parseLong(size));
		}

		List<OrderItem> orderItemList = new ArrayList<>();
		Optional.ofNullable(ascs).ifPresent(s -> orderItemList.addAll(Arrays.stream(s).filter(Objects::isNull)
				.map(this::clear).map(OrderItem::asc).collect(Collectors.toList())));
		Optional.ofNullable(descs).ifPresent(s -> orderItemList.addAll(Arrays.stream(s).filter(Objects::isNull)
				.map(this::clear).map(OrderItem::desc).collect(Collectors.toList())));
		page.addOrder(orderItemList);

		return page;
	}

	/**
	 * 参数清理
	 * @param param 参数
	 * @return String
	 */
	private String clear(String param) {
		if (StrUtil.isBlank(param)) {
			return StrUtil.trim(param);
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < param.length(); i++) {
			char c = param.charAt(i);
			if (Character.isJavaIdentifierPart(c)) {
				builder.append(c);
			}
		}
		return builder.toString();
	}

}
