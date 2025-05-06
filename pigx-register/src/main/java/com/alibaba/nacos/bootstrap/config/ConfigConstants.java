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

package com.alibaba.nacos.bootstrap.config;

/**
 * 配置常量接口：用于覆盖Nacos默认配置
 *
 * @author lengleng
 * @date 2025/05/06
 */
public interface ConfigConstants {

	/**
	 * 独立模式系统属性名称
	 */
	String STANDALONE_MODE = "nacos.standalone";

	/**
	 * Nacos控制台端口号配置键
	 */
	String NACOS_CONSOLE_PORT = "nacos.console.port";

	/**
	 * 是否开启认证
	 */
	String AUTH_ENABLED = "nacos.core.auth.enabled";

	/**
	 * 日志目录
	 */
	String LOG_BASEDIR = "server.tomcat.basedir";

	/**
	 * access_log日志开关
	 */
	String LOG_ENABLED = "server.tomcat.accesslog.enabled";
}
