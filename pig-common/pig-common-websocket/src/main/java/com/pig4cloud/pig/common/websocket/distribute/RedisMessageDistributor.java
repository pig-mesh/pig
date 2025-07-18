package com.pig4cloud.pig.common.websocket.distribute;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于 Redis 的消息分发器
 * <p>
 * 在集群环境下，通过 Redis 的发布/订阅机制，将消息发布到指定频道， 由所有订阅该频道的服务实例进行消费和处理，从而实现跨服务的消息分发。
 * </p>
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
@RequiredArgsConstructor
public class RedisMessageDistributor implements MessageDistributor {

	private final StringRedisTemplate stringRedisTemplate;

	/**
	 * 将消息发布到 Redis 频道。
	 * <p>
	 * 此方法首先将消息对象序列化为 JSON 字符串，然后通过 Redis 发布/订阅机制 将其发送到
	 * {@link RedisWebsocketMessageListener#CHANNEL} 频道。
	 * </p>
	 * @param messageDO 待发送的消息对象，包含消息内容和目标会话信息。
	 */
	@Override
	public void distribute(MessageDO messageDO) {
		// 包装 sessionKey 适配分布式多环境
		List<Object> sessionKeyList = new ArrayList<>(messageDO.getSessionKeys());
		messageDO.setSessionKeys(sessionKeyList);

		String str = JSONUtil.toJsonStr(messageDO);
		stringRedisTemplate.convertAndSend(RedisWebsocketMessageListener.CHANNEL, str);
	}

}
