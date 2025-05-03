package com.pig4cloud.pigx.flow.engine.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pigx.flow.task.api.feign.RemoteFlowTaskService;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.Refuse;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

/**
 * 审批服务任务类，负责处理工作流中的审批逻辑
 * 该类根据审批条件确定工作流的下一步流程
 */
public class ApproveServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        // 转换为实现类以访问额外的方法
        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;

        // 从执行上下文中提取关键标识符
        String currentActivityId = entity.getActivityId();   // 当前活动ID
        String flowDefinitionKey = entity.getProcessDefinitionKey();  // 流程定义键
        String processInstanceId = entity.getProcessInstanceId();  // 流程实例ID

        // 从活动ID中提取节点标识符
        // 如果currentActivityId是"approve_service_task_node1"，则nodeId将是"node1"
        String nodeId = StrUtil.subAfter(currentActivityId, "approve_service_task_", true);

        // 获取此节点的审批条件变量
        // 变量名格式："{nodeId}_approve_condition"（例如："node1_approve_condition"）
        String conditionVarName = StrUtil.format("{}_approve_condition", nodeId);
        Boolean isApproved = execution.getVariable(conditionVarName, Boolean.class);

        // 只有当审批条件明确设置时才继续处理
        if (isApproved != null) {
            // 获取Flowable RuntimeService bean，用于工作流状态变更
            RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);

            // 如果未批准，确定工作流重定向的位置
            if (!isApproved) {
                handleRejection(flowDefinitionKey, nodeId, currentActivityId, processInstanceId, runtimeService);
            }
            // 如果已批准，工作流将沿其正常路径继续（无需操作）
        }
    }

    /**
     * 处理任务被拒绝时的工作流重定向逻辑
     *
     * @param flowDefinitionKey 流程定义键
     * @param nodeId            当前节点标识符
     * @param currentActivityId 当前活动标识符
     * @param processInstanceId 流程实例标识符
     * @param runtimeService    用于工作流状态操作的运行时服务
     */
    private void handleRejection(String flowDefinitionKey, String nodeId,
                                 String currentActivityId, String processInstanceId,
                                 RuntimeService runtimeService) {
        // 获取远程流程任务服务以获取节点配置数据
        RemoteFlowTaskService remoteFlowTaskService = SpringUtil.getBean(RemoteFlowTaskService.class);

        // 查询节点的原始配置数据
        Node node = remoteFlowTaskService.queryNodeOriData(flowDefinitionKey, nodeId).getData();

        // 获取拒绝处理配置
        Refuse refuseConfig = node.getRefuse();

        if (refuseConfig != null) {
            String rejectionHandler = refuseConfig.getHandler();

            // 为当前流程实例创建状态变更构建器
            var stateChangeBuilder = runtimeService.createChangeActivityStateBuilder()
                    .processInstanceId(processInstanceId);

            if (StrUtil.equals(rejectionHandler, "TO_NODE")) {
                // 根据拒绝设置重定向到特定节点
                stateChangeBuilder.moveActivityIdTo(currentActivityId, refuseConfig.getNodeId())
                        .changeState();
            } else {
                // 默认行为：重定向到结束节点
                stateChangeBuilder.moveActivityIdTo(currentActivityId, "end")
                        .changeState();
            }
        }
        // 如果未设置refuseConfig，工作流将沿其正常路径继续
    }
}