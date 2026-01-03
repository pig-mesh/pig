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

package com.pig4cloud.pigx.common.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 通用Web配置类：提供RestTemplate的Bean配置
 *
 * @author lengleng
 * @date 2025/05/01
 */
@Configuration
public class WebCommonConfiguration {

    /**
     * 创建并配置RestTemplate的Bean方法
     *
     * @return RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * 创建并返回{@link PriorityHeaderInitBinder}的Bean实例。
     *
     * @return PriorityHeaderInitBinder的实例对象
     * @condition 当Web应用程序类型为SERVLET时条件成立
     * @see PriorityHeaderInitBinder
     */
    @Bean
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public PriorityHeaderInitBinder priorityHeaderInitBinder() {
        return new PriorityHeaderInitBinder();
    }

}
