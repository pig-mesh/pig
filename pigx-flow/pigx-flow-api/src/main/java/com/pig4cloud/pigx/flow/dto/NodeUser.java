package com.pig4cloud.pigx.flow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 节点用户数据传输对象
 * <p>
 * 该DTO用于表示流程节点的执行人信息。可以代表具体的用户、角色或部门，
 * 用于流程节点的审批人配置、抄送人配置等场景。
 * 支持多种类型的执行人，如指定用户、角色成员、部门成员等。
 * </p>
 * 
 * @author pigx
 */
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class NodeUser {

	/**
	 * 用户/角色/部门ID
	 * <p>
	 * 根据type字段的不同，此ID可能代表：
	 * - 用户ID（当type为USER时）
	 * - 角色ID（当type为ROLE时）
	 * - 部门ID（当type为DEPT时）
	 * </p>
	 */
	private Long id;

	/**
	 * 名称
	 * <p>
	 * 显示名称，如用户姓名、角色名称、部门名称等。
	 * 用于在界面上展示给用户看。
	 * </p>
	 */
	private String name;

	/**
	 * 类型标识
	 * <p>
	 * 标识该对象的类型：
	 * - USER: 用户
	 * - ROLE: 角色
	 * - DEPT: 部门
	 * - VARIABLE: 流程变量
	 * </p>
	 */
	private String type;

	/**
	 * 是否选中
	 * <p>
	 * 在用户界面中标识该项是否被选中。
	 * 主要用于前端交互，如在审批人选择列表中标记已选项。
	 * </p>
	 */
	private Boolean selected;

	/**
	 * 用户头像URL
	 * <p>
	 * 用户的头像地址，仅当type为USER时有效。
	 * 用于在界面上展示用户头像，提升用户体验。
	 * </p>
	 */
	private String avatar;

}
