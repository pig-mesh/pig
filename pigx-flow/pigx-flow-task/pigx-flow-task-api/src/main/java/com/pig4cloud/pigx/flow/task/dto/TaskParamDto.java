package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 任务完成参数对象
 *
 */
@Data
public class TaskParamDto {

	private String processInstanceId;

	private List<String> processInstanceIdList;

	/**
	 * 节点id
	 */
	private String nodeId;

	/**
	 * 添加子流程发起人
	 */
	private Boolean appendChildProcessRootId;

	/**
	 * 任务id
	 */
	private String taskId;

	/**
	 * 用户id
	 */
	private String userId;

	/**
	 * 模板用户id
	 */
	private String targetUserId;

	/**
	 * 参数
	 */
	private Map<String, Object> paramMap;

	/**
	 * 任务本地变量
	 */
	private Map<String, Object> taskLocalParamMap;

	/**
	 * 模板节点
	 */
	private String targetNodeId;

}
