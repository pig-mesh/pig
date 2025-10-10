package com.pig4cloud.pigx.flow.vo;

import lombok.Data;

import java.util.List;

/**
 * 流程节点显示视图对象
 * <p>
 * 用于在前端展示流程节点的详细信息，包括节点的基本属性、审批人员、状态等。
 * 支持树形结构和分支结构的节点展示，可用于流程图的渲染和流程进度的追踪。
 * </p>
 */
@Data
public class NodeVo {

	/**
	 * 节点ID
	 * <p>
	 * 流程节点的唯一标识符，对应流程定义中的节点ID
	 * </p>
	 */
	private String id;

	/**
	 * 节点审批人列表
	 * <p>
	 * 该节点的审批人员信息列表，包含已审批和待审批的人员
	 * </p>
	 */
	private List<UserVo> userVoList;

	/**
	 * 节点占位提示
	 * <p>
	 * 用于在节点上显示的提示信息，如"请选择审批人"等
	 * </p>
	 */
	private String placeholder;

	/**
	 * 节点状态
	 * <p>
	 * 节点的执行状态：1-进行中，2-已完成
	 * </p>
	 */
	private Integer status;

	/**
	 * 节点名称
	 * <p>
	 * 节点的显示名称，如"部门经理审批"、"财务审核"等
	 * </p>
	 */
	private String name;

	/**
	 * 节点类型
	 * <p>
	 * 节点的类型标识，如"审批节点"、"抄送节点"、"条件节点"等。
	 * 使用Object类型以支持不同的类型表示方式
	 * </p>
	 */
	private Object type;

	/**
	 * 是否需要发起人选择审批人
	 * <p>
	 * 标识该节点是否需要流程发起人指定审批人员
	 * </p>
	 */
	private Boolean selectUser;

	/**
	 * 是否支持多选
	 * <p>
	 * 当需要选择审批人时，标识是否允许选择多个审批人
	 * </p>
	 */
	private Boolean multiple;

	/**
	 * 子节点列表
	 * <p>
	 * 该节点的直接子节点列表，用于构建节点的树形结构
	 * </p>
	 */
	private List<NodeVo> children;

	/**
	 * 分支节点列表
	 * <p>
	 * 条件分支或并行分支的节点列表，用于表示分支流程
	 * </p>
	 */
	private List<NodeVo> branch;

}
