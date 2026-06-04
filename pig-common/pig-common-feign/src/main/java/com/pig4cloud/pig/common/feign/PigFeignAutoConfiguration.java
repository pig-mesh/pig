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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.common.feign.endpoint.FeignClientEndpoint;
import com.pig4cloud.pig.common.feign.interceptor.PigFeignInnerRequestInterceptor;
import com.pig4cloud.pig.common.feign.interceptor.PigFeignLanguageInterceptor;
import com.pig4cloud.pig.common.feign.interceptor.PigFeignRequestCloseInterceptor;
import feign.Feign;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.PigFeignClientsRegistrar;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;

import java.util.ListIterator;

/**
 * Feign 客户端自动化配置
 * <p>
 * 提供 Feign 客户端的核心配置，包括：
 * <ul>
 * <li>Jackson 2 消息转换器配置 - 确保与 Spring MVC 使用相同的序列化机制</li>
 * <li>请求拦截器 - 添加内部调用标识、语言标头、连接关闭标头</li>
 * <li>Actuator 端点 - 提供 Feign 客户端监控能力</li>
 * </ul>
 * <p>
 * <b>重要说明</b>：本项目使用 Jackson 2.x 而非 Spring Boot 4 默认的 Jackson 3.x， 因此必须显式配置 Feign 使用
 * Jackson 2 的 ObjectMapper，否则会导致服务间调用时的序列化/反序列化错误。
 *
 * @author lengleng
 * @date 2020/2/8
 * @see HttpMessageConverterCustomizer
 * @see PigFeignInnerRequestInterceptor
 */
@Configuration
@ConditionalOnClass(Feign.class)
@Import(PigFeignClientsRegistrar.class)
@AutoConfigureAfter(EnableFeignClients.class)
@SuppressWarnings("removal")
public class PigFeignAutoConfiguration {

	/**
	 * Feign 客户端监控端点
	 * <p>
	 * 提供 Actuator 端点用于监控和管理 Feign 客户端，可以查看已注册的 Feign 客户端列表及其配置信息。 仅在 Actuator
	 * 可用且端点启用时才会创建此 Bean。
	 * @param context Spring 应用上下文
	 * @return FeignClientEndpoint 实例
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnAvailableEndpoint
	public FeignClientEndpoint feignClientEndpoint(ApplicationContext context) {
		return new FeignClientEndpoint(context);
	}

	/**
	 * 配置 Feign 使用 Jackson 2 消息转换器
	 * <p>
	 * <b>问题背景</b>：Spring Boot 4 默认使用 Jackson 3.x，但本项目为了兼容性使用 Jackson 2.x。 如果不显式配置，Feign
	 * 客户端会使用默认的 Jackson 3 进行序列化/反序列化， 导致与使用 Jackson 2 的 Spring MVC 之间出现数据格式不一致的问题。
	 * <p>
	 * <b>典型错误</b>： <pre>
	 * Cannot deserialize value of type `java.time.LocalDateTime` from String "2019-05-15 00:00:00"
	 * </pre>
	 * <p>
	 * <b>解决方案</b>：通过 {@link HttpMessageConverterCustomizer} 在 Feign 初始化时：
	 * <ol>
	 * <li>原地把 Jackson 3 的 {@link JacksonJsonHttpMessageConverter} 替换成 使用 Spring 装配好的
	 * ObjectMapper 的 Jackson 2 转换器</li>
	 * <li>找不到 Jackson 3 转换器时把 Jackson 2 转换器插入队首作为兜底</li>
	 * <li>保留 byte[]、String、multipart 等其它默认转换器，避免破坏文件下载/上传等非 JSON 调用</li>
	 * <li>注入的 ObjectMapper 已被所有 {@code Jackson2ObjectMapperBuilderCustomizer} （包括
	 * pig-common-xss 的反序列化清洗）处理过，Feign 与 Spring MVC 序列化行为完全一致</li>
	 * </ol>
	 * <p>
	 * <b>注意</b>：此配置使用了已标记为 removal 的 API（{@code MappingJackson2HttpMessageConverter}），
	 * 这是有意为之，因为项目需要使用 Jackson 2 而非 Jackson 3。
	 * <p>
	 * <b>不使用 {@link ConditionalOnMissingBean}</b>：{@code HttpMessageConverterCustomizer}
	 * 是可叠加的扩展点，OpenFeign 等模块可能已注册其它 customizer；如果加上 missing-bean 条件， 本 Bean
	 * 会被静默跳过，Jackson 2 修复消失。
	 * @param objectMapper Spring 装配好的 Jackson 2 ObjectMapper（来自 JacksonConfiguration 的
	 * customizer 链）
	 * @return HttpMessageConverterCustomizer 实例
	 * @see com.pig4cloud.pig.common.core.config.JacksonConfiguration
	 * @see com.pig4cloud.pig.common.core.jackson.PigJavaTimeModule
	 */
	@Bean
	public HttpMessageConverterCustomizer pigFeignJackson2Customizer(ObjectMapper objectMapper) {
		return converters -> {
			org.springframework.http.converter.json.MappingJackson2HttpMessageConverter jackson2Converter = new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter(
					objectMapper);
			ListIterator<HttpMessageConverter<?>> it = converters.listIterator();
			boolean replaced = false;
			while (it.hasNext()) {
				if (it.next() instanceof JacksonJsonHttpMessageConverter) {
					it.set(jackson2Converter);
					replaced = true;
				}
			}
			if (!replaced) {
				converters.add(0, jackson2Converter);
			}
		};
	}

	/**
	 * add http connection close header
	 * @return PigFeignRequestCloseInterceptor
	 */
	@Bean
	public PigFeignRequestCloseInterceptor pigFeignRequestCloseInterceptor() {
		return new PigFeignRequestCloseInterceptor();
	}

	/**
	 * add inner request header
	 * @return PigFeignInnerRequestInterceptor
	 */
	@Bean
	public PigFeignInnerRequestInterceptor pigFeignInnerRequestInterceptor() {
		return new PigFeignInnerRequestInterceptor();
	}

	/**
	 * add accept-language header
	 * @return PigFeignLanguageInterceptor
	 */
	@Bean
	public PigFeignLanguageInterceptor pigFeignLanguageInterceptor() {
		return new PigFeignLanguageInterceptor();
	}

}
