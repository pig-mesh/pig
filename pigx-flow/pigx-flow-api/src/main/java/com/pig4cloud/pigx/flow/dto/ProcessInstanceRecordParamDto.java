package com.pig4cloud.pigx.flow.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 流程实例记录参数数据传输对象
 * <p>
 * 该DTO用于创建或更新流程实例的执行记录。
 * 记录流程实例的基本信息和表单数据，用于流程历史追溯和数据存储。
 * 支持主流程和子流程的记录创建。
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Getter
@Setter
public class ProcessInstanceRecordParamDto {

	/**
	 * 用户ID
	 * <p>
	 * 执行此操作的用户ID，通常是流程发起人或当前任务处理人。
	 * 用于记录操作人信息，便于审计和追溯。
	 * </p>
	 */
	private Long userId;

	/**
	 * 流程定义ID
	 * <p>
	 * 流程模板的唯一标识。
	 * 标识这条记录属于哪个流程定义。
	 * </p>
	 */
	private String flowId;

	/**
	 * 流程实例ID
	 * <p>
	 * 当前流程实例的唯一标识。
	 * 用于关联流程实例的所有相关记录。
	 * </p>
	 */
	private String processInstanceId;

	/**
	 * 父流程实例ID
	 * <p>
	 * 如果当前流程是子流程，此字段存储父流程的实例ID。
	 * 用于建立流程间的父子关系，支持流程嵌套场景。
	 * 如果是主流程，此字段为空。
	 * </p>
	 */
	private String parentProcessInstanceId;

	/**
	 * 表单数据
	 * <p>
	 * JSON格式的表单数据，存储流程实例的业务数据。
	 * 包含用户填写的所有表单字段值，如申请内容、金额、日期等。
	 * 这些数据会在流程执行过程中被各节点使用和展示。
	 * </p>
	 */
	private String formData;

}
