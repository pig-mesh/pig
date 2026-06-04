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

import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.event.SysJobEvent;
import com.pig4cloud.pig.daemon.quartz.support.QuartzExecutionMetadata;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.quartz.Trigger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * Quartz 调用事件发布器。
 * <p>
 * 负责把 Quartz 工厂线程中的任务执行请求转换为 Spring 事件， 以便后续由异步监听器统一处理真实业务调用。
 * </p>
 */
@Slf4j
@Aspect
@Service
@AllArgsConstructor
public class PigQuartzInvokeFactory {

	private final ApplicationEventPublisher publisher;

	/**
	 * 发布任务执行事件。
	 * @param sysJob 当前任务配置
	 * @param trigger 当前 Quartz 触发器
	 * @param executionMetadata 当前触发对应的执行元数据
	 */
	@SneakyThrows
	void init(SysJob sysJob, Trigger trigger, QuartzExecutionMetadata executionMetadata) {
		publisher.publishEvent(new SysJobEvent(sysJob, trigger, executionMetadata));
	}

}
