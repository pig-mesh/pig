package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

/**
 * 流程节点记录-执行人
 */
@Data
public class ProcessNodeRecordAssignUserParamDto {

	/**
	 * 流程id (process id)
	 */
	private String flowId;

	/**
	 * 流程实例id (process instance id)
	 */
	private String processInstanceId;

	/**
	 * 表单数据 (form data)
	 */
	private String data;

	/**
	 * 本地数据 (local data)
	 */
	private String localData;

	/**
	 * 节点id (node id)
	 */
	private String nodeId;

	/**
	 * 用户id (user id)
	 */
	private Long userId;

	/**
	 * 执行id (execution id)
	 */
	private String executionId;

	/**
	 * 任务id (task id)
	 */
	private String taskId;

	/**
	 * 审批描述 (approval description)
	 */
	private String approveDesc;

	/**
	 * 节点名称 (node name)
	 */
	private String nodeName;

	/**
	 * 任务类型 (task type)
	 */
	private String taskType;

}
