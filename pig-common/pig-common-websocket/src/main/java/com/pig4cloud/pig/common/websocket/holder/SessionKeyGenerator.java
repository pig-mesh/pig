package com.pig4cloud.pig.common.websocket.holder;

import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocketSession 唯一标识生成器接口
 * <p>
 * 定义了生成 WebSocket 会话唯一标识的契约。 不同的实现可以根据业务需求（如用户ID、会话ID等）生成唯一的会话标识。
 * </p>
 *
 * @author Hccake 2021/1/5
 * @version 1.0
 */
public interface SessionKeyGenerator {

	/**
	 * 获取当前 WebSocket 会话的唯一标识。
	 * @param webSocketSession 当前的 WebSocket 会话对象。
	 * @return 返回会话的唯一标识对象。
	 */
	Object sessionKey(WebSocketSession webSocketSession);

}
