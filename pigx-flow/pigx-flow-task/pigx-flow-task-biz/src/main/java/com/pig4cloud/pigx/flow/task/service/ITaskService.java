package com.pig4cloud.pigx.flow.task.service;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.TaskParamDto;

/**
 * 任务处理
 */
public interface ITaskService {

	/**
	 * 查询任务
	 *
	 * @param taskId 任务ID
	 * @param view   是否查看
	 * @return 查询结果
	 */
	R queryTask(String taskId, boolean view);

	/**
	 * 完成任务
	 *
	 * @param taskParamDto 任务参数DTO
	 * @return 任务执行结果
	 */
	R completeTask(TaskParamDto taskParamDto);

	/**
	 * 委托任务（前加签）
	 *
	 * @param taskParamDto 任务参数DTO
	 * @return 委托任务结果
	 */
	R delegateTask(TaskParamDto taskParamDto);

	/**
	 * 加签完成任务
	 *
	 * @param taskParamDto 任务参数DTO
	 * @return 任务处理结果
	 */
	R resolveTask(TaskParamDto taskParamDto);

	/**
	 * 设置任务执行人
	 *
	 * @param taskParamDto 任务参数DTO
	 * @return 操作结果
	 */
	R setAssignee(TaskParamDto taskParamDto);

	/**
	 * 结束流程实例
	 *
	 * @param taskParamDto 任务参数DTO
	 * @return 操作结果
	 */
	R stopProcessInstance(TaskParamDto taskParamDto);

	/**
	 * 退回任务
	 *
	 * @param taskParamDto 任务参数DTO
	 * @return 操作结果
	 */
	R back(TaskParamDto taskParamDto);

	/**
	 * 查询首页数据看板
	 *
	 * @return 任务数据结果
	 */
	R queryTaskData();

}
