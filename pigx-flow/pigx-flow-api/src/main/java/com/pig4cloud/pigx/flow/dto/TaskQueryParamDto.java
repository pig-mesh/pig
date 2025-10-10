package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户任务查询参数数据传输对象
 * <p>
 * 该DTO用于查询用户任务列表时传递查询条件。
 * 支持按执行人、流程名称、时间范围、任务状态等条件进行查询，
 * 并支持分页功能。主要用于任务列表、待办事项等功能。
 * </p>
 * 
 * @author pigx
 */
@Data
public class TaskQueryParamDto {

	/**
	 * 任务执行人
	 * <p>
	 * 用于查询指定用户的任务。
	 * 可以是用户ID或用户名，取决于系统实现。
	 * 为空时查询所有用户的任务。
	 * </p>
	 */
	private String assign;

	/**
	 * 当前页码
	 * <p>
	 * 分页查询的页码，从1开始。
	 * 用于实现分页功能。
	 * </p>
	 */
	private Integer current;

	/**
	 * 每页记录数
	 * <p>
	 * 分页查询时每页显示的记录数量。
	 * 用于控制返回数据的大小。
	 * </p>
	 */
	private Integer size;

	/**
	 * 流程名称
	 * <p>
	 * 用于按流程名称过滤任务。
	 * 支持模糊查询，如输入"请假"可查询所有请假相关流程的任务。
	 * </p>
	 */
	private String processName;

	/**
	 * 任务时间范围
	 * <p>
	 * 用于按任务创建时间范围查询。
	 * 数组包含两个元素：[开始时间, 结束时间]。
	 * 可用于查询特定时间段内的任务。
	 * </p>
	 */
	private LocalDateTime[] taskTime;

	/**
	 * 任务状态
	 * <p>
	 * 用于按任务状态过滤：
	 * - 0: 待处理
	 * - 1: 已完成
	 * - 2: 已拒绝
	 * - 3: 已撤回
	 * - 4: 已转办
	 * 为空时查询所有状态的任务。
	 * </p>
	 */
	private Integer status;

}
