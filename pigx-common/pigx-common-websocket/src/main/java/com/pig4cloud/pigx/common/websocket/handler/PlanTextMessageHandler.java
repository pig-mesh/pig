package com.pig4cloud.pigx.common.websocket.handler;

import org.springframework.web.socket.WebSocketSession;

/**
 * 普通文本类型（非指定json类型）的消息处理器 即消息不满足于我们定义的Json类型消息时，所使用的处理器
 *
 * @see com.pig4cloud.pigx.common.websocket.message.JsonWebSocketMessage
 * @author Hccake 2021/1/5
 * @version 1.0
 */
public interface PlanTextMessageHandler {

	/**
	 * 普通文本消息处理
	 * @param session 当前接收消息的session
	 * @param message 文本消息
	 */
	void handle(WebSocketSession session, String message);

}
