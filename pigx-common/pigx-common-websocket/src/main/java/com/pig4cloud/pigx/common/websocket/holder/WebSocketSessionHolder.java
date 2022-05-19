package com.pig4cloud.pigx.common.websocket.holder;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketSession 持有者 主要用于保存当前所有在线的会话信息
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public final class WebSocketSessionHolder {

	private WebSocketSessionHolder() {
	}

	private static final Map<String, WebSocketSession> USER_SESSION_MAP = new ConcurrentHashMap<>();

	/**
	 * 添加一个 session
	 * @param sessionKey session 唯一标识
	 * @param session 待添加的 WebSocketSession
	 */
	public static void addSession(Object sessionKey, WebSocketSession session) {
		USER_SESSION_MAP.put(sessionKey.toString(), session);
	}

	/**
	 * 删除一个 session
	 * @param sessionKey session唯一标识
	 */
	public static void removeSession(Object sessionKey) {
		USER_SESSION_MAP.remove(sessionKey.toString());
	}

	/**
	 * 获取指定标识的 session
	 * @param sessionKey session唯一标识
	 * @return WebSocketSession 该标识对应的 session
	 */
	public static WebSocketSession getSession(Object sessionKey) {
		return USER_SESSION_MAP.get(sessionKey.toString());
	}

	/**
	 * 获取当前所有在线的 session
	 * @return Collection<WebSocketSession> session集合
	 */
	public static Collection<WebSocketSession> getSessions() {
		return USER_SESSION_MAP.values();
	}

	/**
	 * 获取所有在线的用户标识
	 * @return Set<Object> session唯一标识集合
	 */
	public static Set<String> getSessionKeys() {
		return USER_SESSION_MAP.keySet();
	}

}
