package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 任务操作参数数据传输对象
 * <p>
 * 该DTO用于任务操作时传递参数，如完成任务、转办任务、委派任务等。 包含任务标识、操作人信息、流程变量、目标节点等信息。 支持单个任务操作和批量任务操作。
 * </p>
 * 
 * @author pigx
 */
@Data
public class TaskParamDto {

	/**
	 * 流程实例ID
	 * <p>
	 * 单个流程实例的唯一标识。 用于定位要操作的流程实例。
	 * </p>
	 */
	private String processInstanceId;

	/**
	 * 流程实例ID列表
	 * <p>
	 * 批量操作时使用，包含多个流程实例ID。 支持批量完成、批量转办等操作。
	 * </p>
	 */
	private List<String> processInstanceIdList;

	/**
	 * 节点ID
	 * <p>
	 * 当前操作的节点ID。 用于标识任务所在的流程节点。
	 * </p>
	 */
	private String nodeId;

	/**
	 * 是否添加子流程发起人
	 * <p>
	 * 标识是否需要在子流程中记录主流程的发起人信息。 true: 子流程会继承主流程的发起人信息 false: 子流程独立记录发起人
	 * </p>
	 */
	private Boolean appendChildProcessRootId;

	/**
	 * 任务ID
	 * <p>
	 * Flowable引擎的任务唯一标识。 用于定位要操作的具体任务。
	 * </p>
	 */
	private String taskId;

	/**
	 * 操作用户ID
	 * <p>
	 * 执行任务操作的用户ID。 记录是谁在操作这个任务。
	 * </p>
	 */
	private String userId;

	/**
	 * 目标用户ID（已废弃）
	 * <p>
	 * 单个目标用户ID，用于转办等操作。 建议使用targetUserIdList代替。
	 * </p>
	 */
	private String targetUserId;

	/**
	 * 流程变量参数
	 * <p>
	 * 任务完成时设置的流程变量。 这些变量会存储在流程实例中，供后续节点使用。 如审批意见、审批结果、业务数据等。
	 * </p>
	 */
	private Map<String, Object> paramMap;

	/**
	 * 任务本地变量
	 * <p>
	 * 仅在当前任务范围内有效的变量。 任务完成后这些变量会被清除，不会传递到后续节点。 用于存储临时数据或任务特定的配置。
	 * </p>
	 */
	private Map<String, Object> taskLocalParamMap;

	/**
	 * 表单数据
	 */
	private Map<String, Object> formData;

	/**
	 * 目标节点ID
	 * <p>
	 * 指定跳转的目标节点ID。 用于实现流程跳转、回退等功能。 如驳回到指定节点、跳过某些节点等。
	 * </p>
	 */
	private String targetNodeId;

	/**
	 * 转办目标用户ID列表
	 * <p>
	 * 任务转办时的目标用户ID列表。 支持将任务转办给一个或多个用户。 转办后原执行人不再拥有该任务。
	 * </p>
	 */
	private List<Long> targetUserIdList;

}
