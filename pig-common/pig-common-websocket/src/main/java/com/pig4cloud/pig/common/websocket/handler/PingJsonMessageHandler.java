package com.pig4cloud.pig.common.websocket.handler;

import com.pig4cloud.pig.common.websocket.config.WebSocketMessageSender;
import com.pig4cloud.pig.common.websocket.message.JsonWebSocketMessage;
import com.pig4cloud.pig.common.websocket.message.PingJsonWebSocketMessage;
import com.pig4cloud.pig.common.websocket.message.PongJsonWebSocketMessage;
import com.pig4cloud.pig.common.websocket.message.WebSocketMessageTypeEnum;
import org.springframework.web.socket.WebSocketSession;

/**
 * Ping 消息处理器
 * <p>
 * 负责处理客户端发送的 Ping 消息，并立即回复一个 Pong 消息，用于维持 WebSocket 连接的心跳。
 * </p>
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public class PingJsonMessageHandler implements JsonMessageHandler<PingJsonWebSocketMessage> {

	/**
	 * 处理接收到的 Ping 消息。
	 * <p>
	 * 当接收到 Ping 消息时，会构建一个 Pong 消息并通过 {@link WebSocketMessageSender} 发送回客户端。
	 * </p>
	 * @param session 当前的 WebSocket 会话。
	 * @param message 接收到的 Ping 消息对象。
	 */
	@Override
	public void handle(WebSocketSession session, PingJsonWebSocketMessage message) {
		JsonWebSocketMessage pongJsonWebSocketMessage = new PongJsonWebSocketMessage();
		WebSocketMessageSender.send(session, pongJsonWebSocketMessage);
	}

	/**
	 * 获取此处理器处理的消息类型。
	 * @return 返回 {@link WebSocketMessageTypeEnum#PING} 的值。
	 */
	@Override
	public String type() {
		return WebSocketMessageTypeEnum.PING.getValue();
	}

	/**
	 * 获取此处理器对应的消息 Class。
	 * @return 返回 {@link PingJsonWebSocketMessage} 的 Class 对象。
	 */
	@Override
	public Class<PingJsonWebSocketMessage> getMessageClass() {
		return PingJsonWebSocketMessage.class;
	}

}
