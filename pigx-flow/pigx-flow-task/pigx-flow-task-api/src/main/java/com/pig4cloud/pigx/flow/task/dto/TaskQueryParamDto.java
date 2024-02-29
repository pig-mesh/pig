package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户任务查询参数
 */
@Data
public class TaskQueryParamDto {

	/**
	 * 任务执行人
	 */
	private String assign;

	/**
	 * 页码
	 */
	private Integer current;

	/**
	 * 每页的数量
	 */
	private Integer size;

	/**
	 * 流程名称
	 */
	private String processName;

	/**
	 * 任务时间
	 */
	private LocalDateTime[] taskTime;

	/**
	 * 状态
	 */
	private Integer status;

}
