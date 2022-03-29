package com.pig4cloud.pigx.common.websocket.distribute;

import com.pig4cloud.pigx.common.websocket.session.WebSocketSessionStore;

/**
 * 本地消息分发，直接进行发送
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
public class LocalMessageDistributor extends AbstractMessageDistributor {

	public LocalMessageDistributor(WebSocketSessionStore webSocketSessionStore) {
		super(webSocketSessionStore);
	}

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	@Override
	public void distribute(MessageDO messageDO) {
		doSend(messageDO);
	}

}
