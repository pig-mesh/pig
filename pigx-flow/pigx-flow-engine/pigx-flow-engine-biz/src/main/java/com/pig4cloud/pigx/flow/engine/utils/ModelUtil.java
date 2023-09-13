package com.pig4cloud.pigx.flow.engine.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.flow.engine.expression.condition.NodeExpressionStrategyFactory;
import com.pig4cloud.pigx.flow.engine.listeners.ApprovalCreateListener;
import com.pig4cloud.pigx.flow.engine.listeners.FlowProcessEventListener;
import com.pig4cloud.pigx.flow.engine.service.ApproveServiceTask;
import com.pig4cloud.pigx.flow.engine.service.CopyServiceTask;
import com.pig4cloud.pigx.flow.task.api.feign.RemoteFlowTaskService;
import com.pig4cloud.pigx.flow.task.constant.NodeTypeEnum;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.ProcessNodeDataDto;
import com.pig4cloud.pigx.flow.task.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型工具类 处理模型构建相关的
 */
@Slf4j
public class ModelUtil {

	/**
	 * 构建模型
	 * @param nodeDto 前端传输节点
	 * @return
	 */
	public static BpmnModel buildBpmnModel(Node nodeDto, String processName, String flowId) {
		BpmnModel bpmnModel = new BpmnModel();
		bpmnModel.setTargetNamespace("pig");

		Process process = new Process();
		process.setId(flowId);
		process.setName(processName);

		// 流程监听器
		ArrayList<EventListener> eventListeners = new ArrayList<>();

		{
			// 流程实例监听器
			EventListener eventListener = new EventListener();

			eventListener.setImplementationType("class");
			eventListener.setImplementation(FlowProcessEventListener.class.getCanonicalName());

			eventListeners.add(eventListener);

		}
		process.setEventListeners(eventListeners);

		NodeUtil.addEndNode(nodeDto);

		// 创建所有的节点
		buildAllNode(process, nodeDto, flowId);
		// 创建所有的内部节点连接线
		buildAllNodeInnerSequence(process, nodeDto, flowId);
		// 创建节点间连线
		buildAllNodeOuterSequence(process, nodeDto, null);
		// 处理分支和下级连线

		bpmnModel.addProcess(process);
		return bpmnModel;
	}

	/**
	 * 先创建所有的节点
	 * @param process
	 * @param nodeDto
	 * @param flowId
	 */
	public static void buildAllNode(Process process, Node nodeDto, String flowId) {
		if (!NodeUtil.isNode(nodeDto)) {
			return;
		}

		List<FlowElement> flowElementList = buildNode(nodeDto, flowId);
		for (FlowElement flowElement : flowElementList) {
			if (process.getFlowElement(flowElement.getId()) == null) {
				process.addFlowElement(flowElement);
			}
		}

		// 子节点
		Node children = nodeDto.getChildren();

		if (NodeTypeEnum.getByValue(nodeDto.getType()).getBranch()) {

			// 条件分支
			List<Node> branchs = nodeDto.getConditionNodes();
			for (Node branch : branchs) {
				buildAllNode(process, branch.getChildren(), flowId);

			}
			if (NodeUtil.isNode(children)) {
				buildAllNode(process, children, flowId);
			}

		}
		else {

			if (NodeUtil.isNode(children)) {
				buildAllNode(process, children, flowId);
			}
		}

	}

	/**
	 * 先创建所有的内部节点连接线
	 * @param process
	 * @param nodeDto
	 * @param flowId
	 */
	public static void buildAllNodeInnerSequence(Process process, Node nodeDto, String flowId) {
		if (!NodeUtil.isNode(nodeDto)) {
			return;
		}

		// 画内部线
		List<SequenceFlow> flowList = buildInnerSequenceFlow(nodeDto, flowId);
		for (SequenceFlow sequenceFlow : flowList) {
			process.addFlowElement(sequenceFlow);
		}

		// 子节点
		Node children = nodeDto.getChildren();
		if (NodeTypeEnum.getByValue(nodeDto.getType()).getBranch()) {
			// 条件分支
			List<Node> branchs = nodeDto.getConditionNodes();
			for (Node branch : branchs) {
				buildAllNodeInnerSequence(process, branch.getChildren(), flowId);

			}
			if (NodeUtil.isNode(children)) {
				buildAllNodeInnerSequence(process, children, flowId);
			}

		}
		else {

			if (NodeUtil.isNode(children)) {
				buildAllNodeInnerSequence(process, children, flowId);
			}
		}

	}

