package com.pig4cloud.pigx.flow.support.node.impl;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.flow.support.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 发起人自己分配策略实现
 * <p>
 * 实现将任务分配给流程发起人本人的策略。
 * 这是最简单的分配策略，任务将返回给发起流程的用户处理。
 * <p>
 * 使用场景：
 * 1. 申请被驳回后需要发起人修改
 * 2. 流程中需要发起人确认某些信息
 * 3. 发起人需要补充材料或信息
 * <p>
 * 特点：无需配置，自动获取流程发起人信息
 *
 * @author Huijun Zhao
 * @description 发起人自己的分配策略
 * @date 2023-07-07 13:42
 */
@Component(ProcessInstanceConstant.AssignedTypeClass.SELF + "AssignUserStrategy")
public class AssignUserSelfStrategyImpl implements AssignUserStrategy {

	/**
	 * 处理发起人分配逻辑
	 * <p>
	 * 直接返回流程发起人的用户ID，任务将分配给发起人本人。
	 *
	 * @param node      节点配置对象（本策略中未使用）
	 * @param rootUser  流程发起人信息，包含发起人的用户ID
	 * @param variables 流程变量（本策略中未使用）
	 * @return 包含发起人用户ID的列表
	 */
	@Override
	public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {
		return CollUtil.newArrayList(rootUser.getId());
	}

}
