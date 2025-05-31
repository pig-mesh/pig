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

package com.pig4cloud.pig.common.file.oss;

import com.pig4cloud.pig.common.file.core.FileProperties;
import com.pig4cloud.pig.common.file.core.FileTemplate;
import com.pig4cloud.pig.common.file.oss.http.OssEndpoint;
import com.pig4cloud.pig.common.file.oss.service.OssTemplate;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * AWS 对象存储自动配置类
 *
 * @author lengleng
 * @author 858695266
 * @date 2025/05/31
 */
@AllArgsConstructor
public class OssAutoConfiguration {

	private final FileProperties properties;

	/**
	 * 创建OssTemplate Bean
	 * @return 文件模板实例
	 * @ConditionalOnMissingBean 当容器中不存在OssTemplate Bean时创建
	 * @ConditionalOnProperty 当配置项file.oss.enable为true时生效
	 */
	@Bean
	@Primary
	@ConditionalOnMissingBean(OssTemplate.class)
	@ConditionalOnProperty(name = "file.oss.enable", havingValue = "true")
	public FileTemplate ossTemplate() {
		return new OssTemplate(properties);
	}

	/**
	 * 创建OssEndpoint Bean
	 * @param template OssTemplate实例
	 * @return OssEndpoint实例
	 * @ConditionalOnMissingBean 当容器中不存在该类型Bean时创建
	 * @ConditionalOnProperty 当配置项file.oss.info为true时生效
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(name = "file.oss.info", havingValue = "true")
	public OssEndpoint ossEndpoint(OssTemplate template) {
		return new OssEndpoint(template);
	}

}
