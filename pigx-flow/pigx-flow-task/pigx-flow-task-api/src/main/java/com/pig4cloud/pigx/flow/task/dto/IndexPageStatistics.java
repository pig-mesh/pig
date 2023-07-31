package com.pig4cloud.pigx.flow.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 首页统计数据
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndexPageStatistics {

	/**
	 * 待办数量
	 */
	private Long pendingNum;

	/**
	 * 发起数量
	 */
	private Long startedNum;

	/**
	 * 抄送任务
	 */
	private Long copyNum;

	/**
	 * 完成数量
	 */
	private Long completedNum;

}
