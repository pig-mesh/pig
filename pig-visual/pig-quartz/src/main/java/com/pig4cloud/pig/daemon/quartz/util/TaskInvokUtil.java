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

package com.pig4cloud.pig.daemon.quartz.util;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.daemon.quartz.constants.PigQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.entity.SysJobLog;
import com.pig4cloud.pig.daemon.quartz.event.SysJobLogEvent;
import com.pig4cloud.pig.daemon.quartz.service.SysJobService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

/**
 * 定时任务反射工具类
 *
 * @author 郑健楠
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskInvokUtil {

	private final ApplicationEventPublisher publisher;

	private final SysJobService sysJobService;

	@SneakyThrows
	public void invokMethod(SysJob sysJob, Trigger trigger) {

		// 执行开始时间
		long startTime;
		// 执行结束时间
		long endTime;
		// 获取执行开始时间
		startTime = System.currentTimeMillis();
		// 更新定时任务表内的状态、执行时间、上次执行时间、下次执行时间等信息
		SysJob updateSysjob = new SysJob();
		updateSysjob.setJobId(sysJob.getJobId());
		// 日志
		SysJobLog sysJobLog = new SysJobLog();
		sysJobLog.setJobId(sysJob.getJobId());
		sysJobLog.setJobName(sysJob.getJobName());
		sysJobLog.setJobGroup(sysJob.getJobGroup());
		sysJobLog.setJobOrder(sysJob.getJobOrder());
		sysJobLog.setJobType(sysJob.getJobType());
		sysJobLog.setExecutePath(sysJob.getExecutePath());
		sysJobLog.setClassName(sysJob.getClassName());
		sysJobLog.setMethodName(sysJob.getMethodName());
		sysJobLog.setMethodParamsValue(sysJob.getMethodParamsValue());
		sysJobLog.setCronExpression(sysJob.getCronExpression());
		try {
			// 执行任务
			ITaskInvok iTaskInvok = TaskInvokFactory.getInvoker(sysJob.getJobType());
			// 确保租户上下文有值，使得当前线程中的多租户特性生效。
			iTaskInvok.invokMethod(sysJob);
			// 记录成功状态
			sysJobLog.setJobMessage(PigQuartzEnum.JOB_LOG_STATUS_SUCCESS.getDescription());
			sysJobLog.setJobLogStatus(PigQuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
			// 任务表信息更新
			updateSysjob.setJobExecuteStatus(PigQuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
		}
		catch (Throwable e) {
			log.error("定时任务执行失败，任务名称：{}；任务组名：{}，cron执行表达式：{}，执行时间：{}", sysJob.getJobName(), sysJob.getJobGroup(),
					sysJob.getCronExpression(), new Date());
			// 记录失败状态
			sysJobLog.setJobMessage(PigQuartzEnum.JOB_LOG_STATUS_FAIL.getDescription());
			sysJobLog.setJobLogStatus(PigQuartzEnum.JOB_LOG_STATUS_FAIL.getType());
			sysJobLog.setExceptionInfo(StrUtil.sub(e.getMessage(), 0, 2000));
			// 任务表信息更新
			updateSysjob.setJobExecuteStatus(PigQuartzEnum.JOB_LOG_STATUS_FAIL.getType());
		}
		finally {
			// 记录执行时间 立刻执行使用的是simpleTeigger
			if (trigger instanceof CronTrigger) {
				updateSysjob
					.setStartTime(trigger.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
				updateSysjob.setPreviousTime(
						trigger.getPreviousFireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
				updateSysjob.setNextTime(
						trigger.getNextFireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
			}
			// 记录执行时长
			endTime = System.currentTimeMillis();
			sysJobLog.setExecuteTime(String.valueOf(endTime - startTime));

			publisher.publishEvent(new SysJobLogEvent(sysJobLog));
			sysJobService.updateById(updateSysjob);
		}
	}

}
