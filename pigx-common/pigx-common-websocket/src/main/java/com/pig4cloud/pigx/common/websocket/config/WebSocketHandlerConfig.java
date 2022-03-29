package com.pig4cloud.pigx.common.websocket.config;

import com.pig4cloud.pigx.common.websocket.handler.CustomWebSocketHandler;
import com.pig4cloud.pigx.common.websocket.handler.PlanTextMessageHandler;
import com.pig4cloud.pigx.common.websocket.session.DefaultWebSocketSessionStore;
import com.pig4cloud.pigx.common.websocket.session.MapSessionWebSocketHandlerDecorator;
import com.pig4cloud.pigx.common.websocket.session.SessionKeyGenerator;
import com.pig4cloud.pigx.common.websocket.session.WebSocketSessionStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;

/**
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@RequiredArgsConstructor
public class WebSocketHandlerConfig {

	private final WebSocketProperties webSocketProperties;

	/**
	 * WebSocket session 存储器
	 * @return DefaultWebSocketSessionStore
	 */
	@Bean
	@ConditionalOnMissingBean
	public WebSocketSessionStore webSocketSessionStore(
			@Autowired(required = false) SessionKeyGenerator sessionKeyGenerator) {
		return new DefaultWebSocketSessionStore(sessionKeyGenerator);
	}

	@Bean
	@ConditionalOnMissingBean(WebSocketHandler.class)
	public WebSocketHandler webSocketHandler(WebSocketSessionStore webSocketSessionStore,
			@Autowired(required = false) PlanTextMessageHandler planTextMessageHandler) {
		CustomWebSocketHandler customWebSocketHandler = new CustomWebSocketHandler(planTextMessageHandler);
		if (webSocketProperties.isMapSession()) {
			return new MapSessionWebSocketHandlerDecorator(customWebSocketHandler, webSocketSessionStore,
					webSocketProperties.getConcurrent());
		}
		return customWebSocketHandler;
	}

}