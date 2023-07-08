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

package com.pig4cloud.pig.daemon.quartz.event;

import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.util.TaskInvokUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Trigger;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author frwcloud 异步监听定时任务事件
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysJobListener {

	private final TaskInvokUtil taskInvokUtil;

	@Async
	@Order
	@EventListener(SysJobEvent.class)
	public void comSysJob(SysJobEvent event) {
		SysJob sysJob = event.getSysJob();
		Trigger trigger = event.getTrigger();
		taskInvokUtil.invokMethod(sysJob, trigger);
	}

}
