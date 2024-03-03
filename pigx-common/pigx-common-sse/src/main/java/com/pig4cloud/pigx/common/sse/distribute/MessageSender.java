package com.pig4cloud.pigx.common.sse.distribute;

import cn.hutool.core.collection.CollectionUtil;
import com.pig4cloud.pigx.common.sse.config.SseEmitterMessageSender;

import java.util.List;

/**
 * @author lengleng 2021/1/12
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
			SseEmitterMessageSender.broadcast(messageText);
		}
		else if (CollectionUtil.isNotEmpty(sessionKeys)) {
			// 指定用户发送
			for (Object sessionKey : sessionKeys) {
				SseEmitterMessageSender.send(sessionKey, messageText);
			}
		}
	}

}
