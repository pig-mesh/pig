/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary form, with or without
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

package com.pig4cloud.pigx.bi.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * JimuReport 2.0.0 AI Assistant 与 Spring Framework 7 暂不兼容。
 * <p>
 * 其 AI Service 仍调用已移除的
 * {@code HttpComponentsClientHttpRequestFactory#setConnectTimeout(int)}。
 * 这里仅移除 AI Assistant 相关 Bean，保留 JimuReport 报表核心能力。
 *
 * @author lengleng
 */
@Configuration(proxyBeanMethods = false)
public class JimuAiAssistantCompatibilityConfiguration {

    private static final String AI_PACKAGE_PREFIX = "org.jeecg.modules.jmreport.ai.";

    @Bean
    public static BeanDefinitionRegistryPostProcessor jimuAiAssistantBeanDefinitionPostProcessor() {
        return new BeanDefinitionRegistryPostProcessor() {
            @Override
            public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
                Arrays.stream(registry.getBeanDefinitionNames())
                        .filter(beanName -> isJimuAiAssistantBean(registry, beanName))
                        .forEach(registry::removeBeanDefinition);
            }

            private boolean isJimuAiAssistantBean(BeanDefinitionRegistry registry, String beanName) {
                BeanDefinition beanDefinition = registry.getBeanDefinition(beanName);
                String beanClassName = beanDefinition.getBeanClassName();
                return beanName.startsWith("jimuAiAssistant")
                        || (beanClassName != null && beanClassName.startsWith(AI_PACKAGE_PREFIX));
            }
        };
    }

}
