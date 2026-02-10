package com.pig4cloud.pigx.flow.event;

import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.flow.dto.ProcessInstanceParamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 流程状态事件发布者
 * <p>
 * 将流程状态变更事件发布到 Redis Channel
 * </p>
 *
 * @author lengleng
 * @date 2026-02-10
 */
@Slf4j
@RequiredArgsConstructor
public class FlowStatusEventPublisher {

	public static final String CHANNEL = "pigx:flow:status";

	private final StringRedisTemplate redisTemplate;

	/**
	 * 发布流程状态变更事件
	 * @param paramDto 流程实例参数
	 */
	public void publish(ProcessInstanceParamDto paramDto) {
		String message = JSONUtil.toJsonStr(paramDto);
		redisTemplate.convertAndSend(CHANNEL, message);
		log.debug("发布流程状态事件: flowId={}, processInstanceId={}", paramDto.getFlowId(),
				paramDto.getProcessInstanceId());
	}

}
