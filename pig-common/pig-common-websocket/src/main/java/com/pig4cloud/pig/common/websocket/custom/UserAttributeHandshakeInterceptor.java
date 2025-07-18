package com.pig4cloud.pig.common.websocket.custom;

import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 用户属性握手拦截器
 * <p>
 * 在 WebSocket 握手阶段，将当前通过 Spring Security 认证的用户信息存入 WebSocket 会话属性中， 以便后续在 WebSocket
 * 处理流程中访问用户信息。
 * </p>
 *
 * @author lengleng
 * @date 2021/10/4
 */
public class UserAttributeHandshakeInterceptor implements HandshakeInterceptor {

	/**
	 * 在 WebSocket 握手前调用，用于将用户信息添加到会话属性中。
	 * <p>
	 * 由于 WebSocket 握手是基于 HTTP 的，此时可以通过 {@link SecurityUtils} 获取已认证的用户信息。
	 * </p>
	 * @param request 当前的服务器请求。
	 * @param response 当前的服务器响应。
	 * @param wsHandler 目标 WebSocket 处理器。
	 * @param attributes 用于存储 WebSocket 会话属性的映射。
	 * @return 始终返回 {@code true}，允许握手继续。
	 * @throws Exception 如果在处理过程中发生错误。
	 */
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		// 由于 WebSocket 握手是由 http 升级的，携带 token 已经被 Security 拦截验证了，所以可以直接获取到用户
		PigUser user = SecurityUtils.getUser();
		attributes.put("USER_KEY_ATTR_NAME", user);
		return true;
	}

	/**
	 * 在 WebSocket 握手完成后调用。
	 * <p>
	 * 此方法在此实现中为空，没有执行任何操作。
	 * </p>
	 * @param request 当前的服务器请求。
	 * @param response 当前的服务器响应。
	 * @param wsHandler 目标 WebSocket 处理器。
	 * @param exception 握手过程中抛出的异常，如果没有异常则为 {@code null}。
	 */
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {

	}

}
