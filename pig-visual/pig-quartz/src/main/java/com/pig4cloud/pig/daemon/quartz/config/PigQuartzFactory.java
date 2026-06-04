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

import com.pig4cloud.pig.daemon.quartz.constants.PigQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.entity.SysJobLog;
import com.pig4cloud.pig.daemon.quartz.event.SysJobLogEvent;
import com.pig4cloud.pig.daemon.quartz.support.QuartzExecutionMetadata;
import com.pig4cloud.pig.daemon.quartz.support.QuartzProtectionSupport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Quartz 任务工厂。
 * <p>
 * 该工厂是 Quartz 真正进入业务逻辑前的第一道关口， 负责构建执行元数据、拦截同一触发点的重复回调，并把合法执行请求继续分发到异步执行链路。
 * </p>
 *
 * @author 郑健楠
 */
@Slf4j
@DisallowConcurrentExecution
public class PigQuartzFactory implements Job {

	@Autowired
	private PigQuartzInvokeFactory pigQuartzInvokeFactory;

	@Autowired
	private QuartzProtectionSupport quartzProtectionSupport;

	@Autowired
	private ApplicationEventPublisher publisher;

	/**
	 * 执行 Quartz 调度任务。
	 * @param jobExecutionContext Quartz 原始执行上下文，包含任务数据与触发器信息
	 */
	@Override
	@SneakyThrows
	public void execute(JobExecutionContext jobExecutionContext) {
		SysJob sysJob = (SysJob) jobExecutionContext.getMergedJobDataMap()
			.get(PigQuartzEnum.SCHEDULE_JOB_KEY.getType());
		QuartzExecutionMetadata executionMetadata = quartzProtectionSupport.buildExecutionMetadata(jobExecutionContext,
				jobExecutionContext.getTrigger());
		if (quartzProtectionSupport.isProtectionEnabled()) {
			QuartzProtectionSupport.ProtectionLock protectionLock = quartzProtectionSupport
				.tryAcquireFireDedupLock(sysJob, executionMetadata);
			if (!protectionLock.isAcquired()) {
				publishSkippedLog(sysJob, executionMetadata);
				return;
			}
		}
		pigQuartzInvokeFactory.init(sysJob, jobExecutionContext.getTrigger(), executionMetadata);
	}

	/**
	 * 记录“同一触发点重复回调”场景下的跳过日志。
	 * @param sysJob 当前任务配置
	 * @param executionMetadata 当前触发对应的执行元数据
	 */
	private void publishSkippedLog(SysJob sysJob, QuartzExecutionMetadata executionMetadata) {
		if (!quartzProtectionSupport.shouldLogSkipped()) {
			return;
		}
		publisher.publishEvent(new SysJobLogEvent(buildSkippedLog(sysJob, executionMetadata)));
	}

	/**
	 * 构造重复触发场景下的任务日志。
	 * @param sysJob 当前任务配置
	 * @param executionMetadata 当前触发对应的执行元数据
	 * @return 可直接落库的跳过日志对象
	 */
	private SysJobLog buildSkippedLog(SysJob sysJob, QuartzExecutionMetadata executionMetadata) {
		return SysJobLog.builder()
			.jobId(sysJob.getJobId())
			.jobName(sysJob.getJobName())
			.jobGroup(sysJob.getJobGroup())
			.jobOrder(sysJob.getJobOrder())
			.jobType(sysJob.getJobType())
			.executePath(sysJob.getExecutePath())
			.className(sysJob.getClassName())
			.methodName(sysJob.getMethodName())
			.methodParamsValue(sysJob.getMethodParamsValue())
			.cronExpression(sysJob.getCronExpression())
			.jobMessage(PigQuartzEnum.JOB_DEDUP_STATUS_DUPLICATE_FIRE.getDescription())
			.jobLogStatus(PigQuartzEnum.JOB_LOG_STATUS_SUCCESS.getType())
			.dedupStatus(PigQuartzEnum.JOB_DEDUP_STATUS_DUPLICATE_FIRE.getType())
			.scheduledFireTime(executionMetadata.scheduledFireTime())
			.fireInstanceId(executionMetadata.fireInstanceId())
			.build();
	}

}
