package com.pig4cloud.pigx.flow.support.node;

import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;

import java.util.List;
import java.util.Map;

/**
 * 任务分配用户策略接口
 * <p>
 * 定义了流程节点任务分配给用户的策略模式接口。
 * 不同的节点可能有不同的用户分配策略，如指定用户、指定角色、发起人、部门负责人等。
 * 实现类需要根据节点配置和流程上下文，返回应该分配任务的用户ID列表。
 * <p>
 * 该接口是流程引擎用户分配机制的核心，确保任务能够正确地分配给合适的处理人。
 *
 * @author pigx
 * @see com.pig4cloud.pigx.flow.dto.Node 节点配置信息
 * @see com.pig4cloud.pigx.flow.dto.NodeUser 用户信息
 */
public interface AssignUserStrategy {

	/**
	 * 处理用户分配逻辑
	 * <p>
	 * 根据节点配置的用户分配策略，结合流程发起人信息和流程变量，
	 * 计算并返回应该处理该节点任务的用户ID列表。
	 *
	 * @param node      节点配置对象，包含节点的用户分配策略配置
	 * @param rootUser  流程发起人信息，可用于"发起人"、"发起人部门负责人"等策略
	 * @param variables 流程变量Map，包含表单数据和其他流程上下文信息
	 * @return 应该分配任务的用户ID列表，如果返回空列表则任务无人处理
	 */
	List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables);

}
