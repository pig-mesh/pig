package com.pig4cloud.pigx.flow.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.ProcessInstanceParamDto;
import com.pig4cloud.pigx.flow.dto.TaskQueryParamDto;
import com.pig4cloud.pigx.flow.service.IProcessInstanceService;
import com.pig4cloud.pigx.flow.vo.NodeFormatParamVo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

/**
 * 流程实例管理控制器
 * <p>
 * 该控制器提供流程实例的全生命周期管理功能，是工作流系统的核心业务接口。
 * 主要功能包括：
 * 1. 流程启动 - 根据流程定义创建新的流程实例
 * 2. 任务查询 - 查询待办、已办、我发起的、抄送给我的任务
 * 3. 流程监控 - 查看流程实例详情和执行状态
 * 4. 流程展示 - 格式化展示流程节点和执行路径
 * </p>
 * <p>
 * 与EngineFlowController不同，该控制器提供的是面向业务的高级接口，
 * 包含了业务逻辑处理、权限控制、数据格式化等功能。
 * </p>
 *
 * @author pigx
 * @since 2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/process-instance")
public class ProcessInstanceController {

	private final IProcessInstanceService processInstanceService;

	/**
	 * 启动新的流程实例
	 * <p>
	 * 根据流程定义启动一个新的流程实例。该接口会：
	 * 1. 验证用户是否有启动该流程的权限
	 * 2. 创建流程实例记录并保存表单数据
	 * 3. 根据流程配置分配第一个任务节点
	 * 4. 处理流程抄送和通知
	 * </p>
	 *
	 * @param processInstanceParamDto 流程启动参数，包含：
	 *                                - processId: 流程定义ID
	 *                                - formData: 表单数据
	 *                                - startUserId: 发起人用户ID
	 *                                - copyUserList: 抄送人员列表
	 * @return R 统一响应对象，成功返回流程实例ID
	 */
	@SneakyThrows
	@PostMapping("startProcessInstance")
	public R startProcessInstance(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {
		return processInstanceService.startProcessInstance(processInstanceParamDto);
	}

	/**
	 * 分页查询当前用户的待办任务
	 * <p>
	 * 查询分配给当前登录用户的所有待处理任务。支持按流程名称、
	 * 创建时间等条件过滤。返回的数据包含任务信息和关联的表单数据。
	 * </p>
	 *
	 * @param queryParamDto 查询参数，包含：
	 *                      - processName: 流程名称（可选）
	 *                      - taskTime: 任务创建时间范围（可选）
	 *                      - current: 当前页码
	 *                      - size: 每页大小
	 * @return R 统一响应对象，包含分页的待办任务列表
	 */
	@SneakyThrows
	@PostMapping("queryMineTask")
	public R queryMineTask(@RequestBody TaskQueryParamDto queryParamDto) {
		return processInstanceService.queryMineTask(queryParamDto);
	}

	/**
	 * 分页查询当前登录用户的已办任务
	 * <p>
	 * 查询当前用户已经完成处理的历史任务。包括审批通过、拒绝、
	 * 转办等各种已完成状态的任务。返回数据包含任务处理结果和处理时间。
	 * </p>
	 *
	 * @param taskQueryParamDto 查询参数，支持按流程名称、完成时间等条件过滤
	 * @return R 统一响应对象，包含分页的已办任务列表
	 */
	@SneakyThrows
	@PostMapping("queryMineEndTask")
	public R queryMineEndTask(@RequestBody TaskQueryParamDto taskQueryParamDto) {
		return processInstanceService.queryMineEndTask(taskQueryParamDto);
	}

	/**
	 * 分页查询我发起的流程
	 * <p>
	 * 查询当前用户作为发起人创建的所有流程实例。
	 * 包括运行中、已完成、已终止等各种状态的流程。
	 * 用户可以查看自己发起流程的执行进度和当前状态。
	 * </p>
	 *
	 * @param taskQueryParamDto 查询参数，支持按流程名称、发起时间等条件过滤
	 * @return R 统一响应对象，包含分页的流程实例列表
	 */
	@SneakyThrows
	@PostMapping("queryMineStarted")
	public R queryMineStarted(@RequestBody TaskQueryParamDto taskQueryParamDto) {
		return processInstanceService.queryMineStarted(taskQueryParamDto);
	}

	/**
	 * 分页查询抄送给我的流程
	 * <p>
	 * 查询抄送给当前用户的流程信息。抄送是流程中的通知机制，
	 * 被抄送人只需了解流程进展，无需处理任务。
	 * 通常用于让相关人员知晓重要流程的执行情况。
	 * </p>
	 *
	 * @param taskQueryParamDto 查询参数，支持按流程名称、抄送时间等条件过滤
	 * @return R 统一响应对象，包含分页的抄送记录列表
	 */
	@SneakyThrows
	@PostMapping("queryMineCC")
	public R queryMineCC(@RequestBody TaskQueryParamDto taskQueryParamDto) {
		return processInstanceService.queryMineCC(taskQueryParamDto);
	}

	/**
	 * 格式化显示流程节点
	 * <p>
	 * 根据流程定义和表单数据，格式化显示流程节点信息。
	 * 该接口主要用于流程发起页面，根据表单数据动态计算并展示：
	 * 1. 各节点的审批人员
	 * 2. 条件分支的走向
	 * 3. 抄送人员列表
	 * 帮助用户在发起流程前预览流程走向。
	 * </p>
	 *
	 * @param nodeFormatParamVo 节点格式化参数，包含：
	 *                          - processId: 流程定义ID
	 *                          - formData: 表单数据
	 * @return R 统一响应对象，包含格式化后的节点显示信息
	 */
	@PostMapping("formatStartNodeShow")
	public R formatStartNodeShow(@RequestBody NodeFormatParamVo nodeFormatParamVo) {
		return processInstanceService.formatStartNodeShow(nodeFormatParamVo);
	}

	/**
	 * 查询流程实例详情
	 * <p>
	 * 获取指定流程实例的完整信息，包括：
	 * 1. 流程基本信息 - 发起人、发起时间、当前状态等
	 * 2. 表单数据 - 流程关联的业务表单数据
	 * 3. 执行历史 - 各节点的审批记录、处理人、处理意见等
	 * 4. 流程图 - 当前执行节点和路径高亮显示
	 * </p>
	 *
	 * @param processInstanceId 流程实例ID
	 * @return R 统一响应对象，包含流程实例的完整详情
	 */
	@GetMapping("detail")
	public R detail(String processInstanceId) {
		return processInstanceService.detail(processInstanceId);
	}

}
