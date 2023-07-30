package com.pig4cloud.pigx.flow.task.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.ProcessInstanceParamDto;
import com.pig4cloud.pigx.flow.task.dto.TaskQueryParamDto;
import com.pig4cloud.pigx.flow.task.service.IProcessInstanceService;
import com.pig4cloud.pigx.flow.task.vo.NodeFormatParamVo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

/**
 * 流程实例
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/process-instance")
public class ProcessInstanceController {

	private final IProcessInstanceService processInstanceService;

	/**
	 * 启动流程
	 * @param processInstanceParamDto
	 * @return
	 */
	@SneakyThrows
	@PostMapping("startProcessInstance")
	public R startProcessInstance(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {
		return processInstanceService.startProcessInstance(processInstanceParamDto);
	}

	/**
	 * 查询当前登录用户的待办任务
	 * @param pageDto
	 * @return
	 */
	@SneakyThrows
	@PostMapping("queryMineTask")
	public R queryMineTask(@RequestBody TaskQueryParamDto queryParamDto) {
		return processInstanceService.queryMineTask(queryParamDto);
	}

	/**
	 * 查询当前登录用户已办任务
	 * @param taskQueryParamDto
	 * @return
	 */
	@SneakyThrows
	@PostMapping("queryMineEndTask")
	public R queryMineEndTask(@RequestBody TaskQueryParamDto taskQueryParamDto) {
		return processInstanceService.queryMineEndTask(taskQueryParamDto);
	}

	/**
	 * 查询我发起的
	 * @param pageDto
	 * @return
	 */
	@SneakyThrows
	@PostMapping("queryMineStarted")
	public R queryMineStarted(@RequestBody TaskQueryParamDto taskQueryParamDto) {
		return processInstanceService.queryMineStarted(taskQueryParamDto);
	}

	/**
	 * 查询抄送我的
	 * @param pageDto
	 * @return
	 */
	@SneakyThrows
	@PostMapping("queryMineCC")
	public R queryMineCC(@RequestBody TaskQueryParamDto taskQueryParamDto) {
		return processInstanceService.queryMineCC(taskQueryParamDto);
	}

	/**
	 * 格式化流程显示
	 * @param nodeFormatParamVo
	 * @return
	 */
	@PostMapping("formatStartNodeShow")
	public R formatStartNodeShow(@RequestBody NodeFormatParamVo nodeFormatParamVo) {
		return processInstanceService.formatStartNodeShow(nodeFormatParamVo);
	}

	/**
	 * 流程详情
	 * @param processInstanceId
	 * @return
	 */
	@GetMapping("detail")
	public R detail(String processInstanceId) {
		return processInstanceService.detail(processInstanceId);
	}

}
