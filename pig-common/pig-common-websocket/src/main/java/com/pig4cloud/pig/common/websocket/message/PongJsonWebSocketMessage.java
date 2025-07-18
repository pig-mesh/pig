package com.pig4cloud.pig.common.websocket.message;

/**
 * Pong JSON WebSocket 消息
 * <p>
 * 表示一个 Pong 类型的 WebSocket 消息，通常用于服务器响应客户端的 Ping 请求，作为心跳机制的一部分。
 * </p>
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public class PongJsonWebSocketMessage extends AbstractJsonWebSocketMessage {

	/**
	 * 构造函数，创建一个 Pong 类型的 JSON WebSocket 消息。
	 */
	public PongJsonWebSocketMessage() {
		super(WebSocketMessageTypeEnum.PONG.getValue());
	}

}
