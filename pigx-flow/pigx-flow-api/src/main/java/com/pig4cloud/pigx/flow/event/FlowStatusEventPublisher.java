package com.pig4cloud.pigx.flow.event;

import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
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

	public static final String REGISTERED_FLOW_IDS_KEY = CacheConstants.GLOBALLY + "flow:registered-flow-ids";

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

	/**
	 * 注册 flowId 到 Redis Set
	 * @param flowId 流程定义Key
	 */
	public void registerFlowId(String flowId) {
		redisTemplate.opsForSet().add(REGISTERED_FLOW_IDS_KEY, flowId);
		log.debug("注册 flowId: {}", flowId);
	}

	/**
	 * 校验 flowId 是否已注册
	 * @param flowId 流程定义Key
	 * @return true 表示已注册
	 */
	public boolean isFlowIdRegistered(String flowId) {
		return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(REGISTERED_FLOW_IDS_KEY, flowId));
	}

}
