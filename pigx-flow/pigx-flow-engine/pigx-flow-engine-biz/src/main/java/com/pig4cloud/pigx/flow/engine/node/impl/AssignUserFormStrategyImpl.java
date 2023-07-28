package com.pig4cloud.pigx.flow.engine.node.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.engine.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 来自表单
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@RequiredArgsConstructor
@Component(ProcessInstanceConstant.AssignedTypeClass.FORM_USER + "AssignUserStrategy")
public class AssignUserFormStrategyImpl implements AssignUserStrategy {

	private final ObjectMapper objectMapper;

	@SneakyThrows
	@Override
	public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {
		List<Long> assignList = new ArrayList<>();

		Object variable = variables.get(node.getFormUserId());
		if (variable != null && !StrUtil.isBlankIfStr(variable)) {
			String jsonString = objectMapper.writeValueAsString(variable);
			List<NodeUser> nodeUserDtoList = JSONUtil.toList(jsonString, NodeUser.class);
			assignList.addAll(nodeUserDtoList.stream().map(NodeUser::getId).toList());
		}

		return assignList;
	}

}
