package com.pig4cloud.pigx.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页统计数据传输对象
 * <p>
 * 该DTO用于传输流程系统首页的统计数据，包括待办任务、已发起流程、
 * 抄送任务和已完成任务的数量统计。主要用于首页仪表盘的数据展示，
 * 帮助用户快速了解当前的工作状态和任务情况。
 * </p>
 * 
 * @author pigx
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexPageStatistics {

	/**
	 * 待办任务数量
	 * <p>
	 * 当前用户需要处理的待办任务总数。
	 * 包括所有分配给当前用户但尚未完成的审批任务。
	 * </p>
	 */
	private Long pendingNum;

	/**
	 * 已发起流程数量
	 * <p>
	 * 当前用户发起的所有流程实例总数。
	 * 包括进行中、已完成和已终止的流程。
	 * </p>
	 */
	private Long startedNum;

	/**
	 * 抄送任务数量
	 * <p>
	 * 抄送给当前用户的任务总数。
	 * 这些任务仅供查看，不需要用户进行审批操作。
	 * </p>
	 */
	private Long copyNum;

	/**
	 * 已完成任务数量
	 * <p>
	 * 当前用户已经完成的任务总数。
	 * 包括所有已审批（同意或拒绝）的任务。
	 * </p>
	 */
	private Long completedNum;

}
