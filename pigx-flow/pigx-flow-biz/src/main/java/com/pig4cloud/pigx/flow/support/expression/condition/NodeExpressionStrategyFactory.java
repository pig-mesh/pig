package com.pig4cloud.pigx.flow.support.expression.condition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pigx.flow.dto.Condition;
import com.pig4cloud.pigx.flow.dto.GroupCondition;
import com.pig4cloud.pigx.flow.dto.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 节点表达式策略工厂
 * <p>
 * 负责管理和协调不同类型的节点条件策略，生成复杂的条件表达式。 该工厂类是整个条件表达式生成系统的核心，它将多个条件组合成完整的SpEL表达式。
 * <p>
 * 主要功能： 1. 根据条件类型动态选择对应的策略实现 2. 处理条件组内的逻辑关系（AND/OR） 3. 处理多个条件组之间的逻辑关系 4. 生成默认分支的条件表达式
 *
 * @author pigx
 */
public class NodeExpressionStrategyFactory {

	/**
	 * 处理单个条件
	 * <p>
	 * 根据条件的keyType（表单控件类型）动态选择对应的策略实现， 并调用其handle方法生成具体的表达式。
	 * @param nodeConditionDto 单个条件配置对象
	 * @return 生成的条件表达式字符串，如果找不到对应策略则返回"(1==1)"
	 */
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
	 * 处理条件组表达式
	 * <p>
	 * 将一个条件组内的多个条件根据逻辑关系（AND/OR）组合成一个表达式。 mode为true时使用AND连接，为false时使用OR连接。
	 * @param groupDto 条件组配置对象，包含多个条件和逻辑关系
	 * @return 组合后的条件表达式，用括号包裹
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
	 * 处理节点的完整条件表达式
	 * <p>
	 * 将节点的所有条件组根据节点级别的逻辑关系组合成最终的SpEL表达式。 如果节点没有配置条件，返回永真表达式"${1==1}"。
	 * @param node 节点配置对象，包含多个条件组
	 * @return 完整的SpEL表达式，包含${}包裹
	 */
	public static String handle(Node node) {
		if (1 == node.getConditionType()) {
			return "${(" + node.getExpressionText() + ")}";
		}

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

	/**
	 * 处理默认分支的条件表达式
	 * <p>
	 * 生成一个条件表达式，当其他所有分支条件都不满足时，该表达式为真。 实现方式是将其他分支的条件取反并用OR连接，然后整体取反。
	 * @param branchs 所有分支节点列表
	 * @param currentIndex 当前默认分支的索引位置
	 * @return 默认分支的条件表达式
	 */
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