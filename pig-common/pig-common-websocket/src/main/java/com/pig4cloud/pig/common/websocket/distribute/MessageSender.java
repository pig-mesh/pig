package com.pig4cloud.pig.common.websocket.distribute;

import cn.hutool.core.collection.CollectionUtil;
import com.pig4cloud.pig.common.websocket.config.WebSocketMessageSender;

import java.util.List;

/**
 * 消息发送者接口
 * <p>
 * 提供了一个默认方法 {@code doSend}，用于实际执行 WebSocket 消息的发送操作。 此接口可由不同的消息分发策略实现，以统一发送逻辑。
 * </p>
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
public interface MessageSender {

	/**
	 * 执行消息发送。
	 * <p>
	 * 此方法会检查消息是否为广播，如果是，则向所有会话发送； 否则，向指定列表的会话发送。
	 * </p>
	 * @param messageDO 待发送的消息对象，包含消息内容和目标会话信息。
	 */
	default void doSend(MessageDO messageDO) {
		Boolean needBroadcast = messageDO.getNeedBroadcast();
		String messageText = messageDO.getMessageText();
		List<Object> sessionKeys = messageDO.getSessionKeys();
		if (needBroadcast != null && needBroadcast) {
			// 广播信息
			WebSocketMessageSender.broadcast(messageText);
		}
		else if (CollectionUtil.isNotEmpty(sessionKeys)) {
			// 指定用户发送
			for (Object sessionKey : sessionKeys) {
				WebSocketMessageSender.send(sessionKey, messageText);
			}
		}
	}

}
