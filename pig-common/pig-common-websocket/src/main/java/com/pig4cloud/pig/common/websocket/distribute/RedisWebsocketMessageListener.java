package com.pig4cloud.pig.common.websocket.distribute;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis WebSocket 消息监听器
 * <p>
 * 监听 Redis 的特定频道，接收并处理来自其他服务实例的 WebSocket 消息， 从而实现集群环境下的消息同步和分发。
 * </p>
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
@Slf4j
@RequiredArgsConstructor
public class RedisWebsocketMessageListener implements MessageListener, MessageSender {

	/**
	 * WebSocket 消息在 Redis 发布/订阅中的频道名称。
	 */
	public static final String CHANNEL = "websocket-send";

	private final StringRedisTemplate stringRedisTemplate;

	/**
	 * 当从 Redis 频道接收到消息时调用此方法。
	 * <p>
	 * 此方法会反序列化消息内容，并调用 {@link #doSend(MessageDO)} 方法 将消息发送给当前服务实例中的目标 WebSocket 会话。
	 * </p>
	 * @param message 接收到的 Redis 消息。
	 * @param bytes 订阅的频道模式（未使用）。
	 */
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
