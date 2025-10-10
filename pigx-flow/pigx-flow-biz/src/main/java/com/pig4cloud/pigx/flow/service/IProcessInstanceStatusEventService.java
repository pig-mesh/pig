package com.pig4cloud.pigx.flow.service;

import com.pig4cloud.pigx.flow.dto.ProcessInstanceParamDto;

/**
 * 流程实例状态事件服务接口
 *
 * @author lengleng
 * @date 2025/08/08
 */
public interface IProcessInstanceStatusEventService {

	/**
	 * 获取监听的流程定义Key
	 * @return 监听的流程定义Key
	 */
	String getFlowId();

	/**
	 * 处理状态事件
	 * @param paramDto 流程节点记录分配用户参数DTO
	 */
	void handleStatusEvent(ProcessInstanceParamDto paramDto);

}
