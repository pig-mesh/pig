package com.pig4cloud.pigx.flow.support.tasks;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.Refuse;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

/**
 * 审批服务任务
 * <p>
 * Flowable服务任务（Service Task）实现，用于处理审批节点的自动化逻辑。
 * 该任务在审批节点执行后自动触发，根据审批结果（通过/拒绝）决定流程的走向。
 * <p>
 * 主要功能：
 * 1. 读取审批节点的审批结果
 * 2. 如果审批通过，流程继续正常执行
 * 3. 如果审批拒绝，根据配置执行拒绝策略（跳转到指定节点或结束流程）
 * <p>
 * 使用方式：
 * 在流程定义中，将此类配置为审批节点后的服务任务，
 * 任务ID需要遵循命名规则：approve_service_task_{nodeId}
 *
 * @author pigx
 */
public class ApproveServiceTask implements JavaDelegate {

	/**
	 * 执行审批服务任务
	 * <p>
	 * 根据审批节点的审批结果决定流程的下一步走向。
	 * 如果审批通过，流程正常继续；如果审批拒绝，执行拒绝策略。
	 *
	 * @param execution 流程执行上下文，包含流程实例信息和变量
	 */
	@Override
	public void execute(DelegateExecution execution) {
		// 转换为实现类以访问额外的方法
		ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;

		// 从执行上下文中提取关键标识符
		String currentActivityId = entity.getActivityId(); // 当前活动ID
		String flowDefinitionKey = entity.getProcessDefinitionKey(); // 流程定义键
		String processInstanceId = entity.getProcessInstanceId(); // 流程实例ID

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
	 * 处理任务被拒绝时的流程重定向逻辑
	 * <p>
	 * 根据节点配置的拒绝策略，将流程重定向到指定节点或结束节点。
	 * 支持的拒绝策略：
	 * 1. TO_NODE：跳转到指定节点
	 * 2. 其他：直接结束流程
	 *
	 * @param flowDefinitionKey 流程定义键
	 * @param nodeId            当前节点标识符
	 * @param currentActivityId 当前活动标识符
	 * @param processInstanceId 流程实例标识符
	 * @param runtimeService    用于流程状态操作的运行时服务
	 */
	private void handleRejection(String flowDefinitionKey, String nodeId, String currentActivityId,
			String processInstanceId, RuntimeService runtimeService) {
		// 获取流程节点数据服务以获取节点配置数据
		IProcessNodeDataService processNodeDataService = SpringUtil.getBean(IProcessNodeDataService.class);

		// 查询节点的原始配置数据
		Node node = processNodeDataService.getNodeData(flowDefinitionKey, nodeId).getData();

		// 获取拒绝处理配置
		Refuse refuseConfig = node.getRefuse();

		if (refuseConfig != null) {
			String rejectionHandler = refuseConfig.getHandler();

			// 为当前流程实例创建状态变更构建器
			var stateChangeBuilder = runtimeService.createChangeActivityStateBuilder()
				.processInstanceId(processInstanceId);

			if (StrUtil.equals(rejectionHandler, "TO_NODE")) {
				// 根据拒绝设置重定向到特定节点
				stateChangeBuilder.moveActivityIdTo(currentActivityId, refuseConfig.getNodeId()).changeState();
			}
			else {
				// 默认行为：重定向到结束节点
				stateChangeBuilder.moveActivityIdTo(currentActivityId, "end").changeState();
			}
		}
		// 如果未设置refuseConfig，工作流将沿其正常路径继续
	}

}