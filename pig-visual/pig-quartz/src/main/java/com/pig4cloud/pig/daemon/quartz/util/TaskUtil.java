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

import com.pig4cloud.pig.daemon.quartz.config.PigQuartzFactory;
import com.pig4cloud.pig.daemon.quartz.constants.PigQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

/**
 * 定时任务工具类，提供定时任务的增删改查及状态管理功能
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Slf4j
@Component
public class TaskUtil {

	/**
	 * 获取定时任务的唯一key
	 * @param sysjob 定时任务信息
	 * @return 定时任务的唯一key
	 */
	public static JobKey getJobKey(SysJob sysjob) {
		return JobKey.jobKey(sysjob.getJobName(), sysjob.getJobGroup());
	}

	/**
	 * 获取定时任务触发器的唯一键
	 * @param sysjob 定时任务信息
	 * @return 定时任务触发器键
	 */
	public static TriggerKey getTriggerKey(SysJob sysjob) {
		return TriggerKey.triggerKey(sysjob.getJobName(), sysjob.getJobGroup());
	}

	/**
	 * 添加或更新定时任务
	 * @param sysjob 任务信息
	 * @param scheduler 调度器
	 */
	public void addOrUpateJob(SysJob sysjob, Scheduler scheduler) {
		CronTrigger trigger = null;
		try {
			JobKey jobKey = getJobKey(sysjob);
			// 获得触发器
			TriggerKey triggerKey = getTriggerKey(sysjob);
			trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 判断触发器是否存在（如果存在说明之前运行过但是在当前被禁用了，如果不存在说明一次都没运行过）
			if (trigger == null) {
				// 新建一个工作任务 指定任务类型为串接进行的
				JobDetail jobDetail = JobBuilder.newJob(PigQuartzFactory.class).withIdentity(jobKey).build();
				// 将任务信息添加到任务信息中
				jobDetail.getJobDataMap().put(PigQuartzEnum.SCHEDULE_JOB_KEY.getType(), sysjob);
				// 将cron表达式进行转换
				CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sysjob.getCronExpression());
				cronScheduleBuilder = this.handleCronScheduleMisfirePolicy(sysjob, cronScheduleBuilder);
				// 创建触发器并将cron表达式对象给塞入
				trigger = TriggerBuilder.newTrigger()
					.withIdentity(triggerKey)
					.withSchedule(cronScheduleBuilder)
					.build();
				// 在调度器中将触发器和任务进行组合
				scheduler.scheduleJob(jobDetail, trigger);
			}
			else {
				CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(sysjob.getCronExpression());
				cronScheduleBuilder = this.handleCronScheduleMisfirePolicy(sysjob, cronScheduleBuilder);
				// 按照新的规则进行
				trigger = trigger.getTriggerBuilder()
					.withIdentity(triggerKey)
					.withSchedule(cronScheduleBuilder)
					.build();
				// 将任务信息更新到任务信息中
				trigger.getJobDataMap().put(PigQuartzEnum.SCHEDULE_JOB_KEY.getType(), sysjob);
				// 重启
				scheduler.rescheduleJob(triggerKey, trigger);
			}
			// 如任务状态为暂停
			if (sysjob.getJobStatus().equals(PigQuartzEnum.JOB_STATUS_NOT_RUNNING.getType())) {
				this.pauseJob(sysjob, scheduler);
			}
		}
		catch (SchedulerException e) {
			log.error("添加或更新定时任务，失败信息：{}", e.getMessage());
		}
	}

	/**
	 * 立即执行一次任务
	 * @param scheduler 调度器
	 * @param sysJob 任务信息
	 * @return 任务是否执行成功
	 */
	public static boolean runOnce(Scheduler scheduler, SysJob sysJob) {
		try {
			// 参数
			JobDataMap dataMap = new JobDataMap();
			dataMap.put(PigQuartzEnum.SCHEDULE_JOB_KEY.getType(), sysJob);

			scheduler.triggerJob(getJobKey(sysJob), dataMap);
		}
		catch (SchedulerException e) {
			log.error("立刻执行定时任务，失败信息：{}", e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * 暂停定时任务
	 * @param sysjob 任务信息
	 * @param scheduler 任务调度器
	 */
	public void pauseJob(SysJob sysjob, Scheduler scheduler) {
		try {
			if (scheduler != null) {
				scheduler.pauseJob(getJobKey(sysjob));
			}
		}
		catch (SchedulerException e) {
			log.error("暂停任务失败，失败信息：{}", e.getMessage());
		}

	}

	/**
	 * 恢复定时任务
	 * @param sysjob 任务信息
	 * @param scheduler 任务调度器
	 */
	public void resumeJob(SysJob sysjob, Scheduler scheduler) {
		try {
			if (scheduler != null) {
				scheduler.resumeJob(getJobKey(sysjob));
			}
		}
		catch (SchedulerException e) {
			log.error("恢复任务失败，失败信息：{}", e.getMessage());
		}

	}

	/**
	 * 移除定时任务
	 * @param sysjob 定时任务信息
	 * @param scheduler 任务调度器
	 */
	public void removeJob(SysJob sysjob, Scheduler scheduler) {
		try {
			if (scheduler != null) {
				// 停止触发器
				scheduler.pauseTrigger(getTriggerKey(sysjob));
				// 移除触发器
				scheduler.unscheduleJob(getTriggerKey(sysjob));
				// 删除任务
				scheduler.deleteJob(getJobKey(sysjob));
			}
		}
		catch (Exception e) {
			log.error("移除定时任务失败，失败信息：{}", e.getMessage());
		}
	}

	/**
	 * 启动所有运行中的定时任务
	 * @param scheduler 调度器实例
	 * @throws SchedulerException 当启动任务失败时抛出异常
	 */
	public void startJobs(Scheduler scheduler) {
		try {
			if (scheduler != null) {
				scheduler.resumeAll();
			}
		}
		catch (SchedulerException e) {
			log.error("启动所有运行定时任务失败，失败信息：{}", e.getMessage());
		}
	}

	/**
	 * 暂停所有运行中的定时任务
	 * @param scheduler 调度器实例
	 * @throws Exception 暂停任务过程中可能抛出的异常
	 */
	public void pauseJobs(Scheduler scheduler) {
		try {
			if (scheduler != null) {
				scheduler.pauseAll();
			}
		}
		catch (Exception e) {
			log.error("暂停所有运行定时任务失败，失败信息：{}", e.getMessage());
		}
	}

	/**
	 * 根据任务配置处理错失执行策略
	 * @param sysJob 任务信息
	 * @param cronScheduleBuilder 原始Cron调度构建器
	 * @return 处理后的Cron调度构建器
	 */
	private CronScheduleBuilder handleCronScheduleMisfirePolicy(SysJob sysJob,
			CronScheduleBuilder cronScheduleBuilder) {
		if (PigQuartzEnum.MISFIRE_DEFAULT.getType().equals(sysJob.getMisfirePolicy())) {
			return cronScheduleBuilder;
		}
		else if (PigQuartzEnum.MISFIRE_IGNORE_MISFIRES.getType().equals(sysJob.getMisfirePolicy())) {
			return cronScheduleBuilder.withMisfireHandlingInstructionIgnoreMisfires();
		}
		else if (PigQuartzEnum.MISFIRE_FIRE_AND_PROCEED.getType().equals(sysJob.getMisfirePolicy())) {
			return cronScheduleBuilder.withMisfireHandlingInstructionFireAndProceed();
		}
		else if (PigQuartzEnum.MISFIRE_DO_NOTHING.getType().equals(sysJob.getMisfirePolicy())) {
			return cronScheduleBuilder.withMisfireHandlingInstructionDoNothing();
		}
		else {
			return cronScheduleBuilder;
		}
	}

	/**
	 * 校验cron表达式是否合法
	 * @param cronExpression 待校验的cron表达式
	 * @return true表示合法，false表示不合法
	 */
	public boolean isValidCron(String cronExpression) {
		return CronExpression.isValidExpression(cronExpression);
	}

}
