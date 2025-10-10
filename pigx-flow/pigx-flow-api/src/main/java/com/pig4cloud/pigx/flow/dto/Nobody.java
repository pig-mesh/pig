package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.util.List;

/**
 * 无执行人处理策略数据传输对象
 * <p>
 * 该DTO用于定义当流程节点找不到执行人时的处理策略。
 * 在实际流程执行中，可能因为人员离职、角色变更等原因导致节点无法找到执行人，
 * 此时需要根据预定义的策略进行处理，如自动通过、转交管理员、指定默认审批人等。
 * </p>
 * 
 * @author pigx
 */
@Data
public class Nobody {

	/**
	 * 处理策略
	 * <p>
	 * 定义当节点找不到执行人时的处理方式：
	 * - TO_PASS: 自动通过，节点自动完成
	 * - TO_REFUSE: 自动拒绝，节点自动拒绝
	 * - TO_ADMIN: 转交给管理员处理
	 * - TO_USER: 转交给指定用户处理
	 * - TO_END: 直接结束流程
	 * </p>
	 */
	private String handler;

	/**
	 * 指定的处理人列表
	 * <p>
	 * 当handler为TO_USER时，指定接收任务的用户列表。
	 * 包含用户的ID、名称、类型等信息。
	 * 如果handler为其他值，此字段可为空。
	 * </p>
	 */
	private List<NodeUser> assignedUser;

}
