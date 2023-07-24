package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程实例参数对象
 */
@Data
public class ProcessInstanceParamDto {

	/**
	 * 流程id
	 */
	private String flowId;

	/**
	 * 参数集合
	 */
	private Map<String, Object> paramMap = new HashMap<>();

	/**
	 * 发起人
	 */
	private String startUserId;

	/**
	 * 实例id
	 */
	private String processInstanceId;

}
