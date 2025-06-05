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

package com.pig4cloud.pig.common.feign.sentinel;

import com.alibaba.cloud.sentinel.feign.SentinelFeignAutoConfiguration;
import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.RequestOriginParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.common.feign.sentinel.ext.PigSentinelFeign;
import com.pig4cloud.pig.common.feign.sentinel.handle.PigUrlBlockHandler;
import com.pig4cloud.pig.common.feign.sentinel.parser.PigHeaderRequestOriginParser;
import feign.Feign;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Sentinel 自动配置类
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(SentinelFeignAutoConfiguration.class)
public class SentinelAutoConfiguration {

	/**
	 * 创建Feign Sentinel构建器
	 * @return Feign.Builder实例
	 * @ConditionalOnMissingBean 当容器中不存在该类型bean时创建
	 * @ConditionalOnProperty 当配置项spring.cloud.openfeign.sentinel.enabled为true时生效
	 * @Scope 指定bean作用域为prototype
	 */
	@Bean
	@Scope("prototype")
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "spring.cloud.openfeign.sentinel.enabled")
	public Feign.Builder feignSentinelBuilder() {
		return PigSentinelFeign.builder();
	}

	/**
	 * 创建默认的BlockExceptionHandler bean
	 * @param objectMapper 对象映射器
	 * @return PigUrlBlockHandler实例
	 * @ConditionalOnMissingBean 当容器中不存在该类型bean时创建
	 */
	@Bean
	@ConditionalOnMissingBean
	public BlockExceptionHandler blockExceptionHandler(ObjectMapper objectMapper) {
		return new PigUrlBlockHandler(objectMapper);
	}

	/**
	 * 创建并返回一个RequestOriginParser bean，当容器中不存在该类型的bean时生效
	 * @return 默认的PigHeaderRequestOriginParser实例
	 */
	@Bean
	@ConditionalOnMissingBean
	public RequestOriginParser requestOriginParser() {
		return new PigHeaderRequestOriginParser();
	}

}
