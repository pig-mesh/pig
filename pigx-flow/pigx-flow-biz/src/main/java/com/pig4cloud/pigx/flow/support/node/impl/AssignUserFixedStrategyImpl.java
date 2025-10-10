package com.pig4cloud.pigx.flow.support.node.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.support.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 固定用户分配策略实现
 * <p>
 * 实现指定具体用户或部门的任务分配策略。
 * 该策略允许在流程设计时直接指定任务的处理人员或处理部门。
 * <p>
 * 支持的配置方式：
 * 1. 直接指定用户：任务将分配给指定的用户
 * 2. 指定部门：任务将分配给该部门下的所有用户
 * 3. 混合配置：同时指定用户和部门，任务将分配给所有指定的用户及部门下的用户
 * <p>
 * 使用场景：适用于处理人员固定的审批节点，如财务审批、人事审批等
 *
 * @author Huijun Zhao
 * @description 指定具体用户的分配策略
 * @date 2023-07-07 13:42
 */
@RequiredArgsConstructor
@Component(ProcessInstanceConstant.AssignedTypeClass.USER + "AssignUserStrategy")
public class AssignUserFixedStrategyImpl implements AssignUserStrategy {

	private final RemoteUserService remoteUserService;

	/**
	 * 处理固定用户分配逻辑
	 * <p>
	 * 从节点配置中获取指定的用户和部门信息，将部门展开为用户列表，
	 * 最终返回所有应该处理该任务的用户ID列表。
	 *
	 * @param node      节点配置对象，包含指定的用户和部门列表
	 * @param rootUser  流程发起人信息（本策略中未使用）
	 * @param variables 流程变量（本策略中未使用）
	 * @return 用户ID列表，包含所有指定的用户和部门下的用户
	 */
	@Override
	public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {

		// 指定人员
		List<NodeUser> userDtoList = node.getNodeUserList();
		// 用户ID列表
		List<Long> userIdList = userDtoList.stream()
			.filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey()))
			.map(NodeUser::getId)
                .toList();
		// 部门ID列表
		List<Long> deptIdList = userDtoList.stream()
			.filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey()))
			.map(NodeUser::getId)
                .toList();

		if (CollUtil.isNotEmpty(deptIdList)) {
			R<List<SysUser>> r = remoteUserService.getUserIdListByDeptIdList(deptIdList);
			if (CollUtil.isNotEmpty(r.getData())) {
				for (SysUser user : r.getData()) {
					if (!userIdList.contains(user.getUserId())) {
						userIdList.add(user.getUserId());
					}
				}
			}
		}

		return userIdList;
	}

}
