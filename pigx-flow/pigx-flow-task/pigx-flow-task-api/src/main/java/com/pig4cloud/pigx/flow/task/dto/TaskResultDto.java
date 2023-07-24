package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

import java.util.Map;

/**
 * 任务结果对象
 *
 */
@Data
public class TaskResultDto {

	private Boolean currentTask;

	/**
	 * 流程id
	 */
	private String flowId;

	private Node taskNode;

	private String nodeId;

	/**
	 * 实例id
	 */
	private String processInstanceId;

	/**
	 * 委派状态
	 */
	private String delegationState;

	/**
	 * 是否允许继续委派
	 */
	private Boolean delegate;

	/**
	 * 所有变量
	 */
	private Map<String, Object> variableAll;

}
