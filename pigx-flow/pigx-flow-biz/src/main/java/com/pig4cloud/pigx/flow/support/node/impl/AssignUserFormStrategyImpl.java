package com.pig4cloud.pigx.flow.support.node.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.support.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 表单字段分配策略实现
 * <p>
 * 实现从表单字段中获取任务处理人的策略。
 * 允许在流程表单中通过用户选择器等控件动态指定任务的处理人。
 * <p>
 * 功能特点：
 * 1. 从流程变量中读取指定表单字段的值
 * 2. 支持表单中的用户选择器控件
 * 3. 动态获取用户，提供更灵活的任务分配
 * <p>
 * 使用场景：
 * - 需要在流程中动态指定下一步处理人
 * - 申请人可以选择审批人的场景
 * - 根据业务需要灵活指定处理人
 *
 * @author Huijun Zhao
 * @description 来自表单的分配策略
 * @date 2023-07-07 13:42
 */
@RequiredArgsConstructor
@Component(ProcessInstanceConstant.AssignedTypeClass.FORM_USER + "AssignUserStrategy")
public class AssignUserFormStrategyImpl implements AssignUserStrategy {

	private final ObjectMapper objectMapper;

	/**
	 * 处理基于表单字段的用户分配逻辑
	 * <p>
	 * 从流程变量中获取指定表单字段的值，该字段应包含用户信息。
	 * 将字段值解析为用户列表，并返回用户ID列表。
	 *
	 * @param node      节点配置对象，包含表单字段ID
	 * @param rootUser  流程发起人信息（本策略中未使用）
	 * @param variables 流程变量Map，包含表单数据
	 * @return 从表单字段中解析出的用户ID列表
	 * @throws Exception 当JSON解析失败时抛出异常
	 */
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
