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

package com.pig4cloud.pig.daemon.quartz.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lengleng
 * @date 2019-03-14
 * <p>
 * 任务类型枚举
 */
@Getter
@AllArgsConstructor
public enum JobTypeQuartzEnum {

	/**
	 * 反射java类
	 */
	JAVA("1", "反射java类"),

	/**
	 * spring bean 的方式
	 */
	SPRING_BEAN("2", "spring bean容器实例"),

	/**
	 * rest 调用
	 */
	REST("3", "rest调用"),

	/**
	 * jar
	 */
	JAR("4", "jar调用");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
