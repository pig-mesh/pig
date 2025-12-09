package com.pig4cloud.pigx.flow.support.listeners;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;

/**
 * 发起人任务创建监听器
 * <p>
 * 用于处理发起人节点（root节点）的任务创建事件。
 * 该监听器的核心职责是区分"正常发起流程"和"驳回到发起人"两种场景：
 * <ul>
 *   <li>正常发起流程：自动完成root任务，让流程继续到第一个审批节点</li>
 *   <li>驳回到发起人：保留任务，让发起人可以在待办任务中看到并处理</li>
 * </ul>
 * </p>
 * <p>
 * 判断逻辑：
 * 通过检查流程变量 rejectToStarter 是否为 true 来判断是否是驳回场景。
 * 该变量在 ApproveServiceTask 处理驳回到发起人时设置。
 * </p>
 *
 * @author pigx
 * @see com.pig4cloud.pigx.flow.support.tasks.ApproveServiceTask
 */
@Slf4j
public class RootTaskCreateListener implements TaskListener {

	/**
	 * 任务创建事件处理方法
	 * <p>
	 * 当root任务被创建时触发。根据是否是驳回场景决定是否自动完成任务。
	 * </p>
	 *
	 * @param delegateTask 委派的任务对象
	 */
	@Override
	public void notify(DelegateTask delegateTask) {
		RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
		TaskService taskService = SpringUtil.getBean(TaskService.class);

		String processInstanceId = delegateTask.getProcessInstanceId();
		String taskId = delegateTask.getId();
		String assignee = delegateTask.getAssignee();

		log.debug("发起人任务创建 - 任务ID: {}, 执行人: {}, 流程实例ID: {}", taskId, assignee, processInstanceId);

		// 检查是否是驳回场景
		Object rejectToStarter = runtimeService.getVariable(processInstanceId, "rejectToStarter");

		if (!Boolean.TRUE.equals(rejectToStarter)) {
			// 非驳回场景：自动完成 root 任务，让流程继续到下一个节点
			log.debug("非驳回场景，自动完成发起人任务 - 任务ID: {}", taskId);
			taskService.complete(taskId);
		}
		else {
			// 驳回场景：保留任务给发起人处理
			// 发起人可以在待办任务列表中看到这个任务，并进行编辑重新提交
			log.info("驳回场景，保留任务给发起人 - 任务ID: {}, 执行人: {}", taskId, assignee);
		}
	}

}
