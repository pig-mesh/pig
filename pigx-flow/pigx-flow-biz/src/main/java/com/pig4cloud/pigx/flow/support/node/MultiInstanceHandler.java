package com.pig4cloud.pigx.flow.support.node;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 多实例任务处理器
 * <p>
 * 处理Flowable多实例任务（会签、或签、顺序签等）的核心组件。 主要功能包括： 1. 解析多实例任务的执行人列表 2. 判断多实例任务的完成条件
 * <p>
 * 支持的多实例模式： - 会签：所有人都必须处理，任何人拒绝则任务结束 - 或签：任何一人处理即可完成 - 顺序签：按顺序逐个处理
 * <p>
 * 该类在流程定义中通过${multiInstanceHandler.resolveAssignee(execution)}方式调用
 *
 * @author pigx
 */
@Slf4j
@RequiredArgsConstructor
@Component("multiInstanceHandler")
public class MultiInstanceHandler {

	private final IProcessNodeDataService processNodeDataService;

	private final Map<String, AssignUserStrategy> assignUserStrategyMap;

	private final ObjectMapper objectMapper;

	/**
	 * 解析多实例任务的执行人列表
	 * <p>
	 * 根据节点配置的用户分配策略，动态计算该多实例任务应该分配给哪些用户。 如果节点没有配置或找不到执行人，会尝试从流程变量中获取默认值。
	 * 如果最终没有找到任何执行人，会返回一个特殊的空标记。
	 * @param execution 流程执行对象，包含流程实例信息和变量
	 * @return 执行人ID列表，如果没有执行人则返回包含空标记的列表
	 * @throws Exception 当JSON解析失败时抛出异常
	 */
	@SneakyThrows
	public List<Long> resolveAssignee(DelegateExecution execution) {
		// 执行人集合
		List<Long> assignList = new ArrayList<>();

		ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;

		String flowId = entity.getProcessDefinitionKey();
		String nodeId = entity.getActivityId();

		log.debug("nodeId={} nodeName={}", nodeId, entity.getActivityName());

		// 发起人
		Object rootUserObj = execution.getVariable("root");
		String rootUserJson = objectMapper.writeValueAsString(rootUserObj);
		NodeUser rootUser = objectMapper.readValue(rootUserJson, new TypeReference<List<NodeUser>>() {
		}).get(0);

		// 节点数据
		Node node = processNodeDataService.getNodeData(flowId, nodeId).getData();
		if (node != null) {
			Map<String, Object> variables = execution.getVariables();
			variables.put("execution", execution);
			Integer assignedType = node.getAssignedType();
			List<Long> userIdList = assignUserStrategyMap.get(assignedType + "AssignUserStrategy")
				.handle(node, rootUser, variables);
			assignList.addAll(userIdList);
		}
		else {
			// 默认值
			String format = StrUtil.format("{}_assignee_default_list", nodeId);
			Object variable = execution.getVariable(format);
			String variableJson = objectMapper.writeValueAsString(variable);

			List<NodeUser> nodeUserDtos = objectMapper.readValue(variableJson, new TypeReference<>() {
			});
			if (CollUtil.isNotEmpty(nodeUserDtos)) {
				List<Long> collect = nodeUserDtos.stream().map(NodeUser::getId).toList();
				assignList.addAll(collect);
			}
		}

		// 标记为空
		if (CollUtil.isEmpty(assignList)) {
			assignList.add(ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN);
		}
		return assignList;
	}

	/**
	 * 多实例任务完成条件检查
	 * <p>
	 * 检查多实例任务是否满足完成条件，支持以下模式： 1. 会签（全部同意）：所有人都必须同意，任何人拒绝则立即结束 2. 或签（一人同意）：任何一人同意即可完成 3.
	 * 顺序签：按顺序处理，任何人拒绝则立即结束
	 * <p>
	 * 该方法在流程定义中作为completionCondition表达式被调用
	 * @param execution 流程执行对象，包含多实例的执行信息
	 * @return true表示满足完成条件，多实例任务结束；false表示继续执行
	 */
	public boolean completionCondition(DelegateExecution execution) {
		ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
		String processDefinitionKey = entity.getProcessDefinitionKey();
		String nodeId = execution.getCurrentActivityId();

		Node node = processNodeDataService.getNodeData(processDefinitionKey, nodeId).getData();
		Integer multipleMode = node.getMultipleMode();
		BigDecimal modePercentage = BigDecimal.valueOf(100);

		Object variable = execution.getVariable(StrUtil.format("{}_approve_condition", nodeId));
		log.debug("当前节点审批结果：{}", variable);
		Boolean approve = Convert.toBool(variable);

		if (multipleMode == ProcessInstanceConstant.MULTIPLE_MODE_AL_SAME
				|| multipleMode == ProcessInstanceConstant.MULTIPLE_MODE_ALL_SORT) {
			// 如果是会签或者顺序签署
			if (!approve) {
				return true;
			}
		}

		if (multipleMode == ProcessInstanceConstant.MULTIPLE_MODE_ONE) {
			// 如果是或签
			if (approve) {
				return true;
			}
		}

		int nrOfInstances = (int) execution.getVariable("nrOfInstances");
		int nrOfCompletedInstances = (int) execution.getVariable("nrOfCompletedInstances");
		log.debug("当前节点完成实例数：{}  总实例数:{} 需要完成比例:{}", nrOfCompletedInstances, nrOfInstances, modePercentage);

		if (multipleMode == ProcessInstanceConstant.MULTIPLE_MODE_AL_SAME) {
			return BigDecimal.valueOf(nrOfCompletedInstances * 100L)
				.compareTo(BigDecimal.valueOf(nrOfCompletedInstances).multiply(modePercentage)) > 0;
		}

		return nrOfCompletedInstances == nrOfInstances;
	}

}
