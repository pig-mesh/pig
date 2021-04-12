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

package com.pig4cloud.pig.common.security.component;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.annotation.Inner;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lengleng
 * @date 2019/02/14
 * <p>
 * 服务间接口不鉴权处理逻辑
 */
@Slf4j
@Aspect
@RequiredArgsConstructor
public class PigSecurityInnerAspect implements Ordered {

	private final HttpServletRequest request;

	@SneakyThrows
	@Around("@within(inner) || @annotation(inner)")
	public Object around(ProceedingJoinPoint point, Inner inner) {
		// 实际注入的inner实体由表达式后一个注解决定，即是方法上的@Inner注解实体，若方法上无@Inner注解，则获取类上的
		if (inner == null) {
			Class<?> clazz = point.getTarget().getClass();
			inner = AnnotationUtils.findAnnotation(clazz, Inner.class);
		}
		String header = request.getHeader(SecurityConstants.FROM);
		if (inner.value() && !StrUtil.equals(SecurityConstants.FROM_IN, header)) {
			log.warn("访问接口 {} 没有权限", point.getSignature().getName());
			throw new AccessDeniedException("Access is denied");
		}
		return point.proceed();
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}

}
