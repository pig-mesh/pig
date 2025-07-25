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

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 自动配置类
 *
 * @author lengleng
 * @date 2025/05/30
 */
@AutoConfiguration
public class RestTemplateConfiguration {

	/**
	 * 创建动态REST模板
	 * @return {@link RestTemplate} REST模板实例
	 */
	@Bean
	@LoadBalanced
	@ConditionalOnProperty(value = "spring.cloud.nacos.discovery.enabled", havingValue = "true", matchIfMissing = true)
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * 创建支持负载均衡的REST客户端构建器
	 * @return {@link RestClient.Builder} REST客户端构建器
	 */
	@Bean
	@LoadBalanced
	@ConditionalOnProperty(value = "spring.cloud.nacos.discovery.enabled", havingValue = "true", matchIfMissing = true)
	RestClient.Builder restClientBuilder() {
		return RestClient.builder();
	}

}
