package com.pig4cloud.pig.common.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

/**
 * 默认的纯文本消息处理器
 * <p>
 * 当 WebSocket 消息不符合预定义的 JSON 格式时，此处理器将作为默认实现， 仅记录接收到的消息内容和会话 ID，不执行其他业务逻辑。
 * </p>
 *
 * @author lengleng
 * @date 2021/11/4
 */
@Slf4j
public class CustomPlanTextMessageHandler implements PlanTextMessageHandler {

	/**
	 * 处理纯文本 WebSocket 消息。
	 * <p>
	 * 默认实现仅将接收到的消息内容和会话 ID 记录到日志中。
	 * </p>
	 * @param session 当前接收消息的 WebSocket 会话。
	 * @param message 接收到的文本消息。
	 */
	@Override
	public void handle(WebSocketSession session, String message) {
		log.info("sessionId {} ,msg {}", session.getId(), message);
	}

}
