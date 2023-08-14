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

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.pig4cloud.pigx.admin.api.dto.SysLogDTO;
import com.pig4cloud.pigx.admin.api.entity.SysLog;
import com.pig4cloud.pigx.admin.api.feign.RemoteLogService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.jackson.PigxJavaTimeModule;
import com.pig4cloud.pigx.common.log.config.PigXLogProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Objects;

/**
 * @author lengleng 异步监听日志事件
 */
@Slf4j
@RequiredArgsConstructor
public class SysLogListener implements InitializingBean {

	// new 一个 避免日志脱敏策略影响全局ObjectMapper
	private final static ObjectMapper objectMapper = new ObjectMapper();

	private final RemoteLogService remoteLogService;

	private final PigXLogProperties logProperties;

	@SneakyThrows
	@Async
	@Order
	@EventListener(SysLogEvent.class)
	public void saveSysLog(SysLogEvent event) {
		SysLogDTO source = (SysLogDTO) event.getSource();

		// json 格式刷参数放在异步中处理，提升性能
		if (Objects.nonNull(source.getBody()) && logProperties.isRequestEnabled()) {
			String params = objectMapper.writeValueAsString(source.getBody());
			source.setParams(StrUtil.subPre(params, logProperties.getMaxLength()));
		}

		source.setBody(null);
		remoteLogService.saveLog(source, SecurityConstants.FROM_IN);
	}

	@Override
	public void afterPropertiesSet() {
		objectMapper.addMixIn(Object.class, PropertyFilterMixIn.class);
		String[] ignorableFieldNames = logProperties.getExcludeFields().toArray(new String[0]);

		FilterProvider filters = new SimpleFilterProvider().addFilter("filter properties by name",
				SimpleBeanPropertyFilter.serializeAllExcept(ignorableFieldNames));
		objectMapper.setFilterProvider(filters);
		objectMapper.registerModule(new PigxJavaTimeModule());
	}

	@JsonFilter("filter properties by name")
	class PropertyFilterMixIn {

	}

}
