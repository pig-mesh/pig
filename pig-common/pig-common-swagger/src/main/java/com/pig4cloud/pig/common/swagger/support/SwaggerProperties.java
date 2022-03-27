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
package com.pig4cloud.pig.common.swagger.support;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SwaggerProperties
 *
 * @author lengleng
 * @date 2018/7/25 14:00
 */
@Data
@ConfigurationProperties("swagger")
public class SwaggerProperties {

	/**
	 * 是否开启swagger
	 */
	private Boolean enabled = true;

	/**
	 * swagger会解析的包路径
	 **/
	private String basePackage = "";

	/**
	 * swagger会解析的url规则
	 **/
	private List<String> basePath = new ArrayList<>();

	/**
	 * 在basePath基础上需要排除的url规则
	 **/
	private List<String> excludePath = new ArrayList<>();

	/**
	 * 需要排除的服务
	 */
	private List<String> ignoreProviders = new ArrayList<>();

	/**
	 * 标题
	 **/
	private String title = "";

	/**
	 * 网关
	 */
	private String gateway;

	/**
	 * 获取token
	 */
	private String tokenUrl;

	/**
	 * 作用域
	 */
	private String scope;

	private Map<String, String> services;

}
