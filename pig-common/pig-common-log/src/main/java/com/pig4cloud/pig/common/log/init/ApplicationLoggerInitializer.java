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

package com.pig4cloud.pig.common.log.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author lengleng
 * @date 2019-06-25
 * <p>
 * 通过环境变量的形式注入 logging.file 自动维护 Spring Boot Admin Logger Viewer
 */
public class ApplicationLoggerInitializer implements EnvironmentPostProcessor, Ordered {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String appName = environment.getProperty("spring.application.name");
		String logBase = environment.getProperty("LOGGING_PATH", "logs");

		// spring boot admin 直接加载日志
		System.setProperty("logging.file.name", String.format("%s/%s/debug.log", logBase, appName));

		// 避免 sentinel 1.8.4+ 心跳日志过大
		System.setProperty("csp.sentinel.log.level", "OFF");

		// 避免各种依赖的地方组件造成 BeanPostProcessorChecker 警告
		System.setProperty("logging.level.org.springframework.context.support.PostProcessorRegistrationDelegate",
				"ERROR");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
