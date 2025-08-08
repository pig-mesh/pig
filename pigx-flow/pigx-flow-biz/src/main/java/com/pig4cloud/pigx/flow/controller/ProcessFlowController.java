package com.pig4cloud.pigx.flow.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.flow.dto.ProcessInstanceParamDto;
import com.pig4cloud.pigx.flow.dto.TaskDto;
import com.pig4cloud.pigx.flow.dto.TaskQueryParamDto;
import com.pig4cloud.pigx.flow.support.utils.NodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.TaskQuery;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * 流程引擎核心控制器
 * <p>
 * 该控制器是工作流引擎的核心接口，直接与Flowable引擎交互，提供流程生命周期管理功能： 1. 流程定义的创建和部署 - 将前端设计的流程模型转换为BPMN格式并部署到引擎
 * 2. 流程实例的启动和管理 - 启动流程实例、停止运行中的流程 3. 任务的查询和处理 - 查询待办任务、已办任务，执行任务审批
 * </p>
 * <p>
 * 该控制器主要供内部系统调用，通过封装Flowable原生API，简化流程操作。 所有操作都支持多租户隔离，确保数据安全。
 * </p>
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/flow")
public class ProcessFlowController {

	private final TaskService taskService;

	private final HistoryService historyService;

	private final RepositoryService repositoryService;

	private final RuntimeService runtimeService;

	/**
	 * 启动流程实例
	 * <p>
	 * 根据流程定义ID启动一个新的流程实例。启动时可以传入流程变量， 这些变量会在整个流程执行过程中使用（如条件判断、人员分配等）。
	 * </p>
	 * @param processInstanceParamDto 启动参数，包含： - flowId: 流程定义ID - startUserId: 发起人用户ID -
	 * paramMap: 流程变量Map，用于流程执行中的条件判断和数据传递
	 * @return R 统一响应对象，包含启动成功的流程实例ID
	 */
	@PostMapping("/start")
	public R start(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {
		Authentication.setAuthenticatedUserId(processInstanceParamDto.getStartUserId());
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKeyAndTenantId(
				processInstanceParamDto.getFlowId(), processInstanceParamDto.getParamMap(),
				TenantContextHolder.getTenantId().toString());

		String processInstanceId = processInstance.getProcessInstanceId();
		return R.ok(processInstanceId);

	}

	/**
	 * 审批任务（测试接口）
	 * <p>
	 * 该接口用于测试流程审批功能，完成指定的任务并设置审批结果。 实际业务中应使用TaskController中更完善的审批接口。
	 * </p>
	 * @param taskId 待审批的任务ID
	 * @param approved 审批结果：true-通过，false-拒绝
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
	 * 分页查询用户已办任务
	 * <p>
	 * 查询指定用户已经完成的历史任务，支持按时间范围过滤。 返回的任务信息包含任务处理时长、完成时间等历史数据。
	 * </p>
	 * @param taskQueryParamDto 查询参数，包含： - assign: 任务处理人ID - taskTime: 任务时间范围数组 [开始时间,
	 * 结束时间] - current: 当前页码 - size: 每页大小
	 * @return R 统一响应对象，包含分页的已办任务列表（TaskDto）
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

		List<HistoricActivityInstance> list = activityInstanceQuery
			.listPage((taskQueryParamDto.getCurrent() - 1) * taskQueryParamDto.getSize(), taskQueryParamDto.getSize());

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
	 * 分页查询用户待办任务
	 * <p>
	 * 查询分配给指定用户的待处理任务，支持按流程名称和创建时间范围过滤。 待办任务是需要用户处理的活动任务，处理后任务将流转到下一节点。
	 * </p>
	 * @param taskQueryParamDto 查询参数，包含： - assign: 任务处理人ID - processName: 流程名称（可选，用于过滤） -
	 * taskTime: 任务创建时间范围数组 [开始时间, 结束时间] - current: 当前页码 - size: 每页大小
	 * @return R 统一响应对象，包含分页的待办任务列表（TaskDto）
	 */
	@PostMapping("/queryAssignTask")
	public R queryAssignTask(@RequestBody TaskQueryParamDto taskQueryParamDto) {

		String assign = taskQueryParamDto.getAssign();

		List<TaskDto> taskDtoList = new ArrayList<>();

		int pageIndex = taskQueryParamDto.getCurrent() - 1;
		int pageSize = taskQueryParamDto.getSize();

		Page<TaskDto> pageResultDto = new Page<>();
		TaskQuery taskQuery = taskService.createTaskQuery().taskAssignee(assign).orderByTaskCreateTime().desc();
		if (StrUtil.isNotBlank(taskQueryParamDto.getProcessName())) {
			List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery()
				.processDefinitionName(taskQueryParamDto.getProcessName())
				.processInstanceTenantId(TenantContextHolder.getTenantId().toString())
				.list();
			if (CollUtil.isNotEmpty(processInstanceList)) {
				taskQuery.processInstanceIdIn(
						processInstanceList.stream().map(ProcessInstance::getProcessInstanceId).toList());
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
			log.debug("(taskId) {} processInstanceId={} executrionId={}", task.getName(), processInstanceId,
					task.getExecutionId());

			String taskDefinitionKey = task.getTaskDefinitionKey();
			String processDefinitionId = task.getProcessDefinitionId();
			String flowId = NodeUtil.getFlowId(processDefinitionId);

			TaskDto taskDto = new TaskDto();
			taskDto.setFlowId(flowId);
			taskDto.setTaskCreateTime(task.getCreateTime());
			taskDto.setNodeId(taskDefinitionKey);
			taskDto.setProcessInstanceId(processInstanceId);
			taskDto.setTaskId(taskId);
			taskDto.setAssign(task.getAssignee());
			taskDto.setTaskName(task.getName());
			taskDtoList.add(taskDto);
		});

		long count = taskQuery.count();

		log.debug("当前有 {} 个任务:", count);

		pageResultDto.setTotal(count);
		pageResultDto.setRecords(taskDtoList);

		return R.ok(pageResultDto);
	}

}
