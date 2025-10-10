package com.pig4cloud.pigx.flow.support.node.impl;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import com.pig4cloud.pigx.flow.support.node.AssignUserStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 部门主管分配策略实现
 * <p>
 * 实现将任务分配给指定部门主管的策略。 支持配置具体部门，如果未配置则默认使用当前用户所在部门。
 * <p>
 * 功能特点： 1. 支持指定多个部门，任务将分配给所有部门的主管 2. 如果未指定部门，默认分配给当前用户所在部门的主管 3. 支持获取部门的所有层级主管（如果部门有多级主管）
 * <p>
 * 使用场景： - 需要部门主管审批的场景 - 逐级审批流程中的部门主管节点 - 跨部门协作需要多个部门主管审批
 *
 * @author Huijun Zhao
 * @description 指定主管的分配策略
 * @date 2023-07-07 13:42
 */
@RequiredArgsConstructor
@Component(ProcessInstanceConstant.AssignedTypeClass.LEADER + "AssignUserStrategy")
public class AssignUserLeaderStrategyImpl implements AssignUserStrategy {

	private final RemoteDeptService deptService;

	/**
	 * 处理部门主管分配逻辑
	 * <p>
	 * 根据节点配置的部门列表，查询所有部门的主管用户ID。 如果节点未配置部门，则使用当前操作用户的部门作为默认值。
	 * @param node 节点配置对象，可能包含指定的部门列表
	 * @param rootUser 流程发起人信息（本策略中未直接使用）
	 * @param variables 流程变量（本策略中未使用）
	 * @return 所有相关部门主管的用户ID列表
	 */
	@Override
	public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {
		// 如果节点没有设置部门主管，则默认为当前用户的部门主管
		if (CollUtil.isEmpty(node.getNodeUserList())) {
			List<NodeUser> nodeUserList = new ArrayList<>();
			nodeUserList.addAll(SecurityUtils.getUser()
				.getDeptIds()
				.stream()
				.map(deptId -> NodeUser.builder().id(deptId).build())
				.toList());
			node.setNodeUserList(nodeUserList);
		}

		return node.getNodeUserList()
			.stream()
			.map(nodeUser -> deptService.getAllDeptLeader(nodeUser.getId()))
			.flatMap((Function<R<List<Long>>, Stream<Long>>) listR -> listR.getData() == null ? null
					: listR.getData().stream())
			.toList();
	}

}
