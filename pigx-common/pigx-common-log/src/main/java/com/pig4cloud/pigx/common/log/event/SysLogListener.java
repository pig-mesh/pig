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

package com.pig4cloud.pigx.common.log.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.dto.SysLogDTO;
import com.pig4cloud.pigx.admin.api.feign.RemoteLogService;
import com.pig4cloud.pigx.common.core.jackson.PigxJavaTimeModule;
import com.pig4cloud.pigx.common.log.config.PigxLogProperties;
import com.pig4cloud.pigx.common.log.util.JacksonSensitiveFieldUtil;
import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author lengleng 异步监听日志事件
 */
@Slf4j
@RequiredArgsConstructor
public class SysLogListener implements InitializingBean {

	/**
	 * 忽略序列化的对象类型
	 */
	private final static Class[] ignoreClass = {ServletRequest.class, BindingResult.class};

	/**
	 * new 一个 避免日志脱敏策略影响全局ObjectMapper
	 */

	private final RemoteLogService remoteLogService;

	private final PigxLogProperties logProperties;

	@SneakyThrows
	@Async
	@Order
	@EventListener(SysLogEvent.class)
	public void saveSysLog(SysLogEvent event) {
		SysLogDTO source = (SysLogDTO) event.getSource();

		// json 格式刷参数放在异步中处理，提升性能
		if (Objects.nonNull(source.getBody()) && logProperties.isRequestEnabled()) {
			Object[] args = (Object[]) source.getBody();
			List<Object> list = CollUtil.toList(args);
			// 删除部分无法序列化的参数
			list.removeIf(obj -> Arrays.stream(ignoreClass).anyMatch(clazz -> clazz.isAssignableFrom(obj.getClass())));

			try {
				// 序列化参数
				String params = JacksonSensitiveFieldUtil.getObjectMapper().writeValueAsString(list);
				source.setParams(StrUtil.subPre(params, logProperties.getMaxLength()));
			} catch (Exception e) {
				log.error("请求参数序列化异常:{}", e.getMessage());
			}
		}

		source.setBody(null);
		remoteLogService.saveLog(source);
	}

	@Override
	public void afterPropertiesSet() {
		String[] ignorableFieldNames = logProperties.getExcludeFields().toArray(new String[0]);
		JacksonSensitiveFieldUtil.configureSensitiveFields(ignorableFieldNames);
		JacksonSensitiveFieldUtil.registerCustomModule(new PigxJavaTimeModule());
	}
}
