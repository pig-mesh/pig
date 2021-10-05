package com.pig4cloud.pigx.common.websocket.distribute;

/**
 * 消息分发器
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
public interface MessageDistributor {

	/**
	 * 消息分发
	 * @param messageDO 发送的消息
	 */
	void distribute(MessageDO messageDO);

}
