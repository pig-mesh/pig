package com.pig4cloud.pigx.common.websocket;

import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.common.websocket.message.JsonWebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author Hccake 2021/1/4
 * @version 1.0
 */
@Slf4j
public final class WebSocketMessageSender {

	private WebSocketMessageSender() {
	}

	public static void send(WebSocketSession session, JsonWebSocketMessage message) {
		send(session, JSONUtil.toJsonStr(message));
	}

	public static boolean send(WebSocketSession session, String message) {
		if (session == null) {
			log.error("[send] session 为 null");
			return false;
		}
		if (!session.isOpen()) {
			log.error("[send] session 已经关闭");
			return false;
		}
		try {
			session.sendMessage(new TextMessage(message));
		}
		catch (IOException e) {
			log.error("[send] session({}) 发送消息({}) 异常", session, message, e);
			return false;
		}
		return true;
	}

}
