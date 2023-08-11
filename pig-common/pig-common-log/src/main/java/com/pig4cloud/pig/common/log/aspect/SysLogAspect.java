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

package com.pig4cloud.pig.common.log.aspect;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.log.event.SysLogEvent;
import com.pig4cloud.pig.common.log.event.SysLogEventSource;
import com.pig4cloud.pig.common.log.util.LogTypeEnum;
import com.pig4cloud.pig.common.log.util.SysLogUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;

/**
 * 操作日志使用spring event异步入库
 *
 * @author L.cm
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class SysLogAspect {

	@Around("@annotation(sysLog)")
	@SneakyThrows
	public Object around(ProceedingJoinPoint point, com.pig4cloud.pig.common.log.annotation.SysLog sysLog) {
		String strClassName = point.getTarget().getClass().getName();
		String strMethodName = point.getSignature().getName();
		log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

		String value = sysLog.value();
		String expression = sysLog.expression();
		// 当前表达式存在 SPEL，会覆盖 value 的值
		if (StrUtil.isNotBlank(expression)) {
			// 解析SPEL
			MethodSignature signature = (MethodSignature) point.getSignature();
			EvaluationContext context = SysLogUtils.getContext(point.getArgs(), signature.getMethod());
			try {
				value = SysLogUtils.getValue(context, expression, String.class);
			}
			catch (Exception e) {
				// SPEL 表达式异常，获取 value 的值
				log.error("@SysLog 解析SPEL {} 异常", expression);
			}
		}

		SysLogEventSource logVo = SysLogUtils.getSysLog();
		logVo.setTitle(value);
		// 获取请求body参数
		if (StrUtil.isBlank(logVo.getParams())) {
			logVo.setBody(point.getArgs());
		}
		// 发送异步日志事件
		Long startTime = System.currentTimeMillis();
		Object obj;

		try {
			obj = point.proceed();
		}
		catch (Exception e) {
			logVo.setLogType(LogTypeEnum.ERROR.getType());
			logVo.setException(e.getMessage());
			throw e;
		}
		finally {
			Long endTime = System.currentTimeMillis();
			logVo.setTime(endTime - startTime);
			SpringContextHolder.publishEvent(new SysLogEvent(logVo));
		}

		return obj;
	}

}
