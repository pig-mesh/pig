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

package com.pig4cloud.pig.common.datasource;

import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.hikaricp.HikariDataSourceCreator;
import com.baomidou.dynamic.datasource.processor.DsJakartaHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsJakartaSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.pig4cloud.pig.common.datasource.config.*;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.expression.BeanFactoryResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态数据源切换配置
 *
 * @author lengleng
 * @date 2020-02-06
 */
@Configuration
@RequiredArgsConstructor
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DataSourceProperties.class)
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
			StringEncryptor stringEncryptor, DataSourceProperties properties) {
		return new JdbcDynamicDataSourceProvider(defaultDataSourceCreator, stringEncryptor, properties);
	}

	/**
	 * 主数据源提供程序
	 * @param defaultDataSourceCreator 默认数据源创建者
	 * @param properties 性能
	 * @return {@link DynamicDataSourceProvider }
	 */
	@Bean
	public DynamicDataSourceProvider masterDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator,
			DataSourceProperties properties) {
		return new MasterDataSourceProvider(defaultDataSourceCreator, properties);
	}

	/**
	 * 获取默认数据源创建器
	 * @param druidDataSourceCreator Druid数据源创建器
	 * @return 默认数据源创建器
	 */
	@Bean
	public DefaultDataSourceCreator defaultDataSourceCreator(HikariDataSourceCreator druidDataSourceCreator) {
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
