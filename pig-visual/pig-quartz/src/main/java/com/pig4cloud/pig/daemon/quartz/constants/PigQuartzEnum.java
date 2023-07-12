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
 * @author 郑健楠
 *
 * <p>
 * 定时任务枚举
 */
@Getter
@AllArgsConstructor
public enum PigQuartzEnum {

	/**
	 * 错失执行策略默认
	 */
	MISFIRE_DEFAULT("0", "默认"),

	/**
	 * 错失执行策略-立即执行错失任务
	 */
	MISFIRE_IGNORE_MISFIRES("1", "立即执行错失任务"),

	/**
	 * 错失执行策略-触发一次执行周期执行
	 */
	MISFIRE_FIRE_AND_PROCEED("2", "触发一次执行周期执行"),

	/**
	 * 错失执行策略-不触发执行周期执行
	 */
	MISFIRE_DO_NOTHING("3", "不触发周期执行"),

	/**
	 * 任务详细信息的key
	 */
	SCHEDULE_JOB_KEY("scheduleJob", "获取任务详细信息的key"),

	/**
	 * JOB执行状态：0执行成功
	 */
	JOB_LOG_STATUS_SUCCESS("0", "执行成功"),
	/**
	 * JOB执行状态：1执行失败
	 */
	JOB_LOG_STATUS_FAIL("1", "执行失败"),

	/**
	 * JOB状态：1已发布
	 */
	JOB_STATUS_RELEASE("1", "已发布"),
	/**
	 * JOB状态：2运行中
	 */
	JOB_STATUS_RUNNING("2", "运行中"),
	/**
	 * JOB状态：3暂停
	 */
	JOB_STATUS_NOT_RUNNING("3", "暂停"),
	/**
	 * JOB状态：4删除
	 */
	JOB_STATUS_DEL("4", "删除");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
