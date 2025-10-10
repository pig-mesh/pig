package com.pig4cloud.pigx.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.ProcessNodeRecordAssignUserParamDto;
import com.pig4cloud.pigx.flow.entity.ProcessNodeRecordAssignUser;

/**
 * 流程节点执行人管理服务接口
 * <p>
 * 该服务负责管理流程节点的执行人分配和任务完成状态。在流程执行过程中，
 * 每个节点可能有多个执行人（如会签、或签场景），该服务记录每个执行人的
 * 分配情况、处理状态和处理结果。同时提供任务完成事件的处理机制，支持
 * 多人审批场景下的复杂业务逻辑。
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-10
 */
public interface IProcessNodeRecordAssignUserService extends IService<ProcessNodeRecordAssignUser> {

	/**
	 * 为流程节点添加执行人
	 * <p>
	 * 在流程运行时动态分配任务执行人。该接口支持为单个节点分配多个执行人，
	 * 适用于会签（所有人都需要审批）、或签（任意一人审批即可）等场景。
	 * 分配时会记录执行人信息、分配时间和任务类型，并触发相应的任务通知。
	 * </p>
	 *
	 * @param processNodeRecordAssignUserParamDto 执行人分配参数，包含：
	 *                                            - processInstanceId: 流程实例ID
	 *                                            - nodeId: 节点ID
	 *                                            - nodeRecordId: 节点记录ID
	 *                                            - userId: 执行人用户ID
	 *                                            - taskType: 任务类型（审批/抄送/加签等）
	 *                                            - multiInstanceType: 多实例类型（会签/或签）
	 * @return R 响应结果，成功返回分配记录ID，失败返回错误信息（如用户不存在、重复分配等）
	 */
	R addAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

	/**
	 * 处理任务完成事件
	 * <p>
	 * 当执行人完成任务时调用此接口，更新执行人的任务状态和处理结果。
	 * 对于多人审批场景，会根据配置的审批规则（会签/或签）判断整个节点
	 * 是否完成。如果节点完成，会自动推进流程到下一节点。同时记录审批
	 * 意见、审批时间等信息，用于流程追溯。
	 * </p>
	 *
	 * @param processNodeRecordAssignUserParamDto 任务完成参数，包含：
	 *                                            - assignUserId: 执行人记录ID
	 *                                            - taskId: Flowable任务ID
	 *                                            - approved: 是否同意
	 *                                            - comment: 审批意见
	 *                                            - variables: 流程变量（可选）
	 * @return R 响应结果，成功返回节点是否全部完成的状态，失败返回错误信息
	 */
	R completeTaskEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

}
