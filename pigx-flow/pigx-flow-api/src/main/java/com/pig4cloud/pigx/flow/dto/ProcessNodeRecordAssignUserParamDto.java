package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

/**
 * 流程节点记录执行人参数数据传输对象
 * <p>
 * 该DTO用于记录流程节点的执行人信息。当任务分配给用户或用户处理任务时，
 * 创建相应的执行人记录，用于追踪任务的处理过程和审批历史。
 * 包含任务执行的完整上下文信息。
 * </p>
 * 
 * @author pigx
 */
@Data
public class ProcessNodeRecordAssignUserParamDto {

	/**
	 * 流程定义ID
	 * <p>
	 * 流程模板的唯一标识。
	 * 标识该记录所属的流程定义。
	 * </p>
	 */
	private String flowId;

	/**
	 * 流程实例ID
	 * <p>
	 * 运行中的流程实例唯一标识。
	 * 用于关联到具体的流程实例。
	 * </p>
	 */
	private String processInstanceId;

	/**
	 * 表单数据
	 * <p>
	 * JSON格式的表单数据，包含流程实例的业务数据。
	 * 记录任务执行时的表单内容，便于历史查看。
	 * </p>
	 */
	private String data;

	/**
	 * 节点本地数据
	 * <p>
	 * 节点级别的局部数据，仅在当前节点范围内有效。
	 * 可能包含节点特定的配置或临时数据。
	 * </p>
	 */
	private String localData;

	/**
	 * 节点ID
	 * <p>
	 * 流程节点的唯一标识。
	 * 标识任务所在的节点。
	 * </p>
	 */
	private String nodeId;

	/**
	 * 执行人用户ID
	 * <p>
	 * 被分配执行该任务的用户ID。
	 * 记录谁负责处理这个任务。
	 * </p>
	 */
	private Long userId;

	/**
	 * 执行ID
	 * <p>
	 * Flowable引擎的执行ID。
	 * 用于关联流程引擎中的执行实例。
	 * </p>
	 */
	private String executionId;

	/**
	 * 任务ID
	 * <p>
	 * Flowable引擎生成的任务ID。
	 * 用于唯一标识一个用户任务实例。
	 * </p>
	 */
	private String taskId;

	/**
	 * 审批意见
	 * <p>
	 * 用户处理任务时填写的审批意见或备注。
	 * 记录审批理由、建议等信息。
	 * </p>
	 */
	private String approveDesc;

	/**
	 * 节点名称
	 * <p>
	 * 流程节点的显示名称，如"部门经理审批"。
	 * 用于在界面上展示，提高可读性。
	 * </p>
	 */
	private String nodeName;

	/**
	 * 任务类型
	 * <p>
	 * 标识任务的类型：
	 * - APPROVAL: 审批任务
	 * - COPY: 抄送任务
	 * - NOTIFY: 通知任务
	 * - CUSTOM: 自定义任务
	 * </p>
	 */
	private String taskType;

}
