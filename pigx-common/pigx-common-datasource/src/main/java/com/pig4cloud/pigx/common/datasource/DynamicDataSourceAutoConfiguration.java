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

package com.pig4cloud.pigx.common.datasource;

import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.druid.DruidDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.processor.DsJakartaHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsJakartaSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.pig4cloud.pigx.common.datasource.config.*;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.expression.BeanFactoryResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2020-02-06
 * <p>
 * 动态数据源切换配置
 */
@Configuration
@RequiredArgsConstructor
@Import(DynamicLogConfiguration.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DruidDataSourceProperties.class)
public class DynamicDataSourceAutoConfiguration {

	/**
	 * 获取动态数据源提供者
	 * @param defaultDataSourceCreator 默认数据源创建器
	 * @param stringEncryptor 字符串加密器
	 * @param properties 数据源属性
	 * @return 动态数据源提供者
	 */
	@Bean
	public DynamicDataSourceProvider dynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator,
			StringEncryptor stringEncryptor, DruidDataSourceProperties properties) {
		return new JdbcDynamicDataSourceProvider(defaultDataSourceCreator, stringEncryptor, properties);
	}

	/**
	 * 获取默认数据源创建器
	 * @param druidDataSourceCreator Druid数据源创建器
	 * @return 默认数据源创建器
	 */
	@Bean
	public DefaultDataSourceCreator defaultDataSourceCreator(DruidDataSourceCreator druidDataSourceCreator) {
		DefaultDataSourceCreator defaultDataSourceCreator = new DefaultDataSourceCreator();
		List<DataSourceCreator> creators = new ArrayList<>();
		creators.add(druidDataSourceCreator);
		defaultDataSourceCreator.setCreators(creators);
		return defaultDataSourceCreator;
	}

	/**
	 * 获取数据源处理器
	 * @return 数据源处理器
	 */
	@Bean
	public DsProcessor dsProcessor(BeanFactory beanFactory) {
		DsProcessor lastParamDsProcessor = new LastParamDsProcessor();
		DsProcessor headerProcessor = new DsJakartaHeaderProcessor();
		DsProcessor sessionProcessor = new DsJakartaSessionProcessor();
		DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
		spelExpressionProcessor.setBeanResolver(new BeanFactoryResolver(beanFactory));
		lastParamDsProcessor.setNextProcessor(headerProcessor);
		headerProcessor.setNextProcessor(sessionProcessor);
		sessionProcessor.setNextProcessor(spelExpressionProcessor);
		return lastParamDsProcessor;
	}

	/**
	 * 获取清除TTL数据源过滤器
	 * @return 清除TTL数据源过滤器
	 */
	@Bean
	public ClearTtlDataSourceFilter clearTtlDsFilter() {
		return new ClearTtlDataSourceFilter();
	}

}