	/**
	 * 递归创建节点间连线
	 * @param process 流程
	 * @param nodeDto 节点对象
	 * @param nextId
	 */
	public static void buildAllNodeOuterSequence(Process process, Node nodeDto, String nextId) {

		if (!NodeUtil.isNode(nodeDto)) {
			return;
		}

		// 子节点
		Node children = nodeDto.getChildren();
		if (NodeTypeEnum.getByValue(nodeDto.getType()).getBranch()) {
			// children = children.getChildren();
			// 条件分支
			List<Node> branchs = nodeDto.getConditionNodes();
			int ord = 1;
			int size = branchs.size();
			for (Node branch : branchs) {

				buildAllNodeOuterSequence(process, branch.getChildren(), nodeDto.getTailId());

				String expression = null;

				if (nodeDto.getType() == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()) {
					if (ord == size) {
						expression = NodeExpressionStrategyFactory.handleDefaultBranch(branchs, ord - 1);
					}
					else if (nodeDto.getType() == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue() && ord > 1) {
						expression = NodeExpressionStrategyFactory.handleDefaultBranch(branchs, ord - 1);
					}
					else {
						expression = NodeExpressionStrategyFactory.handle(branch);
					}

				}

				// 添加连线
				if (!NodeUtil.isNode(branch.getChildren())) {
					// 当前分支 没有其他节点了 所有就是网关和网关后面节点直接连线

					SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeDto.getId(), nodeDto.getTailId(),
							expression, StrUtil.format("{}->{}", nodeDto.getName(), nodeDto.getName()));
					process.addFlowElement(sequenceFlow);
				}
				else {

					SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeDto.getId(),
							branch.getChildren().getHeadId(), expression,
							StrUtil.format("{}->{}", nodeDto.getName(), branch.getChildren().getName()));
					process.addFlowElement(sequenceFlow);
				}
				ord++;

			}
			// 分支结尾的合并分支节点-》下一个节点
			if (children != null && StrUtil.isNotBlank(children.getHeadId())
					&& StrUtil.isNotBlank(nodeDto.getTailId())) {

				SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeDto.getTailId(), children.getHeadId(), "",
						StrUtil.format("{}->{}", nodeDto.getName(), children.getName()));
				process.addFlowElement(sequenceFlow);

			}
			else if (StrUtil.isAllNotBlank(nodeDto.getTailId(), nextId)) {
				SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeDto.getTailId(), nextId, "",
						StrUtil.format("{}->{}", nodeDto.getName(), nextId));
				process.addFlowElement(sequenceFlow);
			}

			buildAllNodeOuterSequence(process, children, nextId);

		}
		else {
			// 添加连线
			if (NodeUtil.isNode(children)) {
				List<SequenceFlow> sequenceFlowList = buildSequenceFlow(children, nodeDto, "");
				for (SequenceFlow sequenceFlow : sequenceFlowList) {
					process.addFlowElement(sequenceFlow);
				}
				buildAllNodeOuterSequence(process, children, nextId);
			}
			else if (nodeDto.getType() != NodeTypeEnum.END.getValue().intValue()) {
				SequenceFlow seq = buildSingleSequenceFlow(nodeDto.getTailId(), nextId, "",
						StrUtil.format("{}->{}", nodeDto.getName(), nextId));

				process.addFlowElement(seq);

			}
		}

	}

	/**
	 * 构建节点
	 * @param node 前端传输节点
	 * @param flowId
	 * @return
	 */
	private static List<FlowElement> buildNode(Node node, String flowId) {
		List<FlowElement> flowElementList = new ArrayList<>();
		if (!NodeUtil.isNode(node)) {
			return flowElementList;
		}

		// 设置节点的连线头节点
		node.setHeadId(node.getId());
		// 设置节点的连线尾节点
		node.setTailId(node.getId());
		node.setName(StrUtil.format("{}[{}]", node.getName(), RandomUtil.randomNumbers(5)));

		// 存储节点数据
		RemoteFlowTaskService remoteFlowTaskService = SpringUtil.getBean(RemoteFlowTaskService.class);
		ProcessNodeDataDto processNodeDataDto = new ProcessNodeDataDto();
		processNodeDataDto.setFlowId(flowId);
		processNodeDataDto.setNodeId(node.getId());
		processNodeDataDto.setData(JSONUtil.toJsonStr(node));
		remoteFlowTaskService.saveNodeOriData(processNodeDataDto);

		// 开始
		if (node.getType() == NodeTypeEnum.ROOT.getValue().intValue()) {
			flowElementList.addAll(buildStartNode(node));
		}

		// 结束
		if (node.getType() == NodeTypeEnum.END.getValue().intValue()) {
			flowElementList.add(buildEndNode(node, false));
		}

		// 审批
		if (node.getType() == NodeTypeEnum.APPROVAL.getValue().intValue()) {

			flowElementList.addAll(buildApproveNode(node));
		}

		// 抄送
		if (node.getType() == NodeTypeEnum.CC.getValue().intValue()) {
			flowElementList.add(buildCCNode(node));
		}
		// 条件分支
		if (node.getType() == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()) {
			flowElementList.addAll(buildInclusiveGatewayNode(node));
		}
		// 并行分支
		if (node.getType() == NodeTypeEnum.PARALLEL_GATEWAY.getValue().intValue()) {
			flowElementList.addAll(buildParallelGatewayNode(node));
		}

		return flowElementList;
	}

	/**
	 * 构建开始节点 添加一个自动完成任务的用户任务节点
	 * @param node 前端传输节点
	 * @return
	 */
	private static List<FlowElement> buildStartNode(Node node) {

		List<FlowElement> flowElementList = new ArrayList<>();

		StartEvent startEvent = new StartEvent();
		startEvent.setId(node.getId());
		startEvent.setName(node.getName());

		flowElementList.add(startEvent);

		return flowElementList;
	}

	/**
	 * 构建审批节点
	 * @param node
	 * @return
	 */
	private static List<FlowElement> buildApproveNode(Node node) {
		List<FlowElement> flowElementList = new ArrayList<>();
		node.setTailId(StrUtil.format("approve_service_task_{}", node.getId()));

		// 创建了任务执行监听器
		// 先执行指派人 后创建
		// https://tkjohn.github.io/flowable-userguide/#eventDispatcher
		FlowableListener createListener = new FlowableListener();
		createListener.setImplementation(ApprovalCreateListener.class.getCanonicalName());
		createListener.setImplementationType("class");
		createListener.setEvent("create");

		UserTask userTask = buildUserTask(node, createListener);
		flowElementList.add(userTask);

		ServiceTask serviceTask = new ServiceTask();
		serviceTask.setId(StrUtil.format("approve_service_task_{}", node.getId()));
		serviceTask.setName(StrUtil.format("{}_服务任务", node.getName()));
		serviceTask.setImplementationType("class");
		serviceTask.setImplementation(ApproveServiceTask.class.getCanonicalName());
		serviceTask.setAsynchronous(false);

		flowElementList.add(serviceTask);

		{

			// 执行人处理

			String inputDataItem = "${multiInstanceHandler.resolveAssignee(execution)}";

			// 串行

			boolean isSequential = true;

			Integer multipleMode = node.getMultipleMode();
			// 多人
			if ((multipleMode == ProcessInstanceConstant.MULTIPLE_MODE_AL_SAME)) {
				// 并行会签
				isSequential = false;
			}
			if ((multipleMode == ProcessInstanceConstant.MULTIPLE_MODE_ALL_SORT)) {

				// 串行会签
			}
			if ((multipleMode == ProcessInstanceConstant.MULTIPLE_MODE_ONE)) {

				// 或签
				isSequential = false;
			}

			MultiInstanceLoopCharacteristics loopCharacteristics = new MultiInstanceLoopCharacteristics();
			loopCharacteristics.setSequential(isSequential);
			loopCharacteristics.setInputDataItem(inputDataItem);
			loopCharacteristics.setElementVariable(StrUtil.format("{}_assignee_temp", node.getId()));

			loopCharacteristics.setCompletionCondition("${multiInstanceHandler.completionCondition(execution)}");

			userTask.setLoopCharacteristics(loopCharacteristics);
			String format = StrUtil.format("${{}_assignee_temp}", node.getId());
			userTask.setAssignee(format);

		}
		return flowElementList;
	}

	/**
	 * 创建用户任务
	 * @param node 前端传输节点
	 * @return
	 */
	private static UserTask buildUserTask(Node node, FlowableListener... flowableListeners) {
		UserTask userTask = new UserTask();
		userTask.setId(node.getId());
		userTask.setName(node.getName());

		if (flowableListeners != null) {
			List<FlowableListener> taskListeners = new ArrayList<>();

			for (FlowableListener flowableListener : flowableListeners) {
				taskListeners.add(flowableListener);

			}
			userTask.setTaskListeners(taskListeners);
		}

		return userTask;
	}

	/**
	 * 构建简单的包容网关
	 * @param node
	 * @return
	 */
	private static FlowElement buildSimpleExclusiveGatewayNode(Node node) {

		ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
		exclusiveGateway.setId(node.getId());
		exclusiveGateway.setName(node.getName());

		return exclusiveGateway;

	}

	/**
	 * 构建并行网关
	 * @param node
	 * @return
	 */
	private static List<FlowElement> buildParallelGatewayNode(Node node) {
		node.setTailId(StrUtil.format("{}_merge_gateway", node.getId()));
		List<FlowElement> flowElementList = new ArrayList<>();

		ParallelGateway inclusiveGateway = new ParallelGateway();
		inclusiveGateway.setId(node.getId());
		inclusiveGateway.setName(node.getName());
		flowElementList.add(inclusiveGateway);

		// 合并网关
		ParallelGateway parallelGateway = new ParallelGateway();
		parallelGateway.setId(StrUtil.format("{}_merge_gateway", node.getId()));
		parallelGateway.setName(StrUtil.format("{}_合并网关", node.getName()));
		flowElementList.add(parallelGateway);

		return flowElementList;
	}

	/**
	 * 构建包容网关
	 * @param node
	 * @return
	 */
	private static List<FlowElement> buildInclusiveGatewayNode(Node node) {

		node.setTailId(StrUtil.format("{}_merge_gateway", node.getId()));

		List<FlowElement> flowElementList = new ArrayList<>();

		InclusiveGateway inclusiveGateway = new InclusiveGateway();
		inclusiveGateway.setId(node.getId());
		inclusiveGateway.setName(node.getName());
		flowElementList.add(inclusiveGateway);

		// 合并网关
		InclusiveGateway gateway = new InclusiveGateway();
		gateway.setId(StrUtil.format("{}_merge_gateway", node.getId()));
		gateway.setName(StrUtil.format("{}_合并网关", node.getName()));
		flowElementList.add(gateway);

		return flowElementList;
	}

	/**
	 * 构建结束节点
	 * @param node 前端传输节点
	 * @param terminateAll
	 * @return
	 */
	private static EndEvent buildEndNode(Node node, boolean terminateAll) {
		EndEvent endEvent = new EndEvent();
		endEvent.setId(node.getId());
		endEvent.setName(node.getName());

		List<EventDefinition> definitionList = new ArrayList<>();
		TerminateEventDefinition definition = new TerminateEventDefinition();
		definition.setTerminateAll(terminateAll);
		definitionList.add(definition);
		endEvent.setEventDefinitions(definitionList);

		return endEvent;
	}

	/**
	 * 创建连接线
	 * @param node 子级节点
	 * @param parentNode 父级节点
	 * @param expression
	 * @return 所有连接线
	 */
	private static List<SequenceFlow> buildSequenceFlow(Node node, Node parentNode, String expression) {
		List<SequenceFlow> sequenceFlowList = new ArrayList<>();
		// 没有子级了
		if (!NodeUtil.isNode(node)) {
			return sequenceFlowList;
		}

		String pid = parentNode.getId();

		if (StrUtil.hasBlank(pid, node.getId())) {
			return sequenceFlowList;
		}

		SequenceFlow sequenceFlow = buildSingleSequenceFlow(parentNode.getTailId(), node.getHeadId(), expression,
				StrUtil.format("{}->{}", parentNode.getName(), node.getName()));
		sequenceFlowList.add(sequenceFlow);

		return sequenceFlowList;
	}

	/**
	 * 生成扩展数据
	 * @param key
	 * @param val
	 * @return
	 */
	public static ExtensionAttribute generateExtensionAttribute(String key, String val) {
		ExtensionAttribute ea = new ExtensionAttribute();

		ea.setName(key);
		ea.setValue(val);
		return ea;
	}

	/**
	 * 创建抄送节点
	 * @param node
	 * @return
	 */
	private static FlowElement buildCCNode(Node node) {

		ServiceTask serviceTask = new ServiceTask();
		serviceTask.setId(node.getId());
		serviceTask.setName(node.getName());
		serviceTask.setAsynchronous(false);
		serviceTask.setImplementationType("class");
		serviceTask.setImplementation(CopyServiceTask.class.getCanonicalName());

		ExtensionElement e = new ExtensionElement();
		{

			e.setName("flowable:failedJobRetryTimeCycle");
			// 上面的例子会让作业执行器重试5次，并在每次重试前等待1分钟。
			e.setElementText("R5/PT1M");

		}

		serviceTask.addExtensionElement(e);
		return serviceTask;
	}

	/**
	 * 创建连接线
	 * @param node 父级节点
	 * @param flowId
	 * @return 所有连接线
	 */
	private static List<SequenceFlow> buildInnerSequenceFlow(Node node, String flowId) {

		List<SequenceFlow> sequenceFlowList = new ArrayList<>();
		if (!NodeUtil.isNode(node)) {
			return sequenceFlowList;
		}

		String nodeId = node.getId();
		if (StrUtil.hasBlank(nodeId)) {
			return sequenceFlowList;
		}

		if (node.getType() == NodeTypeEnum.APPROVAL.getValue().intValue()) {

			String gatewayId = StrUtil.format("approve_service_task_{}", nodeId);

			{
				SequenceFlow sequenceFlow = buildSingleSequenceFlow(nodeId, gatewayId, "${12==12}", null);
				sequenceFlowList.add(sequenceFlow);
			}

		}

		return sequenceFlowList;
	}

	/**
	 * 创建单个连接线
	 * @param pId 父级id
	 * @param childId 子级id
	 * @param expression 表达式
	 * @param name
	 * @return
	 */
	private static SequenceFlow buildSingleSequenceFlow(String pId, String childId, String expression, String name) {
		if (StrUtil.hasBlank(pId, childId)) {
			return null;
		}
		SequenceFlow sequenceFlow = new SequenceFlow(pId, childId);
		sequenceFlow.setConditionExpression(expression);
		sequenceFlow.setName(StrUtil.format("{}|{}", pId, childId));
		sequenceFlow.setName(StrUtil.format("连线[{}]", RandomUtil.randomString(5)));
		if (StrUtil.isNotBlank(name)) {
			sequenceFlow.setName(name);
		}
		sequenceFlow.setId(StrUtil.format("sq-id-{}-{}", IdUtil.fastSimpleUUID(), RandomUtil.randomInt(1, 10000000)));
		return sequenceFlow;
	}

}
