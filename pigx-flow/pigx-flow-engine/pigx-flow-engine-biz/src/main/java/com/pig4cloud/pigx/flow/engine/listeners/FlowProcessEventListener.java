package com.pig4cloud.pigx.flow.engine.listeners;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.engine.service.NodeTaskCompleteNotifyService;
import com.pig4cloud.pigx.flow.task.api.feign.RemoteFlowTaskService;
import com.pig4cloud.pigx.flow.task.dto.*;
import com.pig4cloud.pigx.flow.task.utils.NodeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
import org.flowable.common.engine.api.delegate.event.FlowableEntityEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.event.impl.FlowableActivityEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableMultiInstanceActivityCompletedEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableProcessStartedEventImpl;
import org.flowable.engine.delegate.event.impl.FlowableProcessTerminatedEventImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.task.api.DelegationState;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.flowable.variable.api.event.FlowableVariableEvent;

import java.util.List;
import java.util.Map;

/**
 * 流程监听器
 */
@Slf4j
public class FlowProcessEventListener implements FlowableEventListener {

	/**
	 * 当事件被触发时调用
	 * @param event 事件对象
	 */
	@SneakyThrows
	@Override
	public void onEvent(FlowableEvent event) {
		RemoteFlowTaskService remoteFlowTaskService = SpringUtil.getBean(RemoteFlowTaskService.class);

		log.info("分支监听器 类型={} class={}", event.getType(), event.getClass().getCanonicalName());
		if (event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_STARTED.toString())) {
			// 节点开始执行
			FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
			String activityId = flowableActivityEvent.getActivityId();
			String activityName = flowableActivityEvent.getActivityName();
			log.info("节点id：{} 名字:{}", activityId, activityName);

			String processInstanceId = flowableActivityEvent.getProcessInstanceId();

			String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId);

