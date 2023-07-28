package com.pig4cloud.pigx.flow.engine.node.impl;

import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.engine.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 指定主管
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@RequiredArgsConstructor
@Component(ProcessInstanceConstant.AssignedTypeClass.LEADER + "AssignUserStrategy")
public class AssignUserLeaderStrategyImpl implements AssignUserStrategy {

	private final RemoteDeptService deptService;

	@Override
	public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {
		// 获取部门ID
		return node.getNodeUserList()
			.stream()
			.map(nodeUser -> deptService.getAllDeptLeader(nodeUser.getId()))
			.flatMap((Function<R<List<Long>>, Stream<Long>>) listR -> listR.getData() == null ? null
					: listR.getData().stream())
			.collect(Collectors.toList());
	}

}
