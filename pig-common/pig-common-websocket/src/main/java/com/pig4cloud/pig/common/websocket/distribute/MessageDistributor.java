package com.pig4cloud.pig.common.websocket.distribute;

/**
 * 消息分发器接口
 * <p>
 * 定义了消息分发的契约，负责将 WebSocket 消息路由到适当的目标， 支持本地内存分发和基于消息中间件（如 Redis）的分布式分发。
 * </p>
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
public interface MessageDistributor {

	/**
	 * 分发消息。
	 * <p>
	 * 根据实现类的不同，此方法可以将消息发送到本地会话或发布到消息队列中， 以便在集群环境中进行广播或单点发送。
	 * </p>
	 * @param messageDO 待发送的消息对象，包含消息内容和目标会话信息。
	 */
	void distribute(MessageDO messageDO);

}
