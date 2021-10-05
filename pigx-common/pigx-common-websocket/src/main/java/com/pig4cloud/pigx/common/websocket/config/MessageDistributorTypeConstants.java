package com.pig4cloud.pigx.common.websocket.config;

/**
 * @author hccake
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
