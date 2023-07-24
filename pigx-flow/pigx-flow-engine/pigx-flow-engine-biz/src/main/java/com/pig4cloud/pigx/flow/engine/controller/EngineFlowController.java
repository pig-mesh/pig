package com.pig4cloud.pigx.flow.engine.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.engine.utils.ModelUtil;
import com.pig4cloud.pigx.flow.task.dto.*;
import com.pig4cloud.pigx.flow.task.entity.Process;
import com.pig4cloud.pigx.flow.task.utils.NodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.impl.DefaultProcessDiagramGenerator;
import org.flowable.task.api.TaskQuery;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工作流控制器 负责流程模型的创建、启动、审批等功能
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/flow")
public class EngineFlowController {

	private final TaskService taskService;

	private final HistoryService historyService;

	private final RepositoryService repositoryService;

	private final RuntimeService runtimeService;

	private final ObjectMapper objectMapper;

	/**
	 * 创建流程定义
	 * @param map 创建参数
	 * @return 流程定义ID
	 */
	@PostMapping("create")
	@SneakyThrows
	public R create(@RequestBody Map<String, Object> map) {

		Long userId = MapUtil.getLong(map, "userId");

		Process process = MapUtil.get(map, "process", Process.class);

		String flowId = "P" + userId + DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN)
				+ RandomUtil.randomString(5);
		log.info("flowId={}", flowId);
		BpmnModel bpmnModel = ModelUtil.buildBpmnModel(objectMapper.readValue(process.getProcess(), Node.class),
				process.getName(), flowId);
		{
			byte[] bpmnBytess = new BpmnXMLConverter().convertToXML(bpmnModel);
			String filename = "/tmp/flowable-deployment/" + flowId + ".bpmn20.xml";
			log.debug("部署时的模型文件：{}", filename);
			FileUtil.writeBytes(bpmnBytess, filename);
		}
		repositoryService.createDeployment().addBpmnModel(StrUtil.format("{}.bpmn20.xml", "test1"), bpmnModel).deploy();

