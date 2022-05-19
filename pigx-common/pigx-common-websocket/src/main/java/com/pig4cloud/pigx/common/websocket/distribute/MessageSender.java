package com.pig4cloud.pigx.common.websocket.distribute;

import cn.hutool.core.collection.CollectionUtil;
import com.pig4cloud.pigx.common.websocket.config.WebSocketMessageSender;

import java.util.List;

/**
 * @author Hccake 2021/1/12
 * @version 1.0
 */
public interface MessageSender {

	/**
	 * 发送消息
	 * @param messageDO 发送的消息
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
