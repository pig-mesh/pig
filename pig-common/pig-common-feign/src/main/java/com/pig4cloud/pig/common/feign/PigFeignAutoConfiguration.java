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

package com.pig4cloud.pig.common.feign;

import com.pig4cloud.pig.common.feign.endpoint.FeignClientEndpoint;
import com.pig4cloud.pig.common.feign.core.PigFeignInnerRequestInterceptor;
import com.pig4cloud.pig.common.feign.core.PigFeignRequestCloseInterceptor;
import feign.Feign;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.PigFeignClientsRegistrar;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author lengleng
 * @date 2020/2/8
 * <p>
 * feign 自动化配置
 */
@Configuration
@ConditionalOnClass(Feign.class)
@Import(PigFeignClientsRegistrar.class)
@AutoConfigureAfter(EnableFeignClients.class)
public class PigFeignAutoConfiguration {

    /**
     * feign actuator endpoint
     * @param context
     * @return FeignClientEndpoint
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnAvailableEndpoint
    public FeignClientEndpoint feignClientEndpoint(ApplicationContext context) {
        return new FeignClientEndpoint(context);
    }

    /**
     * add http connection close header
     *
     * @return PigFeignRequestCloseInterceptor
     */
    @Bean
    public PigFeignRequestCloseInterceptor pigFeignRequestCloseInterceptor() {
        return new PigFeignRequestCloseInterceptor();
    }

    /**
     * add inner request header
     *
     * @return PigFeignInnerRequestInterceptor
     */
    @Bean
    public PigFeignInnerRequestInterceptor pigFeignInnerRequestInterceptor() {
        return new PigFeignInnerRequestInterceptor();
    }
}