			Node node = remoteFlowTaskService.queryNodeOriData(flowId, activityId).getData();

			ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
			processNodeRecordParamDto.setFlowId(flowId);
			processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
			processNodeRecordParamDto.setNodeId(activityId);
			if (node != null) {
				processNodeRecordParamDto.setNodeType(String.valueOf(node.getType()));
			}
			processNodeRecordParamDto.setNodeName(activityName);
			processNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
			remoteFlowTaskService.startNodeEvent(processNodeRecordParamDto);
		}

		if (event.getType()
			.toString()
			.equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION.toString())
				|| event.getType()
					.toString()
					.equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED.toString())) {
			// 多实例任务
			FlowableMultiInstanceActivityCompletedEventImpl flowableActivityEvent = (FlowableMultiInstanceActivityCompletedEventImpl) event;
			String activityId = flowableActivityEvent.getActivityId();
			String activityName = flowableActivityEvent.getActivityName();
			log.info("节点id：{} 名字:{}", activityId, activityName);

			String processInstanceId = flowableActivityEvent.getProcessInstanceId();

			String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId);

			ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
			processNodeRecordParamDto.setFlowId(flowId);
			processNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
			processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
			// processNodeRecordParamDto.setData(JSON.toJSONString(processVariables));
			processNodeRecordParamDto.setNodeId(activityId);
			// processNodeRecordParamDto.setNodeType(nodeDto.getType());
			processNodeRecordParamDto.setNodeName(activityName);

			remoteFlowTaskService.endNodeEvent(processNodeRecordParamDto);
		}
		if (event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_COMPLETED.toString())) {
			// 节点完成执行

			FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
			String activityId = flowableActivityEvent.getActivityId();
			String activityName = flowableActivityEvent.getActivityName();
			log.info("节点id：{} 名字:{}", activityId, activityName);

			String processInstanceId = flowableActivityEvent.getProcessInstanceId();

			String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId);

			ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
			processNodeRecordParamDto.setFlowId(flowId);
			processNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
			processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
			processNodeRecordParamDto.setNodeId(activityId);
			processNodeRecordParamDto.setNodeName(activityName);

			remoteFlowTaskService.endNodeEvent(processNodeRecordParamDto);
		}

		if (event.getType().toString().equals(FlowableEngineEventType.VARIABLE_UPDATED.toString())) {
			// 变量变化了
			FlowableVariableEvent flowableVariableEvent = (FlowableVariableEvent) event;
			log.debug("变量[{}]变化了:{}  ", flowableVariableEvent.getVariableName(),
					flowableVariableEvent.getVariableValue());
		}

		if (event.getType().toString().equals(FlowableEngineEventType.VARIABLE_CREATED.toString())) {
			// 变量创建了
			FlowableVariableEvent flowableVariableEvent = (FlowableVariableEvent) event;
			log.debug("变量[{}]创建了:{} ", flowableVariableEvent.getVariableName(),
					flowableVariableEvent.getVariableValue());
		}
		if (event.getType().toString().equals(FlowableEngineEventType.VARIABLE_DELETED.toString())) {
			// 变量删除了
			FlowableVariableEvent flowableVariableEvent = (FlowableVariableEvent) event;
			log.debug("变量[{}]删除了:{} ", flowableVariableEvent.getVariableName(),
					flowableVariableEvent.getVariableValue());
		}
		if (event.getType()
			.toString()
			.equals(FlowableEngineEventType.PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT.toString())) {
			// 流程开完成
			FlowableProcessTerminatedEventImpl e = (FlowableProcessTerminatedEventImpl) event;
			DelegateExecution execution = e.getExecution();
			String processInstanceId = e.getProcessInstanceId();
			ExecutionEntityImpl entity = (ExecutionEntityImpl) e.getEntity();

			ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
			processInstanceParamDto.setProcessInstanceId(processInstanceId);
			remoteFlowTaskService.endProcessEvent(processInstanceParamDto);

		}

		ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
		if (event.getType().toString().equals(FlowableEngineEventType.TASK_COMPLETED.toString())) {

			TaskService taskService = SpringUtil.getBean(TaskService.class);

			// 任务完成
			FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
			TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();
			// 执行人id
			String assignee = task.getAssignee();

			// nodeid
			String taskDefinitionKey = task.getTaskDefinitionKey();

			// 实例id
			String processInstanceId = task.getProcessInstanceId();

			String processDefinitionId = task.getProcessDefinitionId();
			// 流程id
			String flowId = NodeUtil.getFlowId(processDefinitionId);
			ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto = new ProcessNodeRecordAssignUserParamDto();
			processNodeRecordAssignUserParamDto.setFlowId(flowId);
			processNodeRecordAssignUserParamDto.setProcessInstanceId(processInstanceId);
			processNodeRecordAssignUserParamDto
				.setData(objectMapper.writeValueAsString(taskService.getVariables(task.getId())));
			processNodeRecordAssignUserParamDto
				.setLocalData(objectMapper.writeValueAsString(taskService.getVariablesLocal(task.getId())));
			processNodeRecordAssignUserParamDto.setNodeId(taskDefinitionKey);
			processNodeRecordAssignUserParamDto.setUserId(Long.parseLong(task.getAssignee()));
			processNodeRecordAssignUserParamDto.setTaskId(task.getId());
			processNodeRecordAssignUserParamDto.setNodeName(task.getName());
			processNodeRecordAssignUserParamDto.setTaskType("COMPLETE");
			processNodeRecordAssignUserParamDto.setApproveDesc(Convert.toStr(task.getVariableLocal("approveDesc")));
			processNodeRecordAssignUserParamDto.setExecutionId(task.getExecutionId());

			remoteFlowTaskService.taskEndEvent(processNodeRecordAssignUserParamDto);


			SpringUtil.getBean(NodeTaskCompleteNotifyService.class).sendNotify(SecurityUtils.getToken(),processNodeRecordAssignUserParamDto);
		}
		if (event.getType().toString().equals(FlowableEngineEventType.TASK_ASSIGNED.toString())) {
			// 任务被指派了人员
			FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
			TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();
			// 执行人id
			String assignee = task.getAssignee();
			// 任务拥有者
			String owner = task.getOwner();
			//
			String delegationStateString = task.getDelegationStateString();

			// nodeid
			String taskDefinitionKey = task.getTaskDefinitionKey();

			// 实例id
			String processInstanceId = task.getProcessInstanceId();

			String processDefinitionId = task.getProcessDefinitionId();
			// 流程id
			String flowId = NodeUtil.getFlowId(processDefinitionId);
			ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto = new ProcessNodeRecordAssignUserParamDto();
			processNodeRecordAssignUserParamDto.setFlowId(flowId);
			processNodeRecordAssignUserParamDto.setProcessInstanceId(processInstanceId);
			// processNodeRecordAssignUserParamDto.setData();
			processNodeRecordAssignUserParamDto.setNodeId(taskDefinitionKey);
			processNodeRecordAssignUserParamDto.setUserId(Long.parseLong(task.getAssignee()));
			processNodeRecordAssignUserParamDto.setTaskId(task.getId());
			processNodeRecordAssignUserParamDto.setNodeName(task.getName());
			processNodeRecordAssignUserParamDto
				.setTaskType(StrUtil.equals(DelegationState.PENDING.toString(), delegationStateString) ? "DELEGATION"
						: (StrUtil.equals(DelegationState.RESOLVED.toString(), delegationStateString) ? "RESOLVED"
								: ""));
			processNodeRecordAssignUserParamDto.setApproveDesc(Convert.toStr(task.getVariableLocal("approveDesc")));
			processNodeRecordAssignUserParamDto.setExecutionId(task.getExecutionId());

			remoteFlowTaskService.startAssignUser(processNodeRecordAssignUserParamDto);

		}

		if (event.getType().toString().equals(FlowableEngineEventType.PROCESS_STARTED.toString())) {
			// 流程开始了
			FlowableProcessStartedEventImpl flowableProcessStartedEvent = (FlowableProcessStartedEventImpl) event;

			ExecutionEntityImpl entity = (ExecutionEntityImpl) flowableProcessStartedEvent.getEntity();
			DelegateExecution execution = flowableProcessStartedEvent.getExecution();
			String processInstanceId = flowableProcessStartedEvent.getProcessInstanceId();
			{
				// 上级实例id
				String nestedProcessInstanceId = flowableProcessStartedEvent.getNestedProcessInstanceId();

				String flowId = entity.getProcessDefinitionKey();

				Object variable = execution.getVariable("root");

				List<NodeUser> nodeUsers = objectMapper.readValue(objectMapper.writeValueAsString(variable),
						new TypeReference<>() {
						});
				Long startUserId = nodeUsers.get(0).getId();
				Map<String, Object> variables = execution.getVariables();

				ProcessInstanceRecordParamDto processInstanceRecordParamDto = new ProcessInstanceRecordParamDto();
				processInstanceRecordParamDto.setUserId(startUserId);
				processInstanceRecordParamDto.setParentProcessInstanceId(nestedProcessInstanceId);
				processInstanceRecordParamDto.setFlowId(flowId);
				processInstanceRecordParamDto.setProcessInstanceId(processInstanceId);
				processInstanceRecordParamDto.setFormData(objectMapper.writeValueAsString(variables));
				remoteFlowTaskService.createProcessEvent(processInstanceRecordParamDto);
			}

		}
	}

	/**
	 * 如果监听器抛出异常是否终止当前操作
	 * @return 是否终止当前操作
	 */
	@Override
	public boolean isFailOnException() {
		return false;
	}

	/**
	 * 返回监听器是否在事务生命周期事件发生时立即触发
	 * @return 是否在事务生命周期事件上触发
	 */
	@Override
	public boolean isFireOnTransactionLifecycleEvent() {
		return false;
	}

	/**
	 * 如果非空,表示在当前事务的生命周期中的触发点
	 * @return 事务生命周期中的触发点
	 */
	@Override
	public String getOnTransaction() {
		return null;
	}

}
