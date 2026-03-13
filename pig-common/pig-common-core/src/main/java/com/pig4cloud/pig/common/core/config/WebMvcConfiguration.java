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

package com.pig4cloud.pig.common.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 通用Web配置类：提供RestTemplate和异步任务执行器的Bean配置
 *
 * @author lengleng
 * @date 2025/05/01
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@EnableConfigurationProperties(TaskExecutionProperties.class)
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final TaskExecutionProperties taskExecutionProperties;

    public WebMvcConfiguration(TaskExecutionProperties taskExecutionProperties) {
        this.taskExecutionProperties = taskExecutionProperties;
    }

    /**
     * 异步任务执行器，用于处理Spring MVC异步请求和Flowable异步任务
     * <p>
     * 配置项通过 spring.task.execution.* 进行配置
     * Bean名称为 applicationTaskExecutor，符合Spring Boot默认命名约定
     * </p>
     *
     * @return AsyncTaskExecutor实例
     */
    @Bean
    @Primary
    public AsyncTaskExecutor applicationAsyncTaskExecutor() {
        TaskExecutionProperties.Pool pool = taskExecutionProperties.getPool();
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(pool.getCoreSize());
        executor.setMaxPoolSize(pool.getMaxSize());
        executor.setQueueCapacity(pool.getQueueCapacity());
        executor.setKeepAliveSeconds((int) pool.getKeepAlive().toSeconds());
        executor.setThreadNamePrefix(taskExecutionProperties.getThreadNamePrefix());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.initialize();
        return executor;
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setTaskExecutor(applicationAsyncTaskExecutor());
    }

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
    public PriorityHeaderInitBinder priorityHeaderInitBinder() {
        return new PriorityHeaderInitBinder();
    }

}
