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
 * 流程事件监听器
 * 用于监听流程执行过程中的各种事件，并执行相应的操作
 * 包括节点开始/结束、任务分配/完成、流程开始/结束等事件
 */
@Slf4j
public class FlowProcessEventListener implements FlowableEventListener {

	/**
	 * 当事件被触发时调用的方法
	 * 根据不同事件类型执行不同的业务逻辑
	 *
	 * @param event Flowable引擎触发的事件对象
	 */
	@SneakyThrows
	@Override
	public void onEvent(FlowableEvent event) {
		RemoteFlowTaskService remoteFlowTaskService = SpringUtil.getBean(RemoteFlowTaskService.class);

		log.info("流程事件监听器 类型={} class={}", event.getType(), event.getClass().getCanonicalName());

		// 节点开始事件处理
		// 触发时机：当流程中的节点开始执行时
		if (event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_STARTED.toString())) {
			// 获取节点信息
			FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
			String activityId = flowableActivityEvent.getActivityId();
			String activityName = flowableActivityEvent.getActivityName();
			log.info("节点开始执行 - 节点id：{} 名字:{}", activityId, activityName);

			String processInstanceId = flowableActivityEvent.getProcessInstanceId();
			String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId);

			// 查询节点原始数据
			Node node = remoteFlowTaskService.queryNodeOriData(flowId, activityId).getData();

			// 构建节点开始记录参数并发送事件
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

