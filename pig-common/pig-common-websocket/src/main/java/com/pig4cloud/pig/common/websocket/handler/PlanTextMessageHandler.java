package com.pig4cloud.pig.common.websocket.handler;

import org.springframework.web.socket.WebSocketSession;

/**
 * 纯文本消息处理器接口
 * <p>
 * 定义了处理非 JSON 格式的纯文本 WebSocket 消息的契约。 当接收到的消息不符合预定义的 JSON 消息结构时，将由实现此接口的处理器进行处理。
 * </p>
 *
 * @see com.pig4cloud.pig.common.websocket.message.JsonWebSocketMessage
 * @author Hccake 2021/1/5
 * @version 1.0
 */
public interface PlanTextMessageHandler {

	/**
	 * 处理纯文本 WebSocket 消息。
	 * @param session 当前接收消息的 WebSocket 会话。
	 * @param message 接收到的文本消息内容。
	 */
	void handle(WebSocketSession session, String message);

}
