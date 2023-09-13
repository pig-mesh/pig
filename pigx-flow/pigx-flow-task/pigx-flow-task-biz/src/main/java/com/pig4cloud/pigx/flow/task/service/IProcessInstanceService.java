package com.pig4cloud.pigx.flow.task.service;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.ProcessInstanceParamDto;
import com.pig4cloud.pigx.flow.task.dto.TaskQueryParamDto;
import com.pig4cloud.pigx.flow.task.vo.NodeFormatParamVo;

/**
 * 流程实例进程
 */
public interface IProcessInstanceService {

	/**
	 * 启动流程
	 * @param processInstanceParamDto
	 * @return
	 */
	R startProcessInstance(ProcessInstanceParamDto processInstanceParamDto);

	/**
	 * 查询当前登录用户的待办任务
	 * @param taskQueryParamDto taskQueryParamDto
	 * @return
	 */
	R queryMineTask(TaskQueryParamDto taskQueryParamDto);

	/**
	 * 查询已办任务
	 * @param taskQueryParamDto
	 * @return
	 */
	R queryMineEndTask(TaskQueryParamDto taskQueryParamDto);

	/**
	 * 流程结束
	 * @param processsInstanceId
	 * @return
	 */
	R end(String processsInstanceId);

	/**
	 * 查询我发起的
	 * @param taskQueryParamDto
	 * @return
	 */
	R queryMineStarted(TaskQueryParamDto taskQueryParamDto);

	/**
	 * 查询抄送给我的
	 * @param taskQueryParamDto
	 * @return
	 */
	R queryMineCC(TaskQueryParamDto taskQueryParamDto);

	/**
	 * 格式化流程显示
	 * @param nodeFormatParamVo
	 * @return
	 */
	R formatStartNodeShow(NodeFormatParamVo nodeFormatParamVo);

	/**
	 * 流程详情
	 * @param processInstanceId
	 * @return
	 */
	R detail(String processInstanceId);

}
