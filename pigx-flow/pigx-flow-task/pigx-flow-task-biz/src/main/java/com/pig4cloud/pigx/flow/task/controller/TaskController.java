package com.pig4cloud.pigx.flow.task.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.TaskParamDto;
import com.pig4cloud.pigx.flow.task.service.ITaskService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

/**
 * 任务实例
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

	private final ITaskService taskService;

	/**
	 * 查询首页数据看板
	 * @return
	 */
	@SneakyThrows
	@GetMapping("queryTaskData")
	public R queryTaskData() {
		return taskService.queryTaskData();
	}

	/**
	 * 查询任务
	 * @param taskId
	 * @return
	 */
	@SneakyThrows
	@GetMapping("queryTask")
	public R queryTask(String taskId, boolean view) {

		return taskService.queryTask(taskId, view);

	}

	/**
	 * 完成任务
	 * @param completeParamDto
	 * @return
	 */
	@SneakyThrows
	@PostMapping("completeTask")
	public R completeTask(@RequestBody TaskParamDto completeParamDto) {

		return taskService.completeTask(completeParamDto);

	}

	/**
	 * 前加签
	 * @param completeParamDto
	 * @return
	 */
	@SneakyThrows
	@PostMapping("delegateTask")
	public R delegateTask(@RequestBody TaskParamDto completeParamDto) {

		return taskService.delegateTask(completeParamDto);

	}

	/**
	 * 加签完成任务
	 * @param completeParamDto
	 * @return
	 */
	@SneakyThrows
	@PostMapping("resolveTask")
	public R resolveTask(@RequestBody TaskParamDto completeParamDto) {

		return taskService.resolveTask(completeParamDto);

	}

	/**
	 * 设置执行人
	 * @param completeParamDto
	 * @return
	 */
	@SneakyThrows
	@PostMapping("setAssignee")
	public R setAssignee(@RequestBody TaskParamDto completeParamDto) {

		return taskService.setAssignee(completeParamDto);

	}

	/**
	 * 结束流程
	 * @param completeParamDto
	 * @return
	 */
	@SneakyThrows
	@PostMapping("stopProcessInstance")
	public R stopProcessInstance(@RequestBody TaskParamDto completeParamDto) {

		return taskService.stopProcessInstance(completeParamDto);

	}

	/**
	 * 退回
	 * @param taskParamDto
	 * @return
	 */
	@PostMapping("back")
	public R back(@RequestBody TaskParamDto taskParamDto) {
		return taskService.back(taskParamDto);
	}

}
