package com.pig4cloud.pigx.flow.support.utils;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.flow.constant.NodeTypeEnum;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.ProcessNodeDataDto;
import com.pig4cloud.pigx.flow.dto.TimerConfig;
import com.pig4cloud.pigx.flow.dto.TimerDuration;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import com.pig4cloud.pigx.flow.support.expression.condition.NodeExpressionStrategyFactory;
import com.pig4cloud.pigx.flow.support.listeners.ApprovalCreateListener;
import com.pig4cloud.pigx.flow.support.listeners.FlowProcessEventListener;
import com.pig4cloud.pigx.flow.support.listeners.RootTaskCreateListener;
import com.pig4cloud.pigx.flow.support.tasks.ApproveServiceTask;
import com.pig4cloud.pigx.flow.support.tasks.CopyServiceTask;
import com.pig4cloud.pigx.flow.support.tasks.TriggerServiceTask;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * BPMN模型构建工具类
 * <p>
 * 负责将前端设计的流程节点结构转换为Flowable引擎可执行的BPMN模型。
 * 该工具类是流程定义创建的核心，支持各种节点类型和流程结构的转换。
 * <p>
 * 主要功能：
 * 1. 将前端节点树结构转换为BPMN模型
 * 2. 创建各种类型的流程节点（开始、结束、审批、抄送、网关等）
 * 3. 创建节点间的连接线（顺序流）
 * 4. 配置节点的执行策略和监听器
 * 5. 处理多实例任务（会签、或签）
 * <p>
 * 支持的节点类型：
 * - 开始节点、结束节点
 * - 审批节点（支持单人、会签、或签）
 * - 抄送节点
 * - 排他网关（条件分支）
 * - 并行网关
 *
 * @author pigx
 */
@Slf4j
public class ModelUtil {

	/**
	 * 构建BPMN模型
	 * <p>
	 * 将前端传输的节点树结构转换为Flowable引擎可执行的BPMN模型。
	 * 该方法是整个转换过程的入口，负责协调各个步骤。
	 *
	 * @param nodeDto     前端传输的根节点，包含整个流程的节点树结构
	 * @param processName 流程名称
	 * @param flowId      流程定义ID
	 * @return 构建完成的BPMN模型对象
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
	 * 递归创建所有节点
	 * <p>
	 * 遍历节点树结构，创建所有的流程节点元素。
	 * 该方法只负责创建节点，不处理节点间的连接关系。
	 *
	 * @param process 流程对象，用于添加流程元素
	 * @param nodeDto 当前处理的节点
	 * @param flowId  流程定义ID
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
	 * 创建所有节点的内部连接线
	 * <p>
	 * 处理节点内部的连接线，例如审批节点与其后续服务任务的连接。
	 * 这些连接线是节点内部的逻辑关系，不属于节点间的连接。
	 *
	 * @param process 流程对象
	 * @param nodeDto 当前处理的节点
	 * @param flowId  流程定义ID
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
	 * 递归创建节点间连接线
	 * <p>
	 * 处理节点与节点之间的连接关系，包括：
	 * 1. 普通节点间的顺序连接
	 * 2. 网关节点的分支连接
	 * 3. 分支合并的连接
	 *
	 * @param process 流程对象
	 * @param nodeDto 当前处理的节点
	 * @param nextId  下一个节点的ID，用于处理最后一个节点的连接
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
					else if (ord > 1) {
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
	 * <p>
	 * 根据节点类型创建对应的BPMN元素。
	 * 同时会将节点数据存储到数据库中，供运行时查询使用。
	 *
	 * @param node   前端传输的节点数据
	 * @param flowId 流程定义ID
	 * @return 创建的流程元素列表
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
		IProcessNodeDataService processNodeDataService = SpringUtil.getBean(IProcessNodeDataService.class);
		ProcessNodeDataDto processNodeDataDto = new ProcessNodeDataDto();
		processNodeDataDto.setFlowId(flowId);
		processNodeDataDto.setNodeId(node.getId());
		processNodeDataDto.setData(JSONUtil.toJsonStr(node));
		processNodeDataService.saveNodeData(processNodeDataDto);

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
        // 延时器
        if (node.getType() == NodeTypeEnum.TIMER.getValue().intValue()) {
            flowElementList.add(buildTimerNode(node));
        }

		return flowElementList;
	}

	/**
	 * 构建开始节点
	 * <p>
	 * 创建BPMN流程的开始事件节点和发起人任务节点。
	 * 流程结构：StartEvent(start) → UserTask(root) → 第一个审批节点
	 * </p>
	 * <p>
	 * 发起人任务节点的作用：
	 * <ul>
	 *   <li>正常发起流程时，通过RootTaskCreateListener自动完成，流程继续到下一个节点</li>
	 *   <li>驳回到发起人时，任务保留给发起人，发起人可以在待办任务中编辑并重新提交</li>
	 * </ul>
	 * </p>
	 *
	 * @param node 前端传输的开始节点数据
	 * @return 包含开始事件和发起人任务的流程元素列表
	 */
	private static List<FlowElement> buildStartNode(Node node) {

		List<FlowElement> flowElementList = new ArrayList<>();

		// 1. 创建真正的开始事件（ID 为 start）
		StartEvent startEvent = new StartEvent();
		startEvent.setId("start");
		startEvent.setName("开始");
		flowElementList.add(startEvent);

		// 2. 创建发起人任务节点（ID 保持为 root）
		// 配置任务监听器，用于处理驳回场景
		FlowableListener createListener = new FlowableListener();
		createListener.setImplementation(RootTaskCreateListener.class.getCanonicalName());
		createListener.setImplementationType("class");
		createListener.setEvent("create");

		UserTask rootTask = new UserTask();
		rootTask.setId(node.getId()); // 保持 "root" 作为 ID
		rootTask.setName(node.getName());
		// 动态获取发起人作为执行人
		rootTask.setAssignee("${rootTaskHandler.resolveAssignee(execution)}");

		List<FlowableListener> taskListeners = new ArrayList<>();
		taskListeners.add(createListener);
		rootTask.setTaskListeners(taskListeners);

		flowElementList.add(rootTask);

		// 3. 更新节点的 head/tail 信息
		node.setHeadId("start"); // 头部是 StartEvent
		node.setTailId(node.getId()); // 尾部是 root UserTask

		return flowElementList;
	}

