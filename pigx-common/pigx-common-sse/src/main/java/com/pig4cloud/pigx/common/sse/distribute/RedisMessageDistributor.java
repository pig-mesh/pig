package com.pig4cloud.pigx.common.sse.distribute;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息分发器
 *
 * @author lengleng 2021/1/12
 * @version 1.0
 */
@RequiredArgsConstructor
public class RedisMessageDistributor implements MessageDistributor {

	private final StringRedisTemplate stringRedisTemplate;

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	@Override
	public void distribute(MessageDO messageDO) {
		
		String str = JSONUtil.toJsonStr(messageDO);
		stringRedisTemplate.convertAndSend(RedisSseEmitterMessageListener.CHANNEL, str);
	}

}
