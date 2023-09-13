package com.pig4cloud.pigx.flow.task.api.feign;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author lengleng
 * @date 2023/7/14
 */
@FeignClient(contextId = "remoteFlowEngineService", value = ServiceNameConstants.FLOW_ENGINE_SERVER)
public interface RemoteFlowEngineService {

	/**
	 * 查询任务变量
	 * @param variableQueryParamDto 变量查询参数
	 * @return 任务变量结果
	 */
	@PostMapping("/task/queryTaskVariables")
	R<Map<String, Object>> queryTaskVariables(@RequestBody VariableQueryParamDto variableQueryParamDto);

	/**
	 * 查询简单数据
	 * @param userId 用户ID
	 * @return 索引页统计数据
	 */
	@GetMapping("/process-instance/querySimpleData")
	R<IndexPageStatistics> querySimpleData(@RequestParam("userId") Long userId);

	/**
	 * 创建流程
	 * @param map 流程JSON对象
	 * @return 创建流程结果
	 */
	@PostMapping("/flow/create")
	R<String> createFlow(@RequestBody Map<String, Object> map);

	/**
	 * 启动流程实例
	 * @param processInstanceParamDto 流程实例参数
	 * @return 启动流程结果
	 */
	@PostMapping("/flow/start")
	R<String> startProcess(@RequestBody ProcessInstanceParamDto processInstanceParamDto);

	/**
	 * 查询待办任务
	 * @param paramDto 任务查询参数
	 * @return 待办任务列表
	 */
	@PostMapping("/flow/queryAssignTask")
	R<Page<TaskDto>> queryAssignTask(@RequestBody TaskQueryParamDto paramDto);

	/**
	 * 查询已完成任务
	 * @param paramDto 任务查询参数
	 * @return 已完成任务列表
	 */
	@PostMapping("/flow/queryCompletedTask")
	R<Page<TaskDto>> queryCompletedTask(@RequestBody TaskQueryParamDto paramDto);

	/**
	 * 完成任务
	 * @param taskParamDto 任务参数
	 * @return 完成任务结果
	 */
	@PostMapping("/task/complete")
	R<String> completeTask(@RequestBody TaskParamDto taskParamDto);

	/**
	 * 设置任务负责人
	 * @param taskParamDto 任务参数
	 * @return 设置负责人结果
	 */
	@PostMapping("/task/setAssignee")
	R<String> setAssignee(@RequestBody TaskParamDto taskParamDto);

	/**
	 * 停止流程实例
	 * @param taskParamDto 任务参数
	 * @return 停止流程实例结果
	 */
	@PostMapping("/flow/stopProcessInstance")
	R<String> stopProcessInstance(@RequestBody TaskParamDto taskParamDto);

	/**
	 * 解决任务
	 * @param taskParamDto 任务参数
	 * @return 解决任务结果
	 */
	@PostMapping("/task/resolveTask")
	R<String> resolveTask(@RequestBody TaskParamDto taskParamDto);

	/**
	 * 退回任务
	 * @param taskParamDto 任务参数
	 * @return 退回任务结果
	 */
	@PostMapping("/task/back")
	R<String> back(@RequestBody TaskParamDto taskParamDto);

	/**
	 * 委派任务
	 * @param taskParamDto 任务参数
	 * @return 委派任务结果
	 */
	@PostMapping("/task/delegateTask")
	R<String> delegateTask(@RequestBody TaskParamDto taskParamDto);

	/**
	 * 查询任务
	 * @param taskId 任务ID
	 * @param userId 用户ID
	 * @return 任务查询结果
	 */
	@GetMapping("/task/engine/queryTask")
	R<TaskResultDto> queryTask(@RequestParam("taskId") String taskId, @RequestParam("userId") Long userId);

}