	/**
	 * 构建审批节点
	 * <p>
	 * 创建审批节点，包括用户任务和后续的服务任务。
	 * 支持多种审批模式：
	 * 1. 单人审批
	 * 2. 会签（并行/串行）
	 * 3. 或签
	 *
	 * @param node 审批节点数据
	 * @return 包含用户任务和服务任务的流程元素列表
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
	 * <p>
	 * 创建基本的用户任务节点，并可以添加任务监听器。
	 *
	 * @param node               节点数据
	 * @param flowableListeners  可选的任务监听器数组
	 * @return 用户任务对象
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
        // 触发器
        if (node.getType() == NodeTypeEnum.TRIGGER.getValue().intValue()) {
            flowElementList.add(buildTriggerNode(node));
        }

		return userTask;
	}

	/**
	 * 构建简单的排他网关
	 * <p>
	 * 创建排他网关节点，用于条件分支。
	 *
	 * @param node 网关节点数据
	 * @return 排他网关元素
	 */
	private static FlowElement buildSimpleExclusiveGatewayNode(Node node) {

		ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
		exclusiveGateway.setId(node.getId());
		exclusiveGateway.setName(node.getName());

		return exclusiveGateway;

	}

	/**
	 * 构建并行网关
	 * <p>
	 * 创建并行网关节点和对应的合并网关。
	 * 并行网关用于创建多个并行执行的分支。
	 *
	 * @param node 并行网关节点数据
	 * @return 包含分叉网关和合并网关的元素列表
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
	 * <p>
	 * 创建包容网关（条件分支）节点和对应的合并网关。
	 * 包容网关用于根据条件创建不同的执行路径。
	 *
	 * @param node 包容网关节点数据
	 * @return 包含分叉网关和合并网关的元素列表
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
	 * <p>
	 * 创建BPMN流程的结束事件节点。
	 * 使用终止事件定义，确保流程实例正确结束。
	 *
	 * @param node         结束节点数据
	 * @param terminateAll 是否终止所有活动实例
	 * @return 结束事件对象
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
	 * 创建节点间连接线
	 * <p>
	 * 创建父节点到子节点的连接线。
	 * 使用节点的tailId和headId确定连接点。
	 *
	 * @param node       子级节点
	 * @param parentNode 父级节点
	 * @param expression 连接线条件表达式
	 * @return 连接线列表
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
	 * 生成扩展属性
	 * <p>
	 * 创建自定义的扩展属性，用于在BPMN元素中存储额外信息。
	 *
	 * @param key 属性键
	 * @param val 属性值
	 * @return 扩展属性对象
	 */
	public static ExtensionAttribute generateExtensionAttribute(String key, String val) {
		ExtensionAttribute ea = new ExtensionAttribute();

		ea.setName(key);
		ea.setValue(val);
		return ea;
	}

	/**
     * 创建简单服务任务节点（通用方法）
	 * <p>
     * 创建一个通用的服务任务节点，配置了失败重试机制（重试5次，每次间隔1分钟）。
	 *
     * @param node 节点数据
     * @param delegateClass JavaDelegate 实现类
	 * @return 服务任务元素
	 */
    private static FlowElement buildSimpleServiceTaskNode(Node node, Class<? extends JavaDelegate> delegateClass) {
		ServiceTask serviceTask = new ServiceTask();
		serviceTask.setId(node.getId());
		serviceTask.setName(node.getName());
		serviceTask.setAsynchronous(false);
		serviceTask.setImplementationType("class");
        serviceTask.setImplementation(delegateClass.getCanonicalName());

		ExtensionElement e = new ExtensionElement();
        e.setName("flowable:failedJobRetryTimeCycle");
        e.setElementText("R5/PT1M");

		serviceTask.addExtensionElement(e);
		return serviceTask;
	}

