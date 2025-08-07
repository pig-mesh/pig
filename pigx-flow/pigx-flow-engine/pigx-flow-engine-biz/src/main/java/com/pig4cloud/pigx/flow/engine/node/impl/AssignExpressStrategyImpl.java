package com.pig4cloud.pigx.flow.support.node.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import com.pig4cloud.pigx.flow.support.node.AssignUserStrategy;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.api.variable.VariableContainer;
import org.flowable.common.engine.impl.el.ExpressionManager;
import org.flowable.common.engine.impl.variable.MapDelegateVariableContainer;
import org.flowable.engine.ManagementService;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 表达式分配用户策略实现类：将任务分配给流程发起人本人
 *
 * @author lengleng
 * @date 2025/08/07
 */
@Component(ProcessInstanceConstant.AssignedTypeClass.EXPRESSION + "AssignUserStrategy")
public class AssignExpressStrategyImpl implements AssignUserStrategy {

	/**
	 * 处理发起人分配逻辑
	 * <p>
	 * 直接返回流程发起人的用户ID，任务将分配给发起人本人。
	 * @param node 节点配置对象（本策略中未使用）
	 * @param rootUser 流程发起人信息，包含发起人的用户ID
	 * @param variables 流程变量（本策略中未使用）
	 * @return 包含发起人用户ID的列表
	 */
	@Override
	public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {
		String assignExpress = node.getAssignExpress();
		Object result = getExpressionValue(variables, assignExpress);
		return Convert.toList(Long.class, result);
	}

	/**
	 * 获取表达式值
	 * @param variableContainer 变量容器
	 * @param expressionString 表达式字符串
	 * @param processEngineConfiguration 流程引擎配置
	 * @return 表达式计算结果
	 */
	private static Object getExpressionValue(VariableContainer variableContainer, String expressionString,
			ProcessEngineConfigurationImpl processEngineConfiguration) {
		assert processEngineConfiguration != null;
		ExpressionManager expressionManager = processEngineConfiguration.getExpressionManager();
		assert expressionManager != null;
		Expression expression = expressionManager.createExpression(expressionString);
		return expression.getValue(variableContainer);
	}

	/**
	 * 获取表达式值
	 * @param variableContainer 变量容器
	 * @param expressionString 表达式字符串
	 * @return 表达式计算结果
	 */
	public static Object getExpressionValue(VariableContainer variableContainer, String expressionString) {
		ProcessEngineConfigurationImpl processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration();
		if (processEngineConfiguration != null) {
			return getExpressionValue(variableContainer, expressionString, processEngineConfiguration);
		}
		ManagementService managementService = SpringUtil.getBean(ManagementService.class);
		assert managementService != null;
		return managementService.executeCommand(context -> getExpressionValue(variableContainer, expressionString,
				CommandContextUtil.getProcessEngineConfiguration()));
	}

	/**
	 * 根据变量映射和表达式字符串获取表达式值
	 * @param variable 变量映射
	 * @param expressionString 表达式字符串
	 * @return 表达式计算结果
	 */
	public static Object getExpressionValue(Map<String, Object> variable, String expressionString) {
		VariableContainer variableContainer = new MapDelegateVariableContainer(variable, VariableContainer.empty());
		return getExpressionValue(variableContainer, expressionString);
	}

}
