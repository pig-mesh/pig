package com.pig4cloud.pigx.flow.event;

import com.pig4cloud.pigx.flow.dto.ProcessInstanceParamDto;

/**
 * 流程实例状态事件服务接口
 * <p>
 * 实现此接口以监听流程状态变更事件。
 * 通过 Redis Pub/Sub 实现跨模块通知。
 * </p>
 *
 * @author lengleng
 * @date 2026-02-10
 */
public interface IProcessInstanceStatusEventService {

	/**
	 * 获取监听的流程定义Key
	 * @return 监听的流程定义Key
	 */
	String getFlowId();

	/**
	 * 处理状态事件
	 * @param paramDto 流程实例参数DTO
	 */
	void handleStatusEvent(ProcessInstanceParamDto paramDto);

}
