package com.pig4cloud.pigx.flow.vo;

import com.pig4cloud.pigx.flow.entity.Process;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 流程视图对象
 * <p>
 * 继承自Process实体类，用于流程发起和展示时的扩展信息。
 * 包含了流程发起时需要的额外配置信息，如需要选择审批人的节点、流程变量、表单权限等。
 * </p>
 */
@Data
public class ProcessVO extends Process {

	/**
	 * 需要发起人选择审批人的节点ID列表
	 * <p>
	 * 包含所有需要流程发起人指定审批人的节点ID。
	 * 在流程发起时，前端需要根据这个列表展示审批人选择界面。
	 * </p>
	 */
	private List<String> selectUserNodeId;

	/**
	 * 流程变量映射表
	 * <p>
	 * 包含流程运行时的变量信息，key为变量名，value为变量值。
	 * 这些变量可用于流程条件判断、表单数据传递等场景。
	 * </p>
	 */
	private Map<String, Object> variableMap;

	/**
	 * 发起人节点的表单权限配置
	 * <p>
	 * 定义了发起人在填写表单时各个字段的权限。
	 * key为表单字段ID，value为权限标识（如"write"、"read"、"hidden"）。
	 * 用于控制发起人对表单字段的操作权限。
	 * </p>
	 */
	private Map<String,String> formPerms;

}
