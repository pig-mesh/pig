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

package com.pig4cloud.pigx.common.gray;

import com.pig4cloud.pigx.common.gray.feign.GrayFeignRequestInterceptor;
import com.pig4cloud.pigx.common.gray.rule.GrayLoadBalancerClientConfiguration;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lengleng
 * @date 2020/1/12
 */
@Configuration
@ConditionalOnProperty(value = "gray.rule.enabled", matchIfMissing = true)
@LoadBalancerClients(defaultConfiguration = GrayLoadBalancerClientConfiguration.class)
public class GrayLoadBalancerAutoConfiguration {

	@Bean
	public RequestInterceptor grayFeignRequestInterceptor() {
		return new GrayFeignRequestInterceptor();
	}

}
