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
import com.pig4cloud.pig.daemon.quartz.support.QuartzExecutionMetadata;
import com.pig4cloud.pig.daemon.quartz.support.QuartzProtectionSupport;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.Trigger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/**
 * 定时任务执行工具类。
 * <p>
 * 负责真正调用任务执行器、记录执行日志，并在调用前后挂载 Quartz 防重保护相关的运行锁与元数据。
 * </p>
 *
 * @author 郑健楠
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskInvokUtil {

	private final ApplicationEventPublisher publisher;

	private final SysJobService sysJobService;

	private final QuartzProtectionSupport quartzProtectionSupport;

	/**
	 * 使用兼容旧接口的方式执行定时任务。
	 * @param sysJob 当前任务配置
	 * @param trigger 当前触发器
	 */
	@SneakyThrows
	public void invokMethod(SysJob sysJob, Trigger trigger) {
		invokMethod(sysJob, trigger, QuartzExecutionMetadata.of(new Date(), null, trigger, "QUARTZ"));
	}

	/**
	 * 执行定时任务，并记录本次任务的日志与执行状态。
	 * @param sysJob 当前任务配置，不允许为空
	 * @param trigger 当前触发器，可为空
	 * @param executionMetadata 当前调度的执行元数据，不允许为空
	 */
	@SneakyThrows
	public void invokMethod(SysJob sysJob, Trigger trigger, QuartzExecutionMetadata executionMetadata) {
		Objects.requireNonNull(sysJob, "sysJob must not be null");
		Objects.requireNonNull(executionMetadata, "executionMetadata must not be null");

		// 执行开始时间
		long startTime;
		// 执行结束时间
		long endTime;
		// 获取执行开始时间
		startTime = System.currentTimeMillis();
		// 更新定时任务表内的状态、执行时间、上次执行时间、下次执行时间等信息
		SysJob updateSysjob = createUpdateJob(sysJob);
		SysJobLog sysJobLog = buildBaseJobLog(sysJob, executionMetadata);
		QuartzProtectionSupport.ProtectionLock runningLock = quartzProtectionSupport.tryAcquireRunningLock(sysJob);
		try {
			if (!runningLock.isAcquired()) {
				markRunningSkipped(sysJobLog);
				return;
			}
			// 执行任务
			ITaskInvok iTaskInvok = TaskInvokFactory.getInvoker(sysJob.getJobType());
			iTaskInvok.invokMethod(sysJob);

			// 记录成功状态
			markSuccess(sysJobLog, updateSysjob);
		}
		catch (Throwable e) {
			log.error("定时任务执行失败，任务名称：{}；任务组名：{}，cron执行表达式：{}，执行时间：{}", sysJob.getJobName(), sysJob.getJobGroup(),
					sysJob.getCronExpression(), new Date());
			markFailure(sysJobLog, updateSysjob, e);
		}
		finally {
			fillTriggerTimes(updateSysjob, trigger);
			endTime = System.currentTimeMillis();
			sysJobLog.setExecuteTime(String.valueOf(endTime - startTime));
			publishLogAndUpdateJob(sysJobLog, updateSysjob);
			releaseRunningLockIfNecessary(runningLock);
		}
	}

	/**
	 * 创建任务状态更新对象。
	 * @param sysJob 当前任务配置
	 * @return 仅包含主键的状态更新对象
	 */
	private SysJob createUpdateJob(SysJob sysJob) {
		SysJob updateSysjob = new SysJob();
		updateSysjob.setJobId(sysJob.getJobId());
		return updateSysjob;
	}

	/**
	 * 构造本次任务的基础日志对象。
	 * @param sysJob 当前任务配置
	 * @param executionMetadata 当前调度的执行元数据
	 * @return 已填充任务基本信息的日志对象
	 */
	private SysJobLog buildBaseJobLog(SysJob sysJob, QuartzExecutionMetadata executionMetadata) {
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
		sysJobLog.setScheduledFireTime(executionMetadata.scheduledFireTime());
		sysJobLog.setFireInstanceId(executionMetadata.fireInstanceId());
		sysJobLog.setDedupStatus(PigQuartzEnum.JOB_DEDUP_STATUS_NORMAL.getType());
		return sysJobLog;
	}

	/**
	 * 标记任务因运行锁未获取而跳过。
	 * @param sysJobLog 当前任务日志对象
	 */
	private void markRunningSkipped(SysJobLog sysJobLog) {
		sysJobLog.setJobMessage(PigQuartzEnum.JOB_DEDUP_STATUS_RUNNING_SKIP.getDescription());
		sysJobLog.setJobLogStatus(PigQuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
		sysJobLog.setDedupStatus(PigQuartzEnum.JOB_DEDUP_STATUS_RUNNING_SKIP.getType());
	}

	/**
	 * 标记任务成功执行。
	 * @param sysJobLog 当前任务日志对象
	 * @param updateSysjob 当前任务状态更新对象
	 */
	private void markSuccess(SysJobLog sysJobLog, SysJob updateSysjob) {
		sysJobLog.setJobMessage(PigQuartzEnum.JOB_LOG_STATUS_SUCCESS.getDescription());
		sysJobLog.setJobLogStatus(PigQuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
		updateSysjob.setJobExecuteStatus(PigQuartzEnum.JOB_LOG_STATUS_SUCCESS.getType());
	}

	/**
	 * 标记任务执行失败。
	 * @param sysJobLog 当前任务日志对象
	 * @param updateSysjob 当前任务状态更新对象
	 * @param throwable 本次执行捕获到的异常
	 */
	private void markFailure(SysJobLog sysJobLog, SysJob updateSysjob, Throwable throwable) {
		sysJobLog.setJobMessage(PigQuartzEnum.JOB_LOG_STATUS_FAIL.getDescription());
		sysJobLog.setJobLogStatus(PigQuartzEnum.JOB_LOG_STATUS_FAIL.getType());
		sysJobLog.setExceptionInfo(StrUtil.sub(throwable.getMessage(), 0, 2000));
		updateSysjob.setJobExecuteStatus(PigQuartzEnum.JOB_LOG_STATUS_FAIL.getType());
	}

	/**
	 * 根据 Cron 触发器回填任务时间信息。
	 * @param updateSysjob 当前任务状态更新对象
	 * @param trigger 当前触发器，可为空
	 */
	private void fillTriggerTimes(SysJob updateSysjob, Trigger trigger) {
		if (!(trigger instanceof CronTrigger)) {
			return;
		}
		updateSysjob.setStartTime(trigger.getStartTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		updateSysjob.setPreviousTime(
				trigger.getPreviousFireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		updateSysjob
			.setNextTime(trigger.getNextFireTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
	}

	/**
	 * 发布任务日志并更新任务执行状态。
	 * @param sysJobLog 当前任务日志对象
	 * @param updateSysjob 当前任务状态更新对象
	 */
	private void publishLogAndUpdateJob(SysJobLog sysJobLog, SysJob updateSysjob) {
		publisher.publishEvent(new SysJobLogEvent(sysJobLog));
		sysJobService.updateById(updateSysjob);
	}

	/**
	 * 按需释放运行锁。
	 * @param runningLock 当前任务持有的运行锁句柄
	 */
	private void releaseRunningLockIfNecessary(QuartzProtectionSupport.ProtectionLock runningLock) {
		if (runningLock.isAcquired()) {
			quartzProtectionSupport.releaseRunningLock(runningLock);
		}
	}

}
