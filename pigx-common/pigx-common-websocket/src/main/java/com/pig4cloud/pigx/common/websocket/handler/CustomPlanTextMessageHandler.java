package com.pig4cloud.pigx.common.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author lengleng
 * @date 2021/11/4
 *
 * 默认消息处理
 */
@Slf4j
public class CustomPlanTextMessageHandler implements PlanTextMessageHandler {

	/**
	 * 普通文本消息处理
	 * @param session 当前接收消息的session
	 * @param message 文本消息
	 */
	@Override
	public void handle(WebSocketSession session, String message) {
		log.info("sessionId {} ,msg {}", session.getId(), message);
	}

}