		// 多实例任务完成事件处理
		// 触发时机：当多实例任务节点完成时，包括条件完成和全部完成两种情况
		if (event.getType().toString().equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION.toString())
				|| event.getType().toString().equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED.toString())) {

			FlowableMultiInstanceActivityCompletedEventImpl flowableActivityEvent = (FlowableMultiInstanceActivityCompletedEventImpl) event;
			String activityId = flowableActivityEvent.getActivityId();
			String activityName = flowableActivityEvent.getActivityName();
			log.info("多实例任务完成 - 节点id：{} 名字:{}", activityId, activityName);

			String processInstanceId = flowableActivityEvent.getProcessInstanceId();
			String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId);

			// 构建多实例节点完成记录参数并发送事件
			ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
			processNodeRecordParamDto.setFlowId(flowId);
			processNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
			processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
			processNodeRecordParamDto.setNodeId(activityId);
			processNodeRecordParamDto.setNodeName(activityName);

			remoteFlowTaskService.endNodeEvent(processNodeRecordParamDto);
		}

		// 节点完成事件处理
		// 触发时机：当流程中的节点执行完成时
		if (event.getType().toString().equals(FlowableEngineEventType.ACTIVITY_COMPLETED.toString())) {
			FlowableActivityEventImpl flowableActivityEvent = (FlowableActivityEventImpl) event;
			String activityId = flowableActivityEvent.getActivityId();
			String activityName = flowableActivityEvent.getActivityName();
			log.info("节点执行完成 - 节点id：{} 名字:{}", activityId, activityName);

			String processInstanceId = flowableActivityEvent.getProcessInstanceId();
			String processDefinitionId = flowableActivityEvent.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId);

			// 构建节点完成记录参数并发送事件
			ProcessNodeRecordParamDto processNodeRecordParamDto = new ProcessNodeRecordParamDto();
			processNodeRecordParamDto.setFlowId(flowId);
			processNodeRecordParamDto.setExecutionId(flowableActivityEvent.getExecutionId());
			processNodeRecordParamDto.setProcessInstanceId(processInstanceId);
			processNodeRecordParamDto.setNodeId(activityId);
			processNodeRecordParamDto.setNodeName(activityName);

			remoteFlowTaskService.endNodeEvent(processNodeRecordParamDto);
		}

		// 变量更新事件处理
		// 触发时机：当流程变量被更新时
		if (event.getType().toString().equals(FlowableEngineEventType.VARIABLE_UPDATED.toString())) {
			FlowableVariableEvent flowableVariableEvent = (FlowableVariableEvent) event;
			log.debug("变量更新事件 - 变量[{}]变化为: {} ", flowableVariableEvent.getVariableName(),
					flowableVariableEvent.getVariableValue());
		}

		// 变量创建事件处理
		// 触发时机：当流程变量被创建时
		if (event.getType().toString().equals(FlowableEngineEventType.VARIABLE_CREATED.toString())) {
			FlowableVariableEvent flowableVariableEvent = (FlowableVariableEvent) event;
			log.debug("变量创建事件 - 变量[{}]创建，值为: {} ", flowableVariableEvent.getVariableName(),
					flowableVariableEvent.getVariableValue());
		}

		// 变量删除事件处理
		// 触发时机：当流程变量被删除时
		if (event.getType().toString().equals(FlowableEngineEventType.VARIABLE_DELETED.toString())) {
			FlowableVariableEvent flowableVariableEvent = (FlowableVariableEvent) event;
			log.debug("变量删除事件 - 变量[{}]被删除，值为: {} ", flowableVariableEvent.getVariableName(),
					flowableVariableEvent.getVariableValue());
		}

		// 流程终止事件处理
		// 触发时机：当流程以终止结束事件完成时
		if (event.getType().toString().equals(FlowableEngineEventType.PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT.toString())) {
			FlowableProcessTerminatedEventImpl e = (FlowableProcessTerminatedEventImpl) event;
			DelegateExecution execution = e.getExecution();
			String processInstanceId = e.getProcessInstanceId();
			ExecutionEntityImpl entity = (ExecutionEntityImpl) e.getEntity();

			log.info("流程终止完成事件 - 流程实例ID: {}", processInstanceId);

			// 构建流程结束参数并发送事件
			ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
			processInstanceParamDto.setProcessInstanceId(processInstanceId);
			remoteFlowTaskService.endProcessEvent(processInstanceParamDto);
		}

		ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);

		// 任务完成事件处理
		// 触发时机：当用户任务被完成时
		if (event.getType().toString().equals(FlowableEngineEventType.TASK_COMPLETED.toString())) {
			TaskService taskService = SpringUtil.getBean(TaskService.class);

			FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
			TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();

			// 获取任务相关信息
			String assignee = task.getAssignee();  // 执行人id
			String taskDefinitionKey = task.getTaskDefinitionKey();  // 节点id
			String processInstanceId = task.getProcessInstanceId();  // 实例id
			String processDefinitionId = task.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId);  // 流程id

			log.info("任务完成事件 - 任务ID: {}, 节点ID: {}, 执行人: {}", task.getId(), taskDefinitionKey, assignee);

			// 构建任务完成记录参数并发送事件
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

			// 发送任务完成通知
			SpringUtil.getBean(NodeTaskCompleteNotifyService.class).sendNotify(SecurityUtils.getToken(), processNodeRecordAssignUserParamDto);
		}

		// 任务分配事件处理
		// 触发时机：当任务被分配给特定用户时
		if (event.getType().toString().equals(FlowableEngineEventType.TASK_ASSIGNED.toString())) {
			FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
			TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();

			// 获取任务分配相关信息
			String assignee = task.getAssignee();  // 执行人id
			String owner = task.getOwner();  // 任务拥有者
			String delegationStateString = task.getDelegationStateString();  // 委派状态
			String taskDefinitionKey = task.getTaskDefinitionKey();  // 节点id
			String processInstanceId = task.getProcessInstanceId();  // 实例id
			String processDefinitionId = task.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId);  // 流程id

			log.info("任务分配事件 - 任务ID: {}, 节点ID: {}, 分配给: {}, 委派状态: {}",
					task.getId(), taskDefinitionKey, assignee, delegationStateString);

			// 构建任务分配记录参数并发送事件
			ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto = new ProcessNodeRecordAssignUserParamDto();
			processNodeRecordAssignUserParamDto.setFlowId(flowId);
			processNodeRecordAssignUserParamDto.setProcessInstanceId(processInstanceId);
			processNodeRecordAssignUserParamDto.setNodeId(taskDefinitionKey);
			processNodeRecordAssignUserParamDto.setUserId(Long.parseLong(task.getAssignee()));
			processNodeRecordAssignUserParamDto.setTaskId(task.getId());
			processNodeRecordAssignUserParamDto.setNodeName(task.getName());

			// 根据委派状态设置任务类型
			processNodeRecordAssignUserParamDto
					.setTaskType(StrUtil.equals(DelegationState.PENDING.toString(), delegationStateString) ? "DELEGATION"
							: (StrUtil.equals(DelegationState.RESOLVED.toString(), delegationStateString) ? "RESOLVED"
							: ""));

			processNodeRecordAssignUserParamDto.setApproveDesc(Convert.toStr(task.getVariableLocal("approveDesc")));
			processNodeRecordAssignUserParamDto.setExecutionId(task.getExecutionId());

			remoteFlowTaskService.startAssignUser(processNodeRecordAssignUserParamDto);
		}

		// 流程开始事件处理
		// 触发时机：当流程实例被启动时
		if (event.getType().toString().equals(FlowableEngineEventType.PROCESS_STARTED.toString())) {
			FlowableProcessStartedEventImpl flowableProcessStartedEvent = (FlowableProcessStartedEventImpl) event;

			ExecutionEntityImpl entity = (ExecutionEntityImpl) flowableProcessStartedEvent.getEntity();
			DelegateExecution execution = flowableProcessStartedEvent.getExecution();
			String processInstanceId = flowableProcessStartedEvent.getProcessInstanceId();

			log.info("流程开始事件 - 流程实例ID: {}", processInstanceId);

			{
				// 获取流程相关信息
				String nestedProcessInstanceId = flowableProcessStartedEvent.getNestedProcessInstanceId();  // 上级实例id
				String flowId = entity.getProcessDefinitionKey();  // 流程定义key

				// 获取启动人信息
				Object variable = execution.getVariable("root");
				List<NodeUser> nodeUsers = objectMapper.readValue(objectMapper.writeValueAsString(variable),
						new TypeReference<>() {
						});
				Long startUserId = nodeUsers.get(0).getId();

				// 获取流程变量
				Map<String, Object> variables = execution.getVariables();

				// 构建流程实例记录参数并发送事件
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
	 * 确定监听器抛出异常是否终止当前操作
	 * 返回false表示即使监听器出现异常，流程引擎仍会继续执行
	 *
	 * @return 是否在监听器异常时终止操作
	 */
	@Override
	public boolean isFailOnException() {
		return false;
	}

	/**
	 * 确定监听器是否在事务生命周期事件发生时立即触发
	 * 返回false表示不在事务生命周期事件上立即触发
	 *
	 * @return 是否在事务生命周期事件上触发
	 */
	@Override
	public boolean isFireOnTransactionLifecycleEvent() {
		return false;
	}

	/**
	 * 获取在当前事务的生命周期中的触发点
	 * 返回null表示不在特定事务生命周期触发点触发
	 *
	 * @return 事务生命周期中的触发点，null表示没有指定
	 */
	@Override
	public String getOnTransaction() {
		return null;
	}
}