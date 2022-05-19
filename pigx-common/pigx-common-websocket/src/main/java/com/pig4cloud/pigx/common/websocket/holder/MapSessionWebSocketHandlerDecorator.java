package com.pig4cloud.pigx.common.websocket.holder;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * WebSocketHandler 装饰器，该装饰器主要用于在开启和关闭连接时，进行session的映射存储与释放
 *
 * @author Hccake 2020/12/31
 * @version 1.0
 */
public class MapSessionWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

	private final SessionKeyGenerator sessionKeyGenerator;

	public MapSessionWebSocketHandlerDecorator(WebSocketHandler delegate, SessionKeyGenerator sessionKeyGenerator) {
		super(delegate);
		this.sessionKeyGenerator = sessionKeyGenerator;
	}

	/**
	 * websocket 连接时执行的动作
	 * @param session websocket session 对象
	 * @throws Exception 异常对象
	 */
	@Override
	public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
		Object sessionKey = sessionKeyGenerator.sessionKey(session);
		WebSocketSessionHolder.addSession(sessionKey, session);
	}

	/**
	 * websocket 关闭连接时执行的动作
	 * @param session websocket session 对象
	 * @param closeStatus 关闭状态对象
	 * @throws Exception 异常对象
	 */
	@Override
	public void afterConnectionClosed(final WebSocketSession session, CloseStatus closeStatus) throws Exception {
		Object sessionKey = sessionKeyGenerator.sessionKey(session);
		WebSocketSessionHolder.removeSession(sessionKey);
	}

}
