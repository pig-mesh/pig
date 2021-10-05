package com.pig4cloud.pigx.common.websocket.handler;

import com.pig4cloud.pigx.common.websocket.message.JsonWebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public interface JsonMessageHandler<T extends JsonWebSocketMessage> {

	/**
	 * JsonWebSocketMessage 类型消息处理
	 * @param session 当前接收 session
	 * @param message 当前接收到的 message
	 */
	void handle(WebSocketSession session, T message);

	/**
	 * 当前处理器处理的消息类型
	 * @return messageType
	 */
	String type();

	/**
	 * 当前处理器对应的消息Class
	 * @return Class<T>
	 */
	Class<T> getMessageClass();

}
