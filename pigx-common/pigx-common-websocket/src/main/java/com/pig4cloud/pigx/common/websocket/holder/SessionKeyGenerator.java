package com.pig4cloud.pigx.common.websocket.holder;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketSession 唯一标识生成器
 *
 * @author Hccake 2021/1/5
 * @version 1.0
 */
public interface SessionKeyGenerator {

	/**
	 * 获取当前session的唯一标识
	 * @param webSocketSession 当前session
	 * @return session唯一标识
	 */
	Object sessionKey(WebSocketSession webSocketSession);

}
