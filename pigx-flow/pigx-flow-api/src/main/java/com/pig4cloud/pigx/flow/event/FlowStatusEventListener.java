package com.pig4cloud.pigx.flow.event;

import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.flow.dto.ProcessInstanceParamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * 流程状态事件监听器
 * <p>
 * 监听 Redis Channel，将事件分发给匹配的 IProcessInstanceStatusEventService 实现
 * </p>
 *
 * @author lengleng
 * @date 2026-02-10
 */
@Slf4j
@RequiredArgsConstructor
public class FlowStatusEventListener implements MessageListener {

	private final List<IProcessInstanceStatusEventService> eventServices;

	private final StringRedisTemplate redisTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String body = redisTemplate.getStringSerializer().deserialize(message.getBody());
		ProcessInstanceParamDto paramDto = JSONUtil.toBean(body, ProcessInstanceParamDto.class);

		log.debug("收到流程状态事件: flowId={}, processInstanceId={}", paramDto.getFlowId(),
				paramDto.getProcessInstanceId());

		// 分发给匹配 flowId 的实现
		for (IProcessInstanceStatusEventService service : eventServices) {
			if (service.getFlowId().equals(paramDto.getFlowId())) {
				try {
					service.handleStatusEvent(paramDto);
				}
				catch (Exception e) {
					log.error("处理流程状态事件失败: flowId={}, service={}", paramDto.getFlowId(),
							service.getClass().getSimpleName(), e);
				}
			}
		}
	}

}
