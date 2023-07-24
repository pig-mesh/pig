package com.pig4cloud.pigx.flow.engine.node.impl;

import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.RetOps;
import com.pig4cloud.pigx.flow.engine.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 来自角色
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@RequiredArgsConstructor
@Component(ProcessInstanceConstant.AssignedTypeClass.ROLE + "AssignUserStrategy")
public class AssignUserRoleStrategyImpl implements AssignUserStrategy {

	private final RemoteUserService remoteUserService;

	/**
	 * 处理节点并返回用户ID列表。
	 * @param node 节点
	 * @param rootUser 根用户
	 * @param variables 变量
	 * @return 用户ID列表
	 */
	@Override
	public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {
		// 使用 lambda 表达式和方法引用从 NodeUser 列表中提取角色 ID
		List<Long> roleIds = node.getNodeUserList().stream().map(NodeUser::getId).collect(Collectors.toList());
		// 提取 Optional 结果中的数据
		List<Long> data = RetOps.of(remoteUserService.getUserIdListByRoleIdList(roleIds))
			.getData()
			.orElseGet(Collections::emptyList);

		// 返回用户 ID 列表
		return new ArrayList<>(data);
	}

}
