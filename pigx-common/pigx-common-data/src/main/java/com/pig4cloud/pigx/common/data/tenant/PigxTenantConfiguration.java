/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pigx.common.data.tenant;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * 租户信息拦截配置类
 *
 * @author lengleng
 * @date 2025/06/27
 */
@Configuration
public class PigxTenantConfiguration {

	/**
	 * 创建并返回PigxFeignTenantInterceptor实例
	 *
	 * @return RequestInterceptor实例
	 */
	@Bean
	public RequestInterceptor pigxFeignTenantInterceptor() {
		return new PigxFeignTenantInterceptor();
	}

	/**
	 * 创建并返回一个租户请求拦截器实例
	 *
	 * @return 配置好的租户请求拦截器
	 */
	@Bean
	public ClientHttpRequestInterceptor pigxTenantRequestInterceptor() {
		return new TenantRequestInterceptor();
	}

}
