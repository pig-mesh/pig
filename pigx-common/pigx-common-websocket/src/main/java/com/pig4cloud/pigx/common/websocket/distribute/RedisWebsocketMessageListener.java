package com.pig4cloud.pigx.common.websocket.distribute;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * redis订阅 websocket 发送消息，接收到消息时进行推送
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class RedisWebsocketMessageListener implements MessageListener, MessageSender {

	public static final String CHANNEL = "websocket-send";

	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public void onMessage(Message message, byte[] bytes) {
		log.info("redis channel Listener message send {}", message);
		byte[] channelBytes = message.getChannel();
		RedisSerializer<String> stringSerializer = stringRedisTemplate.getStringSerializer();
		String channel = stringSerializer.deserialize(channelBytes);

		// 这里没有使用通配符，所以一定是true
		if (CHANNEL.equals(channel)) {
			byte[] bodyBytes = message.getBody();
			String body = stringSerializer.deserialize(bodyBytes);
			MessageDO messageDO = JSONUtil.toBean(body, MessageDO.class);
			doSend(messageDO);
		}
	}

}
