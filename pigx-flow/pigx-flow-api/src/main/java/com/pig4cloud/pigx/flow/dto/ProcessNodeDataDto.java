package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

/**
 * 流程节点数据传输对象
 * <p>
 * 该DTO用于存储和传输流程节点的配置数据。
 * 每个流程节点可能有特定的配置信息，如审批人设置、表单权限、条件配置等，
 * 这些配置数据以JSON格式存储在data字段中。
 * </p>
 * 
 * @author pigx
 */
@Data
public class ProcessNodeDataDto {

	/**
	 * 流程定义ID
	 * <p>
	 * 节点所属的流程定义唯一标识。
	 * 标识该节点配置属于哪个流程模板。
	 * </p>
	 */
	private String flowId;

	/**
	 * 节点ID
	 * <p>
	 * 流程节点的唯一标识。
	 * 用于定位具体的节点，每个流程中的节点ID是唯一的。
	 * </p>
	 */
	private String nodeId;

	/**
	 * 节点配置数据
	 * <p>
	 * JSON格式的节点配置信息，可能包含：
	 * - 审批人配置：指定用户、角色、部门主管等
	 * - 表单权限：各字段的读写权限设置
	 * - 条件配置：分支条件、跳转条件等
	 * - 事件配置：超时提醒、自动处理等
	 * - 其他自定义配置
	 * 具体内容根据节点类型和业务需求而定。
	 * </p>
	 */
	private String data;

}
