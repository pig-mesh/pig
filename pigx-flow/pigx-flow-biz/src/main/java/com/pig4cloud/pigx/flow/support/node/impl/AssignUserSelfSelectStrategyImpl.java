package com.pig4cloud.pigx.flow.support.node.impl;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.support.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 发起人自选分配策略实现
 * <p>
 * 实现发起人在提交流程时自主选择后续节点处理人的策略。
 * 这种策略提供了最大的灵活性，允许流程发起人根据实际情况选择合适的审批人。
 * <p>
 * 功能特点：
 * 1. 在流程发起时由发起人选择各节点的处理人
 * 2. 选择的处理人信息存储在特定的流程变量中
 * 3. 通过节点ID关联获取对应的处理人
 * <p>
 * 使用场景：
 * - 审批人不固定，需要根据具体情况选择
 * - 发起人最了解应该由谁审批的场景
 * - 需要灵活配置审批链的流程
 * <p>
 * 变量命名规则：{nodeId}_assignee_select
 *
 * @author Huijun Zhao
 * @description 发起人自选的分配策略
 * @date 2023-07-07 13:42
 */
@Slf4j
@RequiredArgsConstructor
@Component(ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT + "AssignUserStrategy")
public class AssignUserSelfSelectStrategyImpl implements AssignUserStrategy {

	private final ObjectMapper objectMapper;

	/**
	 * 处理发起人自选的用户分配逻辑
	 * <p>
	 * 从流程变量中获取发起人为当前节点选择的处理人信息。
	 * 变量名格式为：{nodeId}_assignee_select
	 *
	 * @param node      节点配置对象，使用节点ID构建变量名
	 * @param rootUser  流程发起人信息（本策略中未直接使用）
	 * @param variables 流程变量Map，包含发起人选择的处理人信息
	 * @return 发起人选择的用户ID列表，如果未找到则返回空列表
	 * @throws Exception 当JSON解析失败时抛出异常
	 */
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
