package com.pig4cloud.pigx.flow.vo;

import lombok.Data;

import java.util.Map;

/**
 * 节点格式化参数视图对象
 * <p>
 * 用于传递流程节点格式化所需的参数信息，包含流程实例、任务和动态参数等信息。
 * 主要用于节点展示时的数据格式化和条件判断。
 * </p>
 */
@Data
public class NodeFormatParamVo {

	/**
	 * 流程ID
	 * <p>
	 * 流程定义的唯一标识符
	 * </p>
	 */
	private String flowId;

	/**
	 * 流程实例ID
	 * <p>
	 * 流程实例的唯一标识符，用于定位具体的流程实例
	 * </p>
	 */
	private String processInstanceId;

	/**
	 * 任务ID
	 * <p>
	 * 当前任务的唯一标识符，用于定位具体的待办任务
	 * </p>
	 */
	private String taskId;

	/**
	 * 参数映射表
	 * <p>
	 * 包含节点格式化所需的动态参数，key为参数名，value为参数值。
	 * 可用于条件判断、动态展示等场景。
	 * </p>
	 */
	private Map<String, Object> paramMap;

}
