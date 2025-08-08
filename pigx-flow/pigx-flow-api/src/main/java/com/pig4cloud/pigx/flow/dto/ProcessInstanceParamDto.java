package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 流程实例参数数据传输对象
 * <p>
 * 该DTO用于启动流程实例或操作流程实例时传递参数。 包含流程定义信息、流程变量、发起人等核心信息。 主要用于流程引擎的实例创建和流程变量设置。
 * </p>
 * 
 * @author pigx
 */
@Data
public class ProcessInstanceParamDto {

	/**
	 * 流程定义ID
	 * <p>
	 * 要启动的流程定义的唯一标识。 对应已部署的流程模板ID，如：leave_process_v1。
	 * </p>
	 */
	private String flowId;

	/**
	 * 流程变量集合
	 * <p>
	 * 存储流程执行过程中需要的各种变量，如： - 表单数据：申请金额、请假天数等 - 控制变量：审批级别、分支条件等 - 业务数据：关联的业务ID、业务类型等
	 * 这些变量会在流程执行过程中被各个节点使用。
	 * </p>
	 */
	private Map<String, Object> paramMap = new HashMap<>();

	/**
	 * 流程发起人ID
	 * <p>
	 * 发起流程实例的用户ID。 用于记录流程的发起者，也可能作为第一个任务的执行人。
	 * </p>
	 */
	private String startUserId;

	/**
	 * 流程实例ID
	 * <p>
	 * 已存在的流程实例ID，用于对已有流程实例进行操作。 如果是启动新流程，此字段为空； 如果是操作已有流程（如设置变量），则需要提供此ID。
	 * </p>
	 */
	private String processInstanceId;

	/** 状态 */
	private Integer status;

	/** 完成原因 */
	private String finishReason;

}
