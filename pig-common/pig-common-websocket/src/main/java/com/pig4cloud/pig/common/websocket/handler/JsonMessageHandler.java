package com.pig4cloud.pig.common.websocket.handler;

import com.pig4cloud.pig.common.websocket.message.JsonWebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * JSON 消息处理器接口
 * <p>
 * 定义了处理特定类型 JSON 格式 WebSocket 消息的契约。 实现此接口的类负责解析和处理对应 {@link JsonWebSocketMessage} 类型的消息。
 * </p>
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public interface JsonMessageHandler<T extends JsonWebSocketMessage> {

	/**
	 * 处理 JSON 格式的 WebSocket 消息。
	 * @param session 当前接收消息的 WebSocket 会话。
	 * @param message 接收到的 JSON 消息对象，其类型由泛型 {@code T} 指定。
	 */
	void handle(WebSocketSession session, T message);

	/**
	 * 获取当前处理器能够处理的消息类型标识。
	 * <p>
	 * 此类型标识用于在 {@link JsonMessageHandlerHolder} 中查找对应的处理器。
	 * </p>
	 * @return 消息类型字符串。
	 */
	String type();

	/**
	 * 获取当前处理器对应的消息类的 Class 对象。
	 * <p>
	 * 此 Class 对象用于 JSON 消息的反序列化。
	 * </p>
	 * @return 消息类的 Class 对象。
	 */
	Class<T> getMessageClass();

}
