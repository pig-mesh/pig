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

package com.pig4cloud.pig.common.feign;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.http.converter.autoconfigure.ClientHttpMessageConvertersCustomizer;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * PIG Feign 客户端子上下文配置。
 * <p>
 * 不加 @Configuration：该类仅通过 defaultConfiguration 注册到 Feign 子上下文， 避免被 pig-boot 等主上下文组件扫描误引入。
 *
 * @author lengleng
 */
public class PigFeignClientConfiguration {

	/**
	 * 构建并预热 Feign 消息转换器。
	 * @param customizers Spring Boot 客户端转换器定制器
	 * @param cloudCustomizers Spring Cloud 转换器定制器
	 * @return 已完成初始化的 FeignHttpMessageConverters
	 */
	@Bean
	public FeignHttpMessageConverters pigFeignHttpMessageConverters(
			ObjectProvider<ClientHttpMessageConvertersCustomizer> customizers,
			ObjectProvider<HttpMessageConverterCustomizer> cloudCustomizers) {
		FeignHttpMessageConverters converters = new FeignHttpMessageConverters(customizers, cloudCustomizers);
		// OpenFeign 5.0.2 在首次调用 getConverters() 时会先发布空列表再填充内容。
		// Feign 子上下文创建 Bean 时预热，避免并发首请求读到尚未初始化完成的空列表。
		converters.getConverters();
		return converters;
	}

}
