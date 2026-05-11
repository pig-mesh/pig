package com.pig4cloud.pigx.flow.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.TaskParamDto;
import com.pig4cloud.pigx.flow.service.ITaskService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;

/**
 * 任务管理控制器
 * <p>
 * 该控制器提供任务操作的高级业务接口，是面向最终用户的任务处理入口。 与EngineTaskController不同，该控制器封装了完整的业务逻辑，包括： 1. 任务查询 -
 * 查看任务详情、首页统计数据 2. 任务处理 - 完成任务、退回任务、终止流程 3. 高级操作 - 加签、委托、转办等特殊处理
 * </p>
 * <p>
 * 所有操作都包含权限验证、业务校验、操作记录等完整的业务处理流程， 确保任务处理的合规性和可追溯性。
 * </p>
 *
 * @author pigx
 * @since 2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class ProcessTaskController {

	private final ITaskService taskService;

	/**
	 * 查询任务统计数据看板
	 * <p>
	 * 获取当前登录用户的任务统计信息，用于首页或工作台展示。 返回的统计数据可能包括：待办数量、已办数量、超时任务数等。
	 * </p>
	 * @return R 统一响应对象，包含任务统计数据
	 */
	@SneakyThrows
	@GetMapping("queryTaskData")
	public R queryTaskData() {
		return taskService.queryTaskData();
	}

	/**
	 * 查询任务详情
	 * <p>
	 * 获取指定任务的完整信息，包括： 1. 任务基本信息 - 任务名称、创建时间、处理人等 2. 流程信息 - 所属流程、发起人、流程状态等 3. 表单数据 -
	 * 与任务关联的业务表单信息 4. 操作权限 - 根据用户角色和任务状态判断可执行的操作
	 * </p>
	 * @param taskId 任务ID
	 * @param view 是否为查看模式：true-仅查看，false-可编辑处理
	 * @param useMainFormData 是否使用主表单数据（true=从process_instance_record.form_data读取最新数据，false=从节点快照读取，默认true）
	 * @return R 统一响应对象，包含任务的完整信息和可操作项
	 */
	@SneakyThrows
	@GetMapping("queryTask")
	public R queryTask(String taskId, boolean view,
			@RequestParam(defaultValue = "true") boolean useMainFormData) {

		return taskService.queryTask(taskId, view, useMainFormData);

	}

	/**
	 * 完成任务
	 * <p>
	 * 处理当前任务，支持多种完成方式： 1. 通过 - 同意任务审批，流程继续往下执行 2. 拒绝 - 不同意任务审批，流程可能结束或进入其他分支 3. 附带意见 -
	 * 可以添加审批意见和附件 完成后会记录操作日志，并根据流程配置进行后续处理。
	 * </p>
	 * @param completeParamDto 任务完成参数，包含： - taskId: 任务ID - result: 处理结果（通过/拒绝） - comment:
	 * 审批意见 - variables: 流程变量
	 * @return R 统一响应对象，操作成功返回下一节点信息
	 */
	@SneakyThrows
	@PostMapping("completeTask")
	public R completeTask(@RequestBody TaskParamDto completeParamDto) {
		return taskService.completeTask(completeParamDto);
	}

	/**
	 * 前加签任务
	 * <p>
	 * 在任务处理前添加其他审批人。前加签是指在当前任务处理前， 先让其他人进行审批，审批完成后再回到当前处理人。 通常用于需要征求其他人意见或补充审批的场景。
	 * </p>
	 * @param completeParamDto 加签参数，包含： - taskId: 当前任务ID - targetUserIdList: 加签用户ID列表 -
	 * comment: 加签说明
	 * @return R 统一响应对象，操作成功返回加签任务信息
	 */
	@SneakyThrows
	@PostMapping("delegateTask")
	public R delegateTask(@RequestBody TaskParamDto completeParamDto) {

		return taskService.delegateTask(completeParamDto);

	}

	/**
	 * 加签完成任务
	 * <p>
	 * 加签人完成加签任务后，任务会返回给原处理人。 该接口用于处理加签任务的完成操作，支持添加审批意见。 完成后，原处理人可以继续处理该任务。
	 * </p>
	 * @param completeParamDto 加签完成参数，包含： - taskId: 加签任务ID - comment: 加签意见 - variables:
	 * 需要传递的变量
	 * @return R 统一响应对象，操作成功后任务返回给原处理人
	 */
	@SneakyThrows
	@PostMapping("resolveTask")
	public R resolveTask(@RequestBody TaskParamDto completeParamDto) {

		return taskService.resolveTask(completeParamDto);

	}

	/**
	 * 设置任务执行人（转办）
	 * <p>
	 * 将任务转办给其他用户处理。与加签不同，转办后任务不会返回给原处理人。 通常用于当前处理人无法处理该任务，需要其他人代为处理的场景。 转办后会保留转办记录。
	 * </p>
	 * @param completeParamDto 转办参数，包含： - taskId: 任务ID - targetUserIdList: 目标用户ID列表 -
	 * comment: 转办说明
	 * @return R 统一响应对象，操作成功返回转办信息
	 */
	@SneakyThrows
	@PostMapping("setAssignee")
	public R setAssignee(@RequestBody TaskParamDto completeParamDto) {

		return taskService.setAssignee(completeParamDto);

	}

	/**
	 * 终止流程实例
	 * <p>
	 * 强制终止当前流程实例的执行。该操作不可逆，流程终止后无法恢复。 通常用于： 1. 流程发起错误需要撤销 2. 业务变更导致流程无需继续 3. 紧急情况下的流程中止
	 * 需要有相应权限才能执行此操作。
	 * </p>
	 * @param completeParamDto 终止参数，包含： - processInstanceId: 流程实例ID - reason: 终止原因
	 * @return R 统一响应对象，操作成功返回终止信息
	 */
	@SneakyThrows
	@PostMapping("stopProcessInstance")
	public R stopProcessInstance(@RequestBody TaskParamDto completeParamDto) {

		return taskService.stopProcessInstance(completeParamDto);

	}

    /**
     * 撤回流程实例
     *
     * @param taskParamDto 撤回参数，包含流程实例ID
     * @return R 统一响应对象，操作成功返回撤回信息
     */
    @PostMapping("withdrawProcessInstance")
    public R withdrawProcessInstance(@RequestBody TaskParamDto taskParamDto) {
        return taskService.withdrawProcessInstance(taskParamDto);
    }

	/**
	 * 退回任务
	 * <p>
	 * 将当前任务退回到上一个或指定的节点重新处理。 退回操作会： 1. 撤销当前节点的处理 2. 流程返回到指定节点 3. 保留退回记录和原因
	 * 退回后，被退回节点的处理人需要重新处理任务。
	 * </p>
	 * @param taskParamDto 退回参数，包含： - taskId: 当前任务ID - targetNodeId: 目标节点ID（可选，不指定则退回上一节点）
	 * - reason: 退回原因
	 * @return R 统一响应对象，操作成功返回退回信息
	 */
	@PostMapping("back")
	public R back(@RequestBody TaskParamDto taskParamDto) {
		return taskService.back(taskParamDto);
	}

	/**
	 * 重新提交任务（驳回到发起人后）
	 * <p>
	 * 当流程被驳回到发起人后，发起人可使用此接口重新编辑表单并提交。
	 * 与completeTask不同：
	 * 1. 不需要填写审批意见
	 * 2. 会自动清理驳回标识
	 * 3. 提交后流程继续到下一个审批节点
	 * </p>
	 * @param taskParamDto 重新提交参数，包含：
	 *                     - taskId: 发起人节点的任务ID
	 *                     - formData: 更新后的表单数据
	 * @return R 统一响应对象，操作成功返回提交结果
	 */
	@PostMapping("resubmitTask")
	public R resubmitTask(@RequestBody TaskParamDto taskParamDto) {
		return taskService.resubmitTask(taskParamDto);
	}

}
