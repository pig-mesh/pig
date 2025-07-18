package com.pig4cloud.pig.common.websocket.custom;

import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.websocket.holder.SessionKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;

/**
 * WebSocket Session 标识生成器
 * <p>
 * 默认的 WebSocket 会话密钥生成器实现，用于根据用户信息生成唯一的会话标识。
 * </p>
 *
 * @author lengleng
 * @date 2021/10/4
 */
@Configuration
@RequiredArgsConstructor
public class PigxSessionKeyGenerator implements SessionKeyGenerator {

	/**
	 * 根据 WebSocket 会话中的用户信息生成会话的唯一标识。
	 * <p>
	 * 此实现从会话属性中获取 {@link PigUser} 对象，并使用其 ID 作为唯一标识。
	 * </p>
	 * @param webSocketSession 当前的 WebSocket 会话。
	 * @return 返回会话的唯一标识，如果无法确定用户，则返回 {@code null}。
	 */
	@Override
	public Object sessionKey(WebSocketSession webSocketSession) {

		Object obj = webSocketSession.getAttributes().get("USER_KEY_ATTR_NAME");

		if (obj instanceof PigUser user) {
			// userId 作为唯一区分
			return String.valueOf(user.getId());
		}

		return null;
	}

}
