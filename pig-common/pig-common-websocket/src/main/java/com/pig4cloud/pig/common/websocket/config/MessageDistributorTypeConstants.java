package com.pig4cloud.pig.common.websocket.config;

/**
 * 消息分发器类型常量
 * <p>
 * 定义了不同消息分发策略的常量标识符。
 * </p>
 *
 * @author hccake
 */
public final class MessageDistributorTypeConstants {

	private MessageDistributorTypeConstants() {
	}

	/**
	 * 本地消息分发
	 * <p>
	 * 适用于单机环境，消息在当前服务实例内部分发。
	 * </p>
	 */
	public static final String LOCAL = "local";

	/**
	 * 基于 Redis PUB/SUB 的消息分发
	 * <p>
	 * 适用于集群环境，通过 Redis 的发布/订阅机制实现跨服务实例的消息分发。
	 * </p>
	 */
	public static final String REDIS = "redis";

	/**
	 * 自定义消息分发
	 * <p>
	 * 用户可以实现自定义的消息分发逻辑。
	 * </p>
	 */
	public static final String CUSTOM = "custom";

}
