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
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
 * Quartz 定时任务配置类
 *
 * @author lengleng
 * @author 郑健楠
 * @date 2025/05/31
 */
@EnableAsync
@Configuration
@PropertySource(value = "classpath:quartz-config.yml", factory = YamlPropertySourceFactory.class)
@ConditionalOnClass({ Scheduler.class, SchedulerFactoryBean.class })
@EnableConfigurationProperties({ QuartzProperties.class })
public class PigQuartzConfig {

	private final QuartzProperties properties;

	private final List<SchedulerFactoryBeanCustomizer> customizers;

	private final JobDetail[] jobDetails;

	private final Map<String, Calendar> calendars;

	private final Trigger[] triggers;

	private final ApplicationContext applicationContext;

	/**
	 * 构造函数，初始化PigQuartzConfig配置
	 * @param properties Quartz配置属性
	 * @param customizers SchedulerFactoryBean自定义器列表
	 * @param jobDetails JobDetail数组
	 * @param calendars 日历Map
	 * @param triggers 触发器数组
	 * @param applicationContext Spring应用上下文
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
	 * 创建并配置Quartz SchedulerFactoryBean
	 * @return 配置完成的SchedulerFactoryBean实例
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
	 * 将Map转换为Properties对象
	 * @param source 源Map，键值对均为String类型
	 * @return 转换后的Properties对象
	 */
	private Properties asProperties(Map<String, String> source) {
		Properties properties = new Properties();
		properties.putAll(source);
		return properties;
	}

	/**
	 * 自定义SchedulerFactoryBean
	 * @param schedulerFactoryBean 需要自定义的调度器工厂bean
	 */
	private void customize(SchedulerFactoryBean schedulerFactoryBean) {
		if (this.customizers != null) {

			for (SchedulerFactoryBeanCustomizer customizer : this.customizers) {
				customizer.customize(schedulerFactoryBean);
			}
		}

	}

	/**
	 * 通过SchedulerFactoryBean获取Scheduler的实例
	 * @return
	 */
	@Bean
	public Scheduler scheduler() {
		return quartzScheduler().getScheduler();
	}

}
