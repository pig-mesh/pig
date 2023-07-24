package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

import java.util.List;

/**
 * 分组条件类
 */
@Data
public class GroupCondition {

	/**
	 * 是否并行
	 */
	private Boolean mode;

	/**
	 * 条件列表
	 */
	private List<Condition> conditionList;

}
