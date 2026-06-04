package com.pig4cloud.pig.daemon.quartz.support;

import org.quartz.Trigger;

import java.util.Date;

/**
 * Quartz 执行上下文元数据。
 * <p>
 * 用于在 Quartz 工厂、异步监听器和真实任务执行器之间传递同一轮调度的关键上下文， 避免重复从 {@code JobExecutionContext}
 * 中取值，也便于记录排查日志。
 * </p>
 */
public record QuartzExecutionMetadata(Date scheduledFireTime, String fireInstanceId, String triggerType,
		String dedupSource) {

	/**
	 * 根据 Quartz 原生触发器构建执行元数据。
	 * @param scheduledFireTime Quartz 计划触发时间，为空时由调用方提前兜底
	 * @param fireInstanceId Quartz 触发实例ID，可为空
	 * @param trigger 当前触发器实例，可为空
	 * @param dedupSource 去重来源标识，用于后续日志排查
	 * @return 封装后的 Quartz 执行元数据
	 */
	public static QuartzExecutionMetadata of(Date scheduledFireTime, String fireInstanceId, Trigger trigger,
			String dedupSource) {
		String resolvedTriggerType = trigger == null ? "UNKNOWN" : trigger.getClass().getSimpleName();
		return new QuartzExecutionMetadata(scheduledFireTime, fireInstanceId, resolvedTriggerType, dedupSource);
	}

}
