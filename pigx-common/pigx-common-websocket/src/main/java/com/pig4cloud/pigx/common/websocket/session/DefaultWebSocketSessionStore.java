package com.pig4cloud.pigx.common.websocket.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 默认的 WebSocketSession 存储器
 *
 * @author hccake
 */
@Slf4j
public class DefaultWebSocketSessionStore implements WebSocketSessionStore {

	private final SessionKeyGenerator sessionKeyGenerator;

	private final ConcurrentHashMap<Object, Map<String, WebSocketSession>> sessionKeyToWsSessions = new ConcurrentHashMap<>();

	public DefaultWebSocketSessionStore(SessionKeyGenerator sessionKeyGenerator) {
		this.sessionKeyGenerator = sessionKeyGenerator;
	}

	/**
	 * 添加一个 wsSession
	 * @param wsSession 待添加的 WebSocketSession
	 */
	@Override
	public void addSession(WebSocketSession wsSession) {
		Object sessionKey = sessionKeyGenerator.sessionKey(wsSession);
		Map<String, WebSocketSession> sessions = this.sessionKeyToWsSessions.get(sessionKey);
		if (sessions == null) {
			sessions = new ConcurrentHashMap<>();
			this.sessionKeyToWsSessions.putIfAbsent(sessionKey, sessions);
			sessions = this.sessionKeyToWsSessions.get(sessionKey);
		}
		sessions.put(wsSession.getId(), wsSession);
	}

	/**
	 * 删除一个 session
	 * @param session WebSocketSession
	 */
	@Override
	public void removeSession(WebSocketSession session) {
		Object sessionKey = sessionKeyGenerator.sessionKey(session);
		String wsSessionId = session.getId();

		Map<String, WebSocketSession> sessions = this.sessionKeyToWsSessions.get(sessionKey);
		if (sessions != null) {
			boolean result = sessions.remove(wsSessionId) != null;
			if (log.isDebugEnabled()) {
				log.debug("Removal of " + wsSessionId + " was " + result);
			}
			if (sessions.isEmpty()) {
				this.sessionKeyToWsSessions.remove(sessionKey);
				if (log.isDebugEnabled()) {
					log.debug("Removed the corresponding HTTP Session for " + wsSessionId
							+ " since it contained no WebSocket mappings");
				}
			}
		}
	}

	/**
	 * 获取当前所有在线的 session
	 * @return Collection<WebSocketSession> session集合
	 */
	@Override
	public Collection<WebSocketSession> getSessions() {
		return sessionKeyToWsSessions.values().stream().flatMap(x -> x.values().stream()).collect(Collectors.toList());
	}

	/**
	 * 根据指定的 sessionKey 获取对应的 wsSessions
	 * @param sessionKey wsSession 标识
	 * @return Collection<WebSocketSession> websocket session集合
	 */
	@Override
	public Collection<WebSocketSession> getSessions(Object sessionKey) {
		return sessionKeyToWsSessions.get(sessionKey).values();
	}

	/**
	 * 获取所有的 sessionKeys
	 * @return sessionKey 集合
	 */
	@Override
	public Collection<Object> getSessionKeys() {
		return sessionKeyToWsSessions.keySet();
	}

}
