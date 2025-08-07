package com.pig4cloud.pigx.flow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 流程节点数据传输对象
 * <p>
 * 用于流程定义的节点配置信息传输。该对象包含了节点的所有配置信息， 包括节点类型、审批人配置、表单权限、条件配置等。
 * 支持嵌套结构，可以表示复杂的流程结构（如条件分支、并行分支）。
 * </p>
 * 
 * @author pigx
 */
@Data
public class Node {

	/**
	 * 节点唯一标识
	 */
	private String id;

	/**
	 * 父节点ID 用于构建节点的层级关系
	 */
	private String parentId;

	/**
	 * 头节点ID 用于条件分支和并行分支的开始节点标识
	 */
	private String headId;

	/**
	 * 尾节点ID 用于条件分支和并行分支的结束节点标识
	 */
	private String tailId;

	/**
	 * 占位符文本 用于流程设计器中的显示
	 */
	private String placeHolder;

	/**
	 * 节点类型 对应NodeTypeEnum中的值
	 */
	private Integer type;

	/**
	 * 节点名称 节点的显示名称
	 */
	@JsonProperty(value = "nodeName")
	private String name;

	/**
	 * 错误标识 标识节点配置是否有错误
	 */
	private Boolean error;

	/**
	 * 子节点 当前节点的下一个节点，用于构建节点链
	 */
	@JsonProperty("childNode")
	private Node children;

	/**
	 * 事件配置 JSON格式存储节点事件配置（如超时提醒、自动审批等）
	 */
	private String eventConfig;

	/**
	 * 执行人分配类型 对应AssignedTypeClass中的常量（指定用户、部门主管、角色等）
	 */
	private Integer assignedType;

	/**
	 * 是否多人审批 true=多人审批，false=单人审批
	 */
	private Boolean multiple;

	/**
	 * 多人审批模式 1=会签（所有人都同意），2=或签（一人同意即可），3=顺签（依次审批）
	 */
	private Integer multipleMode;

	/**
	 * 部门主管级别 当assignedType为LEADER时，指定查找第几级主管
	 */
	private Integer deptLeaderLevel;

	/** 分配规则名称 */
	private String assignExpressName;

	/** 分配表达式 */
	private String assignExpress;

	/**
	 * 表单用户字段ID 当assignedType为FORM_USER时，指定从哪个表单字段获取用户
	 */
	private String formUserId;

	/**
	 * 表单用户字段名称 用于显示
	 */
	private String formUserName;

	/**
	 * 节点执行人列表 当assignedType为USER或ROLE时，存储具体的用户或角色信息
	 */
	private List<NodeUser> nodeUserList;

	/**
	 * 条件节点列表 当节点类型为条件分支时，存储所有分支节点
	 */
	private List<Node> conditionNodes;

	/**
	 * 表单字段权限配置 key=表单字段ID，value=权限类型（H=隐藏，R=只读，E=编辑）
	 */
	private Map<String, String> formPerms;

	/**
	 * 无执行人处理配置 定义当节点找不到执行人时的处理策略
	 */
	private Nobody nobody;

	/**
	 * 分组模式标识 用于条件配置的分组模式
	 */
	private Boolean groupMode;

	/**
	 * 条件列表 当节点为条件分支时，定义各分支的执行条件
	 */
	private List<GroupCondition> conditionList;

	/**
	 * 拒绝配置 定义节点拒绝后的处理策略
	 */
	private Refuse refuse;

}
