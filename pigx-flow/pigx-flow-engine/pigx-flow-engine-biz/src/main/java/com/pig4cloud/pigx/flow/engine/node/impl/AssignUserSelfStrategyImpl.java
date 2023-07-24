package com.pig4cloud.pigx.flow.engine.node.impl;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.flow.engine.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 发起人自己
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@Component(ProcessInstanceConstant.AssignedTypeClass.SELF + "AssignUserStrategy")
public class AssignUserSelfStrategyImpl implements AssignUserStrategy {

	@Override
	public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {
		return CollUtil.newArrayList(rootUser.getId());
	}

}
