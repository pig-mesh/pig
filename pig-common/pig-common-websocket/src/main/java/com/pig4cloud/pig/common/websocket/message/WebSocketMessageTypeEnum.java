package com.pig4cloud.pig.common.websocket.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * WebSocket 消息类型枚举
 * <p>
 * 定义了 WebSocket 消息的常见类型，例如 Ping 和 Pong，用于心跳机制。
 * </p>
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum WebSocketMessageTypeEnum {

	/**
	 * Ping 消息类型，通常由客户端发送，用于心跳检测。
	 */
	PING("ping"),

	/**
	 * Pong 消息类型，通常由服务器响应 Ping 消息发送，用于心跳检测。
	 */
	PONG("pong");

	private final String value;

}
