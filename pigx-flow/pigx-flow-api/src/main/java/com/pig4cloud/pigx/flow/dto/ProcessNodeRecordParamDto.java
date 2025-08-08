package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

/**
 * 流程节点记录参数数据传输对象
 * <p>
 * 该DTO用于创建流程节点的执行记录。记录节点的执行信息， 包括节点类型、表单数据、执行上下文等。 主要用于流程历史记录的创建和流程轨迹的追踪。
 * </p>
 * 
 * @author pigx
 */
@Data
public class ProcessNodeRecordParamDto {

	/**
	 * 流程定义ID
	 * <p>
	 * 流程模板的唯一标识。 标识该记录所属的流程定义。
	 * </p>
	 */
	private String flowId;

	/**
	 * 流程实例ID
	 * <p>
	 * 运行中的流程实例唯一标识。 用于关联到具体的流程实例。
	 * </p>
	 */
	private String processInstanceId;

	/**
	 * 表单数据
	 * <p>
	 * JSON格式的表单数据，包含节点执行时的业务数据。 记录节点处理时的表单内容快照。
	 * </p>
	 */
	private String data;

	/**
	 * 节点ID
	 * <p>
	 * 流程节点的唯一标识。 标识具体执行的是哪个节点。
	 * </p>
	 */
	private String nodeId;

	/**
	 * 节点类型
	 * <p>
	 * 节点的类型标识，对应NodeTypeEnum： - ROOT: 发起人节点 - APPROVAL: 审批节点 - CC: 抄送节点 - CONDITION: 条件节点
	 * - CONCURRENT: 并行节点 - DELAY: 延迟节点 - TRIGGER: 触发器节点 - EMPTY: 空节点 - END: 结束节点
	 * </p>
	 */
	private String nodeType;

	/**
	 * 节点名称
	 * <p>
	 * 流程节点的显示名称，如"部门经理审批"、"财务审核"等。 用于在界面上展示，便于用户理解。
	 * </p>
	 */
	private String nodeName;

	/**
	 * 执行ID
	 * <p>
	 * Flowable引擎的执行ID。 用于关联流程引擎中的执行实例。
	 * </p>
	 */
	private String executionId;

	/**
	 * 任务ID
	 * <p>
	 * Flowable引擎生成的任务ID。 对于用户任务节点，记录对应的任务ID。
	 * </p>
	 */
	private String taskId;

	/**
	 * 审批意见
	 * <p>
	 * 节点执行时的意见或备注。 通常用于审批节点记录审批意见。
	 * </p>
	 */
	private String approveDesc;

	/**
	 * 任务类型
	 * <p>
	 * 标识任务的具体类型，用于区分不同的处理逻辑： - APPROVAL: 审批任务 - COPY: 抄送任务 - NOTIFY: 通知任务 - SYSTEM: 系统任务
	 * </p>
	 */
	private String taskType;

	/** 状态 */
	private Integer status;

	/** 完成原因 */
	private String finishReason;

}
