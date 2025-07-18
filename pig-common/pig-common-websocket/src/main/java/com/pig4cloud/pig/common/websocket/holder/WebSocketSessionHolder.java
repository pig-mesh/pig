package com.pig4cloud.pig.common.websocket.holder;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocketSession 持有者
 * <p>
 * 负责管理和存储所有在线的 WebSocket 会话信息。 提供添加、删除、获取会话以及获取所有会话和会话键的方法。
 * </p>
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public final class WebSocketSessionHolder {

	private WebSocketSessionHolder() {
	}

	private static final Map<String, WebSocketSession> USER_SESSION_MAP = new ConcurrentHashMap<>();

	/**
	 * 添加一个 WebSocket 会话。
	 * @param sessionKey 会话的唯一标识。
	 * @param session 待添加的 WebSocketSession 对象。
	 */
	public static void addSession(Object sessionKey, WebSocketSession session) {
		USER_SESSION_MAP.put(sessionKey.toString(), session);
	}

	/**
	 * 删除一个 WebSocket 会话。
	 * @param sessionKey 会话的唯一标识。
	 */
	public static void removeSession(Object sessionKey) {
		USER_SESSION_MAP.remove(sessionKey.toString());
	}

	/**
	 * 获取指定标识的 WebSocket 会话。
	 * @param sessionKey 会话的唯一标识。
	 * @return 对应的 WebSocketSession 对象，如果不存在则返回 {@code null}。
	 */
	public static WebSocketSession getSession(Object sessionKey) {
		return USER_SESSION_MAP.get(sessionKey.toString());
	}

	/**
	 * 获取当前所有在线的 WebSocket 会话集合。
	 * @return 所有 WebSocketSession 对象的集合。
	 */
	public static Collection<WebSocketSession> getSessions() {
		return USER_SESSION_MAP.values();
	}

	/**
	 * 获取所有在线 WebSocket 会话的唯一标识集合。
	 * @return 所有会话唯一标识（字符串形式）的集合。
	 */
	public static Set<String> getSessionKeys() {
		return USER_SESSION_MAP.keySet();
	}

}
