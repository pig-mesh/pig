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

package com.pig4cloud.pig.daemon.quartz.config;

import com.pig4cloud.pig.common.core.factory.YamlPropertySourceFactory;
import org.quartz.Calendar;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.quartz.autoconfigure.QuartzProperties;
import org.springframework.boot.quartz.autoconfigure.SchedulerFactoryBeanCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Quartz 调度器配置。
 * <p>
 * 负责装配 {@link SchedulerFactoryBean}、加载 `quartz-config.yml` 中的 Quartz 参数，
 * 并启用本模块的异步任务监听与重复触发保护配置。
 * </p>
 */
@EnableAsync
@Configuration
@PropertySource(value = "classpath:quartz-config.yml", factory = YamlPropertySourceFactory.class)
@ConditionalOnClass({ Scheduler.class, SchedulerFactoryBean.class })
@EnableConfigurationProperties({ QuartzProperties.class, QuartzProtectionProperties.class })
public class PigQuartzConfig {

	private final QuartzProperties properties;

	private final List<SchedulerFactoryBeanCustomizer> customizers;

	private final JobDetail[] jobDetails;

	private final Map<String, Calendar> calendars;

	private final Trigger[] triggers;

	private final ApplicationContext applicationContext;

	/**
	 * 创建 Quartz 配置类。
	 * @param properties Quartz 原生属性配置
	 * @param customizers 调度器自定义器集合
	 * @param jobDetails 预注册 JobDetail 集合
	 * @param calendars Quartz 日历配置
	 * @param triggers 预注册 Trigger 集合
	 * @param applicationContext Spring 应用上下文
	 */
	public PigQuartzConfig(QuartzProperties properties,
			ObjectProvider<List<SchedulerFactoryBeanCustomizer>> customizers, ObjectProvider<JobDetail[]> jobDetails,
			ObjectProvider<Map<String, Calendar>> calendars, ObjectProvider<Trigger[]> triggers,
			ApplicationContext applicationContext) {
		this.properties = properties;
		this.customizers = customizers.getIfAvailable();
		this.jobDetails = jobDetails.getIfAvailable();
		this.calendars = calendars.getIfAvailable();
		this.triggers = triggers.getIfAvailable();
		this.applicationContext = applicationContext;
	}

	/**
	 * 创建 Quartz 调度器工厂。
	 * @return 已注入 Spring JobFactory 和 Quartz 属性的调度器工厂
	 */
	@Bean
	@ConditionalOnMissingBean
	public SchedulerFactoryBean quartzScheduler() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean
			.setJobFactory(new AutowireCapableBeanJobFactory(this.applicationContext.getAutowireCapableBeanFactory()));
		if (!this.properties.getProperties().isEmpty()) {
			schedulerFactoryBean.setQuartzProperties(this.asProperties(this.properties.getProperties()));
		}

		if (this.jobDetails != null && this.jobDetails.length > 0) {
			schedulerFactoryBean.setJobDetails(this.jobDetails);
		}

		if (this.calendars != null && !this.calendars.isEmpty()) {
			schedulerFactoryBean.setCalendars(this.calendars);
		}

		if (this.triggers != null && this.triggers.length > 0) {
			schedulerFactoryBean.setTriggers(this.triggers);
		}

		this.customize(schedulerFactoryBean);
		return schedulerFactoryBean;
	}

	/**
	 * 将 Spring Boot 的 Quartz Map 属性转换为标准 {@link Properties}。
	 * @param source Quartz Map 形式的属性源
	 * @return Quartz 可直接消费的标准属性对象
	 */
	private Properties asProperties(Map<String, String> source) {
		Properties properties = new Properties();
		properties.putAll(source);
		return properties;
	}

	/**
	 * 统一执行所有调度器自定义器。
	 * @param schedulerFactoryBean 当前待定制的调度器工厂
	 */
	private void customize(SchedulerFactoryBean schedulerFactoryBean) {
		if (this.customizers != null) {
			for (SchedulerFactoryBeanCustomizer customizer : this.customizers) {
				customizer.customize(schedulerFactoryBean);
			}
		}

	}

	/**
	 * 通过 {@link SchedulerFactoryBean} 获取最终的 Quartz 调度器实例。
	 * @return Quartz 调度器实例
	 */
	@Bean
	public Scheduler scheduler() {
		return quartzScheduler().getScheduler();
	}

}
