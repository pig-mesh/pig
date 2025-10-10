package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.util.List;

/**
 * 变量查询参数数据传输对象
 * <p>
 * 该DTO用于查询流程变量或任务变量。
 * 支持按任务ID或执行ID查询指定的变量，可以查询单个或多个变量。
 * 主要用于获取流程执行过程中的数据，如表单数据、控制变量等。
 * </p>
 * 
 * @author pigx
 */
@Data
public class VariableQueryParamDto {

	/**
	 * 任务ID
	 * <p>
	 * Flowable引擎的任务唯一标识。
	 * 用于查询特定任务的变量。
	 * 与executionId二选一，优先使用taskId。
	 * </p>
	 */
	private String taskId;

	/**
	 * 变量键列表
	 * <p>
	 * 需要查询的变量名称列表。
	 * 如果为空，则查询所有变量；
	 * 如果指定了具体的键，则只返回这些键对应的变量值。
	 * 例如：["amount", "reason", "approver"]
	 * </p>
	 */
	private List<String> keyList;

	/**
	 * 执行ID
	 * <p>
	 * Flowable引擎的执行实例ID。
	 * 用于查询特定执行实例的变量。
	 * 当没有taskId时使用此字段查询。
	 * </p>
	 */
	private String executionId;

}
