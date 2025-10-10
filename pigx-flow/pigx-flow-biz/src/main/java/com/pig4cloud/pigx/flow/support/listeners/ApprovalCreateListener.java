package com.pig4cloud.pigx.flow.support.listeners;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Nobody;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import com.pig4cloud.pigx.flow.service.IRemoteService;
import com.pig4cloud.pigx.flow.support.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;

/**
 * 审批任务创建监听器
 * <p>
 * Flowable任务监听器实现，用于处理审批任务创建时的特殊逻辑。
 * 主要功能是处理没有指定执行人的任务，根据节点配置的"无人处理策略"自动执行相应操作。
 * <p>
 * 支持的无人处理策略：
 * 1. 自动通过：任务自动完成并通过
 * 2. 指派给管理员：将任务分配给流程管理员
 * 3. 指派给特定用户：将任务分配给配置中指定的用户
 * 4. 自动拒绝：任务自动完成并拒绝
 * <p>
 * 该监听器需要在流程定义的用户任务节点中配置，通过taskListener扩展属性指定。
 *
 * @author pigx
 */
@Slf4j
public class ApprovalCreateListener implements TaskListener {

	/**
	 * 任务创建事件处理方法
	 * <p>
	 * 当任务被创建时触发，检查任务是否有执行人，如果没有则根据配置的策略自动处理。
	 * 这个机制解决了流程执行过程中可能出现的无人处理任务的问题。
	 *
	 * @param delegateTask 委派的任务对象，包含任务的所有信息和上下文
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		log.debug("任务类型: {}", delegateTask.getClass().getCanonicalName());
		TaskService taskService = SpringUtil.getBean(TaskService.class);

		// 获取任务的基本信息
		String assignee = delegateTask.getAssignee(); // 执行人
		String name = delegateTask.getName(); // 任务名称
		log.debug("任务创建事件 - 任务名称:{}, 执行人:{}", name, assignee);

		// 转换为TaskEntity以获取更多信息
		TaskEntityImpl taskEntity = (TaskEntityImpl) delegateTask;
		String nodeId = taskEntity.getTaskDefinitionKey(); // 节点ID
		String processDefinitionId = taskEntity.getProcessDefinitionId(); // 流程定义ID

		// 从流程定义ID中提取流程ID
		String flowId = NodeUtil.getFlowId(processDefinitionId);

		// 判断任务是否没有分配执行人或执行人为空标记
		// 如果没有执行人，需要根据无人处理策略决定下一步操作
		if (StrUtil.isBlank(assignee)
				|| StrUtil.equals(ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN.toString(), assignee)) {

			log.debug("任务无执行人，开始处理无人执行策略");
			IProcessNodeDataService processNodeDataService = SpringUtil.getBean(IProcessNodeDataService.class);
			IRemoteService remoteService = SpringUtil.getBean(IRemoteService.class);

			// 查询节点原始配置数据，获取无人处理策略
			Node node = processNodeDataService.getNodeData(flowId, nodeId).getData();
			Nobody nobody = node.getNobody(); // 获取无人处理配置
			String handler = nobody.getHandler(); // 获取无人处理的处理方式

			// 根据无人处理策略执行不同操作
			// 策略1: 自动通过任务
			if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_PASS)) {
				log.info("无人处理策略: 自动通过 - 任务ID: {}", taskEntity.getId());
				// 设置条件变量为true并完成任务，让流程继续执行
				Dict param = Dict.create().set(StrUtil.format("{}_approve_condition", nodeId), true);
				taskService.complete(taskEntity.getId(), param);
			}

			// 策略2: 指派给管理员
			if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_ADMIN)) {
				log.info("无人处理策略: 指派给管理员 - 任务ID: {}", taskEntity.getId());
				// 查询流程的管理员ID
				R<Long> longR = remoteService.queryProcessAdmin(flowId);
				Long adminId = longR.getData();

				// 将任务分配给管理员
				taskService.setAssignee(taskEntity.getId(), String.valueOf(adminId));
				log.debug("任务已指派给管理员, 管理员ID: {}", adminId);
			}

			// 策略3: 指派给特定用户
			if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_USER)) {
				log.info("无人处理策略: 指派给特定用户 - 任务ID: {}", taskEntity.getId());
				// 获取配置中指定的用户
				NodeUser nodeUser = nobody.getAssignedUser().get(0);

				// 将任务分配给指定用户
				taskService.setAssignee(taskEntity.getId(), nodeUser.getId().toString());
				log.debug("任务已指派给特定用户, 用户ID: {}", nodeUser.getId());
			}

			// 策略4: 自动拒绝任务
			if (StrUtil.equals(handler, ProcessInstanceConstant.USER_TASK_NOBODY_HANDLER_TO_REFUSE)) {
				log.info("无人处理策略: 自动拒绝 - 任务ID: {}", taskEntity.getId());
				// 设置条件变量为false并完成任务，通常会导致流程走向拒绝分支
				Dict param = Dict.create().set(StrUtil.format("{}_approve_condition", nodeId), false);
				taskService.complete(taskEntity.getId(), param);
			}
		}
		else {
			log.debug("任务已有执行人，无需处理无人执行策略");
		}
	}

}