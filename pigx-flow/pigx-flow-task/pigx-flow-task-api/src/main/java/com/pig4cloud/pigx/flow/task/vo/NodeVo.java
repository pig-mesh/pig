package com.pig4cloud.pigx.flow.task.vo;

import lombok.Data;

import java.util.List;

/**
 * 流程节点显示对象
 */
@Data
public class NodeVo {

	/**
	 * nodeId
	 */
	private String id;

	/**
	 * 用户列表
	 */
	private List<UserVo> userVoList;

	/**
	 * 显示
	 */
	private String placeholder;

	/**
	 * 状态 1进行中2已完成
	 */
	private Integer status;

	/**
	 * 节点名称
	 */
	private String name;

	/**
	 * 节点类型
	 */
	private Object type;

	/**
	 * 发起人选择用户
	 */
	private Boolean selectUser;

	/**
	 * 是否多选
	 */
	private Boolean multiple;

	/**
	 * 子级列表
	 */
	private List<NodeVo> children;

	/**
	 * 分支列表
	 */
	private List<NodeVo> branch;

}
