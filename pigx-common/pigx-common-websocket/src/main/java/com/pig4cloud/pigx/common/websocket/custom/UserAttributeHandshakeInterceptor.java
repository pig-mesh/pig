package com.pig4cloud.pigx.common.websocket.custom;

import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author lengleng
 * @date 2021/10/4
 */
public class UserAttributeHandshakeInterceptor implements HandshakeInterceptor {

	/**
	 * Invoked before the handshake is processed.
	 * @param request the current request
	 * @param response the current response
	 * @param wsHandler the target WebSocket handler
	 * @param attributes the attributes from the HTTP handshake to associate with the
	 * WebSocket session; the provided attributes are copied, the original map is not
	 * used.
	 * @return whether to proceed with the handshake ({@code true}) or abort
	 * ({@code false})
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		// 由于 WebSocket 握手是由 http 升级的，携带 token 已经被 Security 拦截验证了，所以可以直接获取到用户
		PigxUser user = SecurityUtils.getUser();
		attributes.put("USER_KEY_ATTR_NAME", user);
		return true;
	}

	/**
	 * Invoked after the handshake is done. The response status and headers indicate the
	 * results of the handshake, i.e. whether it was successful or not.
	 * @param request the current request
	 * @param response the current response
	 * @param wsHandler the target WebSocket handler
	 * @param exception an exception raised during the handshake, or {@code null} if none
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {

	}

}
