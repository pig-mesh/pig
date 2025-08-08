package com.pig4cloud.pigx.flow.support.listeners;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.dto.*;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import com.pig4cloud.pigx.flow.service.IRemoteService;
import com.pig4cloud.pigx.flow.support.utils.NodeUtil;
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
 * 流程全局事件监听器
 * <p>
 * Flowable全局事件监听器实现，用于监听和处理流程执行过程中的各种事件。 该监听器是流程引擎与业务系统交互的核心组件，负责将流程引擎的事件转换为业务事件。
 * <p>
 * 监听的主要事件类型： 1. 节点事件：节点开始、节点完成、多实例节点完成 2. 任务事件：任务分配、任务完成 3. 流程事件：流程开始、流程结束 4.
 * 变量事件：变量创建、变量更新、变量删除
 * <p>
 * 主要功能： - 记录流程执行轨迹 - 触发业务系统的相关操作 - 发送任务通知 - 同步流程数据到业务系统
 *
 * @author pigx
 */
@Slf4j
public class FlowProcessEventListener implements FlowableEventListener {

	/**
	 * 处理Flowable引擎触发的事件
	 * <p>
	 * 该方法是事件监听的核心，根据事件类型分发到不同的处理逻辑。 每种事件类型都会触发相应的业务操作，如记录日志、发送通知等。
	 * @param event Flowable引擎触发的事件对象，包含事件类型和相关数据
	 * @throws Exception 当事件处理失败时可能抛出异常
	 */
	@SneakyThrows
	@Override
	public void onEvent(FlowableEvent event) {
		IRemoteService remoteService = SpringUtil.getBean(IRemoteService.class);
		IProcessNodeDataService processNodeDataService = SpringUtil.getBean(IProcessNodeDataService.class);

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
			Node node = processNodeDataService.getNodeData(flowId, activityId).getData();

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
			remoteService.startNodeEvent(processNodeRecordParamDto);
		}

		// 多实例任务完成事件处理
		// 触发时机：当多实例任务节点完成时，包括条件完成和全部完成两种情况
		if (event.getType()
			.toString()
			.equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED_WITH_CONDITION.toString())
				|| event.getType()
					.toString()
					.equals(FlowableEngineEventType.MULTI_INSTANCE_ACTIVITY_COMPLETED.toString())) {

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

			remoteService.endNodeEvent(processNodeRecordParamDto);

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

			remoteService.endNodeEvent(processNodeRecordParamDto);

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
		if (event.getType()
			.toString()
			.equals(FlowableEngineEventType.PROCESS_COMPLETED_WITH_TERMINATE_END_EVENT.toString())) {
			FlowableProcessTerminatedEventImpl e = (FlowableProcessTerminatedEventImpl) event;
			DelegateExecution execution = e.getExecution();
			String processInstanceId = e.getProcessInstanceId();
			ExecutionEntityImpl entity = (ExecutionEntityImpl) e.getEntity();

			log.info("流程终止完成事件 - 流程实例ID: {}", processInstanceId);

			String processDefinitionId = execution.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId); // 流程id

			// 构建流程结束参数并发送事件
			ProcessInstanceParamDto processInstanceParamDto = new ProcessInstanceParamDto();
			processInstanceParamDto.setProcessInstanceId(processInstanceId);
			processInstanceParamDto.setFlowId(flowId);
			processInstanceParamDto.setParamMap(execution.getVariables());
			remoteService.endProcess(processInstanceParamDto);

			// 发送任务完成通知
			SpringUtil.getBean(NodeTaskCompleteNotify.class)
				.sendFlowNotify(SecurityUtils.getToken(), processInstanceParamDto);
		}

		ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);

		// 任务完成事件处理
		// 触发时机：当用户任务被完成时
		if (event.getType().toString().equals(FlowableEngineEventType.TASK_COMPLETED.toString())) {
			TaskService taskService = SpringUtil.getBean(TaskService.class);

			FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
			TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();

			// 获取任务相关信息
			String assignee = task.getAssignee(); // 执行人id
			String taskDefinitionKey = task.getTaskDefinitionKey(); // 节点id
			String processInstanceId = task.getProcessInstanceId(); // 实例id
			String processDefinitionId = task.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId); // 流程id

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

			remoteService.taskEndEvent(processNodeRecordAssignUserParamDto);

			// 发送任务完成通知
			SpringUtil.getBean(NodeTaskCompleteNotify.class)
				.sendNotify(SecurityUtils.getToken(), processNodeRecordAssignUserParamDto);
		}

		// 任务分配事件处理
		// 触发时机：当任务被分配给特定用户时
		if (event.getType().toString().equals(FlowableEngineEventType.TASK_ASSIGNED.toString())) {
			FlowableEntityEvent flowableEntityEvent = (FlowableEntityEvent) event;
			TaskEntityImpl task = (TaskEntityImpl) flowableEntityEvent.getEntity();

			// 获取任务分配相关信息
			String assignee = task.getAssignee(); // 执行人id
			String owner = task.getOwner(); // 任务拥有者
			String delegationStateString = task.getDelegationStateString(); // 委派状态
			String taskDefinitionKey = task.getTaskDefinitionKey(); // 节点id
			String processInstanceId = task.getProcessInstanceId(); // 实例id
			String processDefinitionId = task.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId); // 流程id

			log.info("任务分配事件 - 任务ID: {}, 节点ID: {}, 分配给: {}, 委派状态: {}", task.getId(), taskDefinitionKey, assignee,
					delegationStateString);

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

			remoteService.startAssignUser(processNodeRecordAssignUserParamDto);
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
				String nestedProcessInstanceId = flowableProcessStartedEvent.getNestedProcessInstanceId(); // 上级实例id
				String flowId = entity.getProcessDefinitionKey(); // 流程定义key

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
				remoteService.createProcessEvent(processInstanceRecordParamDto);
			}
		}
	}

	/**
	 * 确定监听器抛出异常时是否终止当前操作
	 * <p>
	 * 返回false表示即使监听器处理出现异常，流程引擎仍会继续执行。 这确保了监听器的异常不会影响流程的正常执行。
	 * @return false，表示监听器异常不终止流程执行
	 */
	@Override
	public boolean isFailOnException() {
		return false;
	}

	/**
	 * 确定监听器是否在事务生命周期事件发生时立即触发
	 * <p>
	 * 返回false表示监听器不需要在事务生命周期事件上立即触发。 这意味着监听器将在正常的事件触发时机执行。
	 * @return false，表示不在事务生命周期事件上触发
	 */
	@Override
	public boolean isFireOnTransactionLifecycleEvent() {
		return false;
	}

	/**
	 * 获取在当前事务的生命周期中的触发点
	 * <p>
	 * 返回null表示监听器不需要在特定的事务生命周期触发点触发。 可选值包括：committed、committing、rolled-back等。
	 * @return null，表示没有指定特定的事务触发点
	 */
	@Override
	public String getOnTransaction() {
		return null;
	}

}