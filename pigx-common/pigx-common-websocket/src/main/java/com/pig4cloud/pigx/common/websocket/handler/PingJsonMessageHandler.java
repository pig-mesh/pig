package com.pig4cloud.pigx.common.websocket.handler;

import com.pig4cloud.pigx.common.websocket.config.WebSocketMessageSender;
import com.pig4cloud.pigx.common.websocket.message.JsonWebSocketMessage;
import com.pig4cloud.pigx.common.websocket.message.PingJsonWebSocketMessage;
import com.pig4cloud.pigx.common.websocket.message.PongJsonWebSocketMessage;
import com.pig4cloud.pigx.common.websocket.message.WebSocketMessageTypeEnum;
import org.springframework.web.socket.WebSocketSession;

/**
 * 心跳处理，接收到客户端的ping时，立刻回复一个pong
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public class PingJsonMessageHandler implements JsonMessageHandler<PingJsonWebSocketMessage> {

	@Override
	public void handle(WebSocketSession session, PingJsonWebSocketMessage message) {
		JsonWebSocketMessage pongJsonWebSocketMessage = new PongJsonWebSocketMessage();
		WebSocketMessageSender.send(session, pongJsonWebSocketMessage);
	}

	@Override
	public String type() {
		return WebSocketMessageTypeEnum.PING.getValue();
	}

	@Override
	public Class<PingJsonWebSocketMessage> getMessageClass() {
		return PingJsonWebSocketMessage.class;
	}

}
