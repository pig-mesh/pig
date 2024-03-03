package com.pig4cloud.pigx.flow.engine.node;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.task.api.feign.RemoteFlowTaskService;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;
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

@Slf4j
@RequiredArgsConstructor
@Component("multiInstanceHandler")
public class MultiInstanceHandler {

	private final RemoteFlowTaskService remoteFlowTaskService;

	private final Map<String, AssignUserStrategy> assignUserStrategyMap;

	private final ObjectMapper objectMapper;

	/**
	 * 解析执行人
	 * @param execution 流程执行对象
	 * @return 执行人集合
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
		Node node = remoteFlowTaskService.queryNodeOriData(flowId, nodeId).getData();
		if (node != null) {
			Map<String, Object> variables = execution.getVariables();
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
	 * 会签或者或签完成条件检查 检查节点是否满足会签或者或签的完成条件
	 * @param execution 执行实例对象
	 * @return boolean 如果节点满足条件则返回true，否则返回false
	 */
	public boolean completionCondition(DelegateExecution execution) {
		ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
		String processDefinitionKey = entity.getProcessDefinitionKey();
		String nodeId = execution.getCurrentActivityId();

		Node node = remoteFlowTaskService.queryNodeOriData(processDefinitionKey, nodeId).getData();
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
