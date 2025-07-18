package com.pig4cloud.pig.common.websocket.distribute;

/**
 * 本地消息分发器
 * <p>
 * 在单机环境下，直接将消息发送给本地的 WebSocket 会话，不涉及跨服务通信。
 * </p>
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
public class LocalMessageDistributor implements MessageDistributor, MessageSender {

	/**
	 * 分发消息，对于本地分发器，直接调用发送逻辑。
	 * @param messageDO 待发送的消息对象，包含消息内容和目标会话信息。
	 */
	@Override
	public void distribute(MessageDO messageDO) {
		doSend(messageDO);
	}

}
