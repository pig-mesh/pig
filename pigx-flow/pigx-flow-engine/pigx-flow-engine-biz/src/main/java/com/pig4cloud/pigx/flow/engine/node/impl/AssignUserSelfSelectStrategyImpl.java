package com.pig4cloud.pigx.flow.engine.node.impl;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.engine.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发起人自选
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@Slf4j
@RequiredArgsConstructor
@Component(ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT + "AssignUserStrategy")
public class AssignUserSelfSelectStrategyImpl implements AssignUserStrategy {

	private final ObjectMapper objectMapper;

	@SneakyThrows
	@Override
	public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {

		List<Long> assignList = new ArrayList<>();

		Object variable = variables.get(StrUtil.format("{}_assignee_select", node.getId()));
		log.info("{}-发起人自选参数:{}", node.getName(), variable);
		if (variable == null) {
			return assignList;
		}

		List<NodeUser> nodeUserDtos = objectMapper.readValue(objectMapper.writeValueAsString(variable),
				new TypeReference<>() {
				});

		List<Long> collect = nodeUserDtos.stream().map(NodeUser::getId).toList();

		assignList.addAll(collect);
		return assignList;
	}

}
