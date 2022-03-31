package com.pig4cloud.pigx.common.websocket.session;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;

/**
 * @author hccake
 */
public interface WebSocketSessionStore {

	/**
	 * 添加一个 session
	 * @param session 待添加的 WebSocketSession
	 */
	void addSession(WebSocketSession session);

	/**
	 * 删除一个 session
	 * @param session WebSocketSession
	 */
	void removeSession(WebSocketSession session);

	/**
	 * 获取当前所有在线的 wsSessions
	 * @return Collection<WebSocketSession> websocket session集合
	 */
	Collection<WebSocketSession> getSessions();

	/**
	 * 根据指定的 sessionKey 获取对应的 wsSessions
	 * @param sessionKey wsSession 标识
	 * @return Collection<WebSocketSession> websocket session集合
	 */
	Collection<WebSocketSession> getSessions(Object sessionKey);

	/**
	 * 获取所有的 sessionKeys
	 * @return sessionKey 集合
	 */
	Collection<Object> getSessionKeys();

}
