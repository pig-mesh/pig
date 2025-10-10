package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.util.Map;

/**
 * 任务结果数据传输对象
 * <p>
 * 该DTO用于返回任务的详细信息和执行结果。
 * 包含任务的当前状态、节点配置、流程变量、委派状态等信息。
 * 主要用于任务详情展示、任务处理页面的数据渲染。
 * </p>
 *
 * @author pigx
 */
@Data
public class TaskResultDto {

	/**
	 * 是否为当前任务
	 * <p>
	 * 标识该任务是否为流程的当前活动任务。
	 * true: 任务正在等待处理
	 * false: 任务已完成或不是当前节点
	 * </p>
	 */
	private Boolean currentTask;

	/**
	 * 流程定义ID
	 * <p>
	 * 任务所属的流程定义唯一标识。
	 * 用于获取流程模板信息。
	 * </p>
	 */
	private String flowId;

	/**
	 * 任务节点配置
	 * <p>
	 * 包含任务节点的完整配置信息。
	 * 如节点类型、审批人设置、表单权限、条件配置等。
	 * 用于渲染任务处理界面。
	 * </p>
	 */
	private Node taskNode;

	/**
	 * 节点ID
	 * <p>
	 * 任务对应的流程节点ID。
	 * 用于定位任务在流程中的位置。
	 * </p>
	 */
	private String nodeId;

	/**
	 * 流程实例ID
	 * <p>
	 * 任务所属的流程实例唯一标识。
	 * 用于关联到具体的流程实例。
	 * </p>
	 */
	private String processInstanceId;

	/**
	 * 委派状态
	 * <p>
	 * 标识任务的委派状态：
	 * - PENDING: 待处理，委派任务等待被委派人处理
	 * - RESOLVED: 已解决，委派任务已完成并返回给委派人
	 * - null: 非委派任务
	 * </p>
	 */
	private String delegationState;

	/**
	 * 是否允许继续委派
	 * <p>
	 * 控制任务是否可以被继续委派给其他人。
	 * true: 允许当前处理人将任务委派给其他人
	 * false: 不允许继续委派
	 * </p>
	 */
	private Boolean delegate;

	/**
	 * 所有流程变量
	 * <p>
	 * 包含流程实例的所有变量，包括：
	 * - 表单数据：用户填写的业务数据
	 * - 系统变量：流程引擎设置的变量
	 * - 控制变量：用于流程控制的变量
	 * 这些变量可用于任务处理和页面展示。
	 * </p>
	 */
	private Map<String, Object> variableAll;

}
