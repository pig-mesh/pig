package com.pig4cloud.pigx.flow.engine.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.IndexPageStatistics;
import com.pig4cloud.pigx.flow.task.dto.VariableQueryParamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstanceQuery;
import org.flowable.task.api.TaskQuery;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 任务控制器
 */
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/process-instance")
public class EngineProcessInstanceController {

	private final TaskService taskService;

	private final HistoryService historyService;

	private final RuntimeService runtimeService;

	/**
	 * 查询首页统计数量
	 * @param userId 用户ID
	 * @return 统计结果
	 */
	@GetMapping("querySimpleData")
	public R<IndexPageStatistics> querySimpleData(long userId) {
		TaskQuery taskQuery = taskService.createTaskQuery();

		// 待办数量
		long pendingNum = taskQuery.taskAssignee(String.valueOf(userId)).count();
		// 已完成任务
		HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService
			.createHistoricActivityInstanceQuery();

		long completedNum = historicActivityInstanceQuery.taskAssignee(String.valueOf(userId)).finished().count();

		IndexPageStatistics indexPageStatistics = IndexPageStatistics.builder()
			.pendingNum(pendingNum)
			.completedNum(completedNum)
			.build();

		return R.ok(indexPageStatistics);
	}

	/**
	 * 查询流程变量
	 * @param paramDto 查询参数
	 * @return 流程变量
	 */
	@PostMapping("queryVariables")
	public R queryVariables(@RequestBody VariableQueryParamDto paramDto) {

		Map<String, Object> variables = runtimeService.getVariables(paramDto.getExecutionId());

		return R.ok(variables);

	}

}
