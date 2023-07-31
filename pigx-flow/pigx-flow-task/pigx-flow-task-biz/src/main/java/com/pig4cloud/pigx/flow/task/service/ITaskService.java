package com.pig4cloud.pigx.flow.task.service;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.TaskParamDto;

/**
 * 任务处理
 */
public interface ITaskService {

	/**
	 * 查询任务
	 * @param taskId
	 * @param view
	 * @return
	 */
	R queryTask(String taskId, boolean view);

	/**
	 * 完成任务
	 * @param taskParamDto
	 * @return
	 */
	R completeTask(TaskParamDto taskParamDto);

	/**
	 * 前加签
	 * @param taskParamDto
	 * @return
	 */
	R delegateTask(TaskParamDto taskParamDto);

	/**
	 * 加签完成任务
	 * @param taskParamDto
	 * @return
	 */
	R resolveTask(TaskParamDto taskParamDto);

	/**
	 * 设置执行人
	 * @param taskParamDto
	 * @return
	 */
	R setAssignee(TaskParamDto taskParamDto);

	/**
	 * 结束流程
	 * @param taskParamDto
	 * @return
	 */
	R stopProcessInstance(TaskParamDto taskParamDto);

	/**
	 * 退回
	 * @param taskParamDto
	 * @return
	 */
	R back(TaskParamDto taskParamDto);

	/**
	 * 查询首页数据看板
	 * @return
	 */
	R queryTaskData();

}
