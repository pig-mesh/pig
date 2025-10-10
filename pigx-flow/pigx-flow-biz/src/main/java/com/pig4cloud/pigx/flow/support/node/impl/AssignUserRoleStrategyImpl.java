package com.pig4cloud.pigx.flow.support.node.impl;

import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.RetOps;
import com.pig4cloud.pigx.flow.support.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 角色分配策略实现
 * <p>
 * 实现基于角色的任务分配策略。
 * 任务将分配给拥有指定角色的所有用户。
 * <p>
 * 功能特点：
 * 1. 支持配置多个角色
 * 2. 任务会分配给所有拥有这些角色的用户
 * 3. 通过远程服务查询角色对应的用户列表
 * <p>
 * 使用场景：
 * - 特定角色才能处理的审批节点（如财务角色、人事角色）
 * - 基于职能划分的任务分配
 * - 需要特定权限角色处理的流程节点
 *
 * @author Huijun Zhao
 * @description 来自角色的分配策略
 * @date 2023-07-07 13:42
 */
@RequiredArgsConstructor
@Component(ProcessInstanceConstant.AssignedTypeClass.ROLE + "AssignUserStrategy")
public class AssignUserRoleStrategyImpl implements AssignUserStrategy {

	private final RemoteUserService remoteUserService;

	/**
	 * 处理基于角色的用户分配逻辑
	 * <p>
	 * 从节点配置中获取角色ID列表，通过远程服务查询拥有这些角色的所有用户，
	 * 返回这些用户的ID列表作为任务的处理人。
	 *
	 * @param node      节点配置对象，包含角色ID列表
	 * @param rootUser  流程发起人信息（本策略中未使用）
	 * @param variables 流程变量（本策略中未使用）
	 * @return 拥有指定角色的用户ID列表
	 */
	@Override
	public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {
		// 使用 lambda 表达式和方法引用从 NodeUser 列表中提取角色 ID
		List<Long> roleIds = node.getNodeUserList().stream().map(NodeUser::getId).toList();
		// 提取 Optional 结果中的数据
		List<Long> data = RetOps.of(remoteUserService.getUserIdListByRoleIdList(roleIds))
			.getData()
			.orElseGet(Collections::emptyList);

		// 返回用户 ID 列表
		return new ArrayList<>(data);
	}

}
