package com.pig4cloud.pigx.flow.service;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.TaskParamDto;

/**
 * 任务处理服务接口
 * <p>
 * 提供流程任务的核心操作功能，包括任务的查询、完成、委托、退回等。 该服务是流程执行过程中最重要的接口，处理所有与任务相关的业务逻辑。
 * </p>
 * 
 * @author pigx
 */
public interface ITaskService {

	/**
	 * 查询任务详情
	 * <p>
	 * 获取任务的详细信息，包括： - 任务基本信息（名称、创建时间、执行人等） - 所属流程信息 - 表单数据和权限 - 历史审批记录
	 * </p>
	 * @param taskId 任务ID
	 * @param view 是否为查看模式（true=只查看，false=处理模式）
	 * @return 任务详情信息
	 */
	R queryTask(String taskId, boolean view);

	/**
	 * 查询任务详情（支持选择表单数据源）
	 * <p>
	 * 获取任务的详细信息，包括： - 任务基本信息（名称、创建时间、执行人等） - 所属流程信息 - 表单数据和权限 - 历史审批记录
	 * </p>
	 * @param taskId 任务ID
	 * @param view 是否为查看模式（true=只查看，false=处理模式）
	 * @param useMainFormData 是否使用主表单数据（true=从process_instance_record.form_data读取最新数据，false=从节点快照读取）
	 * @return 任务详情信息
	 */
	R queryTask(String taskId, boolean view, boolean useMainFormData);

	/**
	 * 完成任务
	 * <p>
	 * 处理当前任务并使流程继续执行。根据不同的处理结果： - 同意：任务通过，流程按照定义继续执行 - 拒绝：根据拒绝策略结束流程或跳转到指定节点
	 * </p>
	 * @param taskParamDto 任务参数，包括任务ID、处理意见、表单数据等
	 * @return 任务完成结果
	 */
	R completeTask(TaskParamDto taskParamDto);

	/**
	 * 委托任务（前加签）
	 * <p>
	 * 将当前任务委托给其他人处理。委托后： - 原执行人不再拥有该任务 - 新执行人完成任务后，需要原执行人确认（resolveTask） -
	 * 通常用于审批人需要征求他人意见的场景
	 * </p>
	 * @param taskParamDto 任务参数，必须包含新执行人信息
	 * @return 委托结果
	 */
	R delegateTask(TaskParamDto taskParamDto);

	/**
	 * 加签完成任务
	 * <p>
	 * 当任务被委托后，委托人完成任务，原执行人使用此方法确认并完成任务。 这是委托流程的最后一步，确认委托结果并使流程继续。
	 * </p>
	 * @param taskParamDto 任务参数
	 * @return 任务完成结果
	 */
	R resolveTask(TaskParamDto taskParamDto);

	/**
	 * 设置任务执行人
	 * <p>
	 * 修改任务的执行人，通常用于： - 管理员重新分配任务 - 无执行人时指定执行人 - 任务转办
	 * </p>
	 * @param taskParamDto 任务参数，必须包含新执行人信息
	 * @return 操作结果
	 */
	R setAssignee(TaskParamDto taskParamDto);

	/**
	 * 结束流程实例
	 * <p>
	 * 强制结束正在执行的流程实例。通常用于： - 流程异常需要终止 - 管理员强制结束 - 业务需求变更
	 * </p>
	 * @param taskParamDto 任务参数，包含结束原因
	 * @return 操作结果
	 */
	R stopProcessInstance(TaskParamDto taskParamDto);

	/**
	 * 退回任务
	 * <p>
	 * 将当前任务退回到上一个节点或指定节点。通常用于： - 发现上一步审批有误 - 需要补充资料 - 业务流程需要回退重新处理
	 * </p>
	 * @param taskParamDto 任务参数，可指定退回到的节点
	 * @return 操作结果
	 */
	R back(TaskParamDto taskParamDto);

	/**
	 * 查询首页数据看板
	 * <p>
	 * 获取当前用户的任务统计数据，包括： - 待办任务数量 - 已办任务数量 - 我发起的流程数量 - 抄送给我的数量
	 * </p>
	 * @return 任务统计数据
	 */
	R queryTaskData();

	/**
	 * 重新提交任务（驳回到发起人后）
	 * <p>
	 * 当流程被驳回到发起人时，发起人使用此方法重新编辑表单并提交。
	 * 与普通completeTask不同：
	 * 1. 不需要填写审批意见
	 * 2. 会清理 rejectToStarter 流程变量
	 * 3. 完成后流程继续到下一个审批节点
	 * </p>
	 * @param taskParamDto 任务参数，包含taskId和更新后的formData
	 * @return 操作结果
	 */
	R resubmitTask(TaskParamDto taskParamDto);

}