    /**
     * 构建延时器节点
     * <p>
     * 创建Flowable中间捕获事件（Timer Intermediate Catch Event），
     * 用于在流程中暂停执行，等待指定时间后自动继续。
     * <p>
     * 支持三种延时模式：
     * <ul>
     *   <li>DURATION - 固定时长（ISO 8601格式，如PT30M、PT2H、P1D、P1W）</li>
     *   <li>DATETIME - 指定日期时间（ISO 8601格式）</li>
     *   <li>FORM_FIELD - 表单字段日期（使用流程变量表达式）</li>
     * </ul>
     *
     * @param node 延时器节点数据
     * @return 中间捕获事件元素
     */
    private static FlowElement buildTimerNode(Node node) {
        IntermediateCatchEvent catchEvent = new IntermediateCatchEvent();
        catchEvent.setId(node.getId());
        catchEvent.setName(node.getName());

        TimerEventDefinition timerDef = new TimerEventDefinition();

        TimerConfig config = node.getTimerConfig();
        if (config != null && StrUtil.isNotBlank(config.getTimerType())) {
            String timerType = config.getTimerType();

            if ("DURATION".equals(timerType)) {
                TimerDuration duration = config.getDuration();
                if (duration != null && duration.getValue() != null) {
                    int value = duration.getValue();
                    String unit = duration.getUnit() != null ? duration.getUnit() : "MINUTE";
                    String iso = switch (unit) {
                        case "HOUR" -> StrUtil.format("PT{}H", value);
                        case "DAY" -> StrUtil.format("P{}D", value);
                        case "WEEK" -> StrUtil.format("P{}W", value);
                        default -> StrUtil.format("PT{}M", value);
                    };
                    timerDef.setTimeDuration(iso);
                }
            } else if ("DATETIME".equals(timerType)) {
                String dateTime = config.getDateTime();
                if (StrUtil.isNotBlank(dateTime)) {
                    timerDef.setTimeDate(dateTime);
                }
            } else if ("FORM_FIELD".equals(timerType)) {
                String formFieldId = config.getFormFieldId();
                if (StrUtil.isNotBlank(formFieldId)) {
                    timerDef.setTimeDate("${timerHandler.resolveDate(execution, '" + formFieldId + "')}");
                }
            } else {
                log.warn("未知的延时器类型: {}，使用默认30分钟", timerType);
                timerDef.setTimeDuration("PT30M");
            }
        } else {
            timerDef.setTimeDuration("PT30M");
        }

        catchEvent.setEventDefinitions(List.of(timerDef));

        return catchEvent;
    }

    private static FlowElement buildCCNode(Node node) {
        return buildSimpleServiceTaskNode(node, CopyServiceTask.class);
    }

    private static FlowElement buildTriggerNode(Node node) {
        return buildSimpleServiceTaskNode(node, TriggerServiceTask.class);
    }

	/**
	 * 创建节点内部连接线
	 * <p>
	 * 创建节点内部的连接线，例如：
	 * <ul>
	 *   <li>开始节点：start → root（StartEvent到发起人UserTask）</li>
	 *   <li>审批节点：审批任务 → 服务任务</li>
	 * </ul>
	 *
	 * @param node   节点数据
	 * @param flowId 流程定义ID
	 * @return 内部连接线列表
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

		// 开始节点：创建 start → root 的连线
		if (node.getType() == NodeTypeEnum.ROOT.getValue().intValue()) {
			SequenceFlow sequenceFlow = buildSingleSequenceFlow("start", nodeId, null, "start->root");
			sequenceFlowList.add(sequenceFlow);
		}

		// 审批节点：创建审批任务到服务任务的连线
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
	 * <p>
	 * 创建两个节点之间的顺序流连接线。
	 * 自动生成唯一的连接线ID和名称。
	 *
	 * @param pId        父级节点ID（起始节点）
	 * @param childId    子级节点ID（目标节点）
	 * @param expression 条件表达式，可以为空
	 * @param name       连接线名称，可以为null
	 * @return 顺序流对象，如果参数不合法则返回null
	 */
	private static SequenceFlow buildSingleSequenceFlow(String pId, String childId, String expression, String name) {
		if (StrUtil.hasBlank(pId, childId)) {
			return null;
		}
		SequenceFlow sequenceFlow = new SequenceFlow(pId, childId);
		sequenceFlow.setConditionExpression(expression);
        sequenceFlow.setName(StrUtil.isNotBlank(name) ? name : StrUtil.format("连线[{}]", RandomUtil.randomString(5)));
		sequenceFlow.setId(StrUtil.format("sq-id-{}-{}", IdUtil.fastSimpleUUID(), RandomUtil.randomInt(1, 10000000)));
		return sequenceFlow;
	}

}
