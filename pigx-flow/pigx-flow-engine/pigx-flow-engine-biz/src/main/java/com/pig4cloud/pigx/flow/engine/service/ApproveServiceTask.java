package com.pig4cloud.pigx.flow.engine.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pigx.flow.task.api.feign.RemoteFlowTaskService;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.Refuse;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

/**
 * 审批任务处理器--java服务任务
 */
@Slf4j
public class ApproveServiceTask implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) {

		ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
		String nodeIdO = entity.getActivityId();
		String flowId = entity.getProcessDefinitionKey();
		String processInstanceId = entity.getProcessInstanceId();

		String nodeId = StrUtil.subAfter(nodeIdO, "approve_service_task_", true);

		Boolean approve = execution.getVariable(StrUtil.format("{}_approve_condition", nodeId), Boolean.class);

		if (approve != null) {

			RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);

			if (!approve) {
				// 跳转
				RemoteFlowTaskService remoteFlowTaskService = SpringUtil.getBean(RemoteFlowTaskService.class);
				Node node = remoteFlowTaskService.queryNodeOriData(flowId, nodeId).getData();
				Refuse refuse = node.getRefuse();
				if (refuse != null) {
					String handler = refuse.getHandler();
					if (StrUtil.equals(handler, "TO_NODE")) {
						runtimeService.createChangeActivityStateBuilder()
							.processInstanceId(processInstanceId)
							.moveActivityIdTo(nodeIdO, refuse.getNodeId())
							.changeState();
					}
					else {
						runtimeService.createChangeActivityStateBuilder()
							.processInstanceId(processInstanceId)
							.moveActivityIdTo(nodeIdO, "end")
							.changeState();
					}
				}

			}

		}

	}

}
