package com.pig4cloud.pig.common.sse.config;

/**
 * 消息分发器类型常量类
 *
 * @author lengleng
 * @date 2025/06/06
 */
public final class MessageDistributorTypeConstants {

	private MessageDistributorTypeConstants() {
	}

	/**
	 * 本地
	 */
	public static final String LOCAL = "local";

	/**
	 * 基于 Redis PUB/SUB
	 */
	public static final String REDIS = "redis";

	/**
	 * 自定义
	 */
	public static final String CUSTOM = "custom";

}