		return R.ok(flowId);
	}

	/**
	 * 启动流程实例
	 * @param processInstanceParamDto 启动参数
	 * @return 流程实例ID
	 */
	@PostMapping("/start")
	public R start(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {
		Authentication.setAuthenticatedUserId(processInstanceParamDto.getStartUserId());
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processInstanceParamDto.getFlowId(),
				processInstanceParamDto.getParamMap());

		String processInstanceId = processInstance.getProcessInstanceId();
		return R.ok(processInstanceId);

	}

	@GetMapping("/showImg")
	public R showImg(String procInsId) {

		String procDefId;
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
			.processInstanceId(procInsId)
			.singleResult();
		if (processInstance == null) {
			HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(procInsId)
				.singleResult();
			procDefId = historicProcessInstance.getProcessDefinitionId();

		}
		else {
			procDefId = processInstance.getProcessDefinitionId();
		}

		// 使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		BpmnModel bpmnModel = repositoryService.getBpmnModel(procDefId);

		// 创建默认的流程图生成器
		DefaultProcessDiagramGenerator defaultProcessDiagramGenerator = new DefaultProcessDiagramGenerator();
		// 生成图片的类型
		String imageType = "png";
		// 高亮节点集合
		List<String> highLightedActivities = new ArrayList<>();
		// 高亮连线集合
		List<String> highLightedFlows = new ArrayList<>();
		// 查询所有历史节点信息
		List<HistoricActivityInstance> hisActInsList = historyService.createHistoricActivityInstanceQuery()
			.processInstanceId(procInsId)
			.list();

		// 遍历
		hisActInsList.forEach(historicActivityInstance -> {
			if ("sequenceFlow".equals(historicActivityInstance.getActivityType())) {
				// 添加高亮连线
				highLightedFlows.add(historicActivityInstance.getActivityId());
			}
			else {
				// 添加高亮节点
				highLightedActivities.add(historicActivityInstance.getActivityId());
			}
		});
		// 节点字体
		String activityFontName = "宋体";
		// 连线标签字体
		String labelFontName = "微软雅黑";
		// 连线标签字体
		String annotationFontName = "宋体";
		// 类加载器
		ClassLoader customClassLoader = null;
		// 比例因子，默认即可
		double scaleFactor = 1.0d;
		// 不设置连线标签不会画
		boolean drawSequenceFlowNameWithNoLabelDI = true;

		BpmnAutoLayout bpmnAutoLayout = new BpmnAutoLayout(bpmnModel);
		bpmnAutoLayout.setTaskHeight(120);
		bpmnAutoLayout.setTaskWidth(120);
		bpmnAutoLayout.execute();
		// 生成图片
		InputStream inputStream = defaultProcessDiagramGenerator.generateDiagram(bpmnModel, imageType,
				highLightedActivities, highLightedFlows, activityFontName, labelFontName, annotationFontName, null,
				scaleFactor, drawSequenceFlowNameWithNoLabelDI); // 获取输入流

		String content = Base64.encode(inputStream);
		return R.ok(content);
	}

	/**
	 * 审批任务
	 * @param taskId 任务ID
	 * @param approved 是否通过
	 */
	@PostMapping("/approve")
	public void approve(String taskId, boolean approved) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("approved", approved);
		variables.put("ko", 10);
		variables.put("assigneeListSub", CollUtil.newArrayList("aa", "bb"));
		taskService.complete(taskId, variables);

	}

	/**
	 * 停止流程实例
	 * @param taskParamDto 参数
	 * @return 操作结果
	 */
	@PostMapping("stopProcessInstance")
	public R stopProcessInstance(@RequestBody TaskParamDto taskParamDto) {
		List<String> processInstanceIdList = taskParamDto.getProcessInstanceIdList();
		processInstanceIdList.forEach(processInstanceId -> {
			// 查询流程实例
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId)
				.singleResult();
			if (Optional.ofNullable(processInstance).isPresent()) {
				// 查询执行实例
				List<String> executionIds = runtimeService.createExecutionQuery()
					.parentId(processInstanceId)
					.list()
					.stream()
					.map(Execution::getId)
					.collect(Collectors.toList());
				// 更改活动状态为结束
				runtimeService.createChangeActivityStateBuilder()
					.moveExecutionsToSingleActivityId(executionIds, "end")
					.changeState();
			}
		});
		return R.ok();
	}

	/**
	 * 查询用户已办任务
	 * @param taskQueryParamDto 查询参数
	 * @return 分页任务信息
	 */
	@PostMapping("/queryCompletedTask")
	public R queryCompletedTask(@RequestBody TaskQueryParamDto taskQueryParamDto) {
		HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService
			.createHistoricActivityInstanceQuery();
		HistoricActivityInstanceQuery activityInstanceQuery = historicActivityInstanceQuery
			.taskAssignee(taskQueryParamDto.getAssign())
			.finished()
			.orderByHistoricActivityInstanceEndTime()
			.desc();

		if (ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime())) {
			ZoneId zoneId = ZoneId.systemDefault();
			ZonedDateTime zonedBeforeDateTime = taskQueryParamDto.getTaskTime()[0].atZone(zoneId);
			ZonedDateTime zonedAfterDateTime = taskQueryParamDto.getTaskTime()[1].atZone(zoneId);
			Date beforeDate = Date.from(zonedBeforeDateTime.toInstant());
			Date afterDate = Date.from(zonedAfterDateTime.toInstant());
			activityInstanceQuery.finishedBefore(afterDate).finishedAfter(beforeDate);
		}

		List<HistoricActivityInstance> list = activityInstanceQuery.listPage(
				(taskQueryParamDto.getPageNum() - 1) * taskQueryParamDto.getPageSize(),
				taskQueryParamDto.getPageSize());

		long count = activityInstanceQuery.count();
		List<TaskDto> taskDtoList = new ArrayList<>();

		for (HistoricActivityInstance historicActivityInstance : list) {
			String activityId = historicActivityInstance.getActivityId();
			String activityName = historicActivityInstance.getActivityName();
			String executionId = historicActivityInstance.getExecutionId();
			String taskId = historicActivityInstance.getTaskId();
			Date startTime = historicActivityInstance.getStartTime();
			Date endTime = historicActivityInstance.getEndTime();
			Long durationInMillis = historicActivityInstance.getDurationInMillis();
			String processInstanceId = historicActivityInstance.getProcessInstanceId();

			String processDefinitionId = historicActivityInstance.getProcessDefinitionId();
			// 流程id
			String flowId = NodeUtil.getFlowId(processDefinitionId);

			TaskDto taskDto = new TaskDto();
			taskDto.setFlowId(flowId);
			taskDto.setTaskCreateTime(startTime);
			taskDto.setTaskEndTime(endTime);
			taskDto.setNodeId(activityId);
			taskDto.setExecutionId(executionId);
			taskDto.setProcessInstanceId(processInstanceId);
			taskDto.setDurationInMillis(durationInMillis);
			taskDto.setTaskId(taskId);
			taskDto.setAssign(historicActivityInstance.getAssignee());
			taskDto.setTaskName(activityName);

			taskDtoList.add(taskDto);
		}

		Page<TaskDto> pageResultDto = new Page<>();
		pageResultDto.setTotal(count);
		pageResultDto.setRecords(taskDtoList);
		return R.ok(pageResultDto);
	}

	/**
	 * 查询用户待办任务
	 * @param taskQueryParamDto 查询参数
	 * @return 分页任务信息
	 */
	@PostMapping("/queryAssignTask")
	public R queryAssignTask(@RequestBody TaskQueryParamDto taskQueryParamDto) {

		String assign = taskQueryParamDto.getAssign();

		List<TaskDto> taskDtoList = new ArrayList<>();

		int pageIndex = taskQueryParamDto.getPageNum() - 1;
		int pageSize = taskQueryParamDto.getPageSize();

		Page<TaskDto> pageResultDto = new Page<>();
		TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(assign).orderByTaskCreateTime().desc();
		if (StrUtil.isNotBlank(taskQueryParamDto.getProcessName())) {
			List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
				.processInstanceNameLikeIgnoreCase(taskQueryParamDto.getProcessName())
				.list();
			if (CollUtil.isNotEmpty(processInstanceList)) {
				taskQuery.processInstanceIdIn(processInstanceList.stream()
					.map(ProcessInstance::getProcessDefinitionId)
					.collect(Collectors.toList()));
			}
		}

		if (ArrayUtil.isNotEmpty(taskQueryParamDto.getTaskTime())) {
			ZoneId zoneId = ZoneId.systemDefault();
			ZonedDateTime zonedBeforeDateTime = taskQueryParamDto.getTaskTime()[0].atZone(zoneId);
			ZonedDateTime zonedAfterDateTime = taskQueryParamDto.getTaskTime()[1].atZone(zoneId);
			Date beforeDate = Date.from(zonedBeforeDateTime.toInstant());
			Date afterDate = Date.from(zonedAfterDateTime.toInstant());
			taskQuery.taskCreatedBefore(afterDate).taskCreatedAfter(beforeDate);
		}

		taskQuery.listPage(pageIndex * pageSize, pageSize).forEach(task -> {
			String taskId = task.getId();
			String processInstanceId = task.getProcessInstanceId();
			log.debug("(taskId) " + task.getName() + " processInstanceId={} executrionId={}", processInstanceId,
					task.getExecutionId());

			Map<String, Object> taskServiceVariables = taskService.getVariables(taskId);
			Map<String, Object> runtimeServiceVariables = runtimeService.getVariables(processInstanceId);
			Map<String, Object> variables = runtimeService.getVariables(task.getExecutionId());
			log.debug("任务变量:{}", JSONUtil.toJsonStr(taskServiceVariables));
			log.debug("流程节点:{}", JSONUtil.toJsonStr(runtimeServiceVariables));
			log.debug("执行节点变量:{}", JSONUtil.toJsonStr(variables));

			String taskDefinitionKey = task.getTaskDefinitionKey();
			String processDefinitionId = task.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId);

			TaskDto taskDto = new TaskDto();
			taskDto.setFlowId(flowId);
			taskDto.setTaskCreateTime(task.getCreateTime());
			taskDto.setNodeId(taskDefinitionKey);
			taskDto.setParamMap(taskServiceVariables);
			taskDto.setProcessInstanceId(processInstanceId);
			taskDto.setTaskId(taskId);
			taskDto.setAssign(task.getAssignee());
			taskDto.setTaskName(task.getName());

			taskDtoList.add(taskDto);
		});

		long count = taskQuery.count();

		log.debug("当前有" + count + " 个任务:");

		pageResultDto.setTotal(count);
		pageResultDto.setRecords(taskDtoList);

		return R.ok(pageResultDto);
	}

}
