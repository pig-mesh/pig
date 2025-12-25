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

package com.pig4cloud.pig.common.core.config;

import com.pig4cloud.pig.common.core.jackson.LongToStringModule;
import com.pig4cloud.pig.common.core.jackson.PigJavaTimeModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Jackson配置类，用于自定义Jackson的ObjectMapper配置
 *
 * @author lengleng
 * @author L.cm
 * @author lishangbu
 * @date 2025/05/30
 */
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
@AutoConfigureBefore(JacksonAutoConfiguration.class)
public class JacksonConfiguration {

	/**
	 * 自定义JsonMapperBuilder配置 (Jackson 3)
	 * @return JsonMapperBuilderCustomizer实例，包含以下配置： 1. 设置地区为中国 2. 设置系统默认时区 3. 配置Long类型序列化为字符串
	 * 4. 注册自定义时间模块
	 */
	@Bean
	@ConditionalOnMissingBean
	public JsonMapperBuilderCustomizer customizer() {
		return builder -> builder.defaultLocale(Locale.CHINA)
			.defaultTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
			.addModule(new PigJavaTimeModule())
			.addModule(new LongToStringModule());
	}

}
