package com.pig4cloud.pigx.flow.engine.expression.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pigx.flow.task.dto.Condition;
import com.pig4cloud.pigx.flow.task.dto.GroupCondition;
import com.pig4cloud.pigx.flow.task.dto.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class NodeExpressionStrategyFactory {

	public static String handleSingleCondition(Condition nodeConditionDto) {
		Map<String, NodeConditionStrategy> nodeConditionStrategyMap = SpringUtil
			.getBeansOfType(NodeConditionStrategy.class);

		// 通过keyType获取对应的处理器, 忽略大小写
		AtomicReference<NodeConditionStrategy> nodeConditionHandler = new AtomicReference<>();
		nodeConditionStrategyMap.forEach((k, v) -> {
			if (k.equalsIgnoreCase(nodeConditionDto.getKeyType() + "NodeConditionStrategy")) {
				nodeConditionHandler.set(v);
			}
		});


		if (nodeConditionHandler.get() == null) {
			return "(1==1)";
		}
		return nodeConditionHandler.get().handle(nodeConditionDto);
	}

	/**
	 * 组内处理表达式
	 * @param groupDto
	 * @return
	 */
	public static String handleGroupCondition(GroupCondition groupDto) {

		List<String> exps = new ArrayList<>();

		for (Condition condition : groupDto.getConditionList()) {
			String singleExpression = handleSingleCondition(condition);
			exps.add(singleExpression);
		}
		Boolean mode = groupDto.getMode();

		if (!mode) {
			String join = CollUtil.join(exps, "||");

			return "(" + join + ")";
		}

		String join = CollUtil.join(exps, "&&");
		return "(" + join + ")";
	}

	/**
	 * 处理单个分支表达式
	 * @return
	 */
	public static String handle(Node node) {

		List<String> exps = new ArrayList<>();

		List<GroupCondition> groups = node.getConditionList();
		if (CollUtil.isEmpty(groups)) {
			return "${1==1}";
		}
		for (GroupCondition group : groups) {
			String s = handleGroupCondition(group);
			exps.add(s);
		}

		if (!node.getGroupMode()) {
			String join = CollUtil.join(exps, "||");
			return "${(" + join + ")}";
		}

		String join = CollUtil.join(exps, "&&");
		return "${(" + join + ")}";
	}

	public static String handleDefaultBranch(List<Node> branchs, int currentIndex) {

		List<String> expList = new ArrayList<>();

		int index = 1;
		for (Node branch : branchs) {

			if (index == currentIndex + 1) {
				continue;
			}

			String exp = handle(branch);
			String s = StrUtil.subBetween(exp, "${", "}");
			expList.add(StrUtil.format("({})", s));

			index++;
		}
		String join = StrUtil.format("!({})", CollUtil.join(expList, "||"));
		return "${" + join + "}";
	}

}
