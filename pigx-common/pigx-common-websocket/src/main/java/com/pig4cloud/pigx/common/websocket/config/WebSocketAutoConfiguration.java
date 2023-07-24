package com.pig4cloud.pigx.common.websocket.config;

import com.pig4cloud.pigx.common.websocket.handler.JsonMessageHandler;
import com.pig4cloud.pigx.common.websocket.holder.JsonMessageHandlerHolder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;

/**
 * websocket自动配置
 *
 * @author Yakir
 */
@Import(WebSocketHandlerConfig.class)
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketAutoConfiguration {

	private final WebSocketProperties webSocketProperties;

	private final List<JsonMessageHandler> jsonMessageHandlerList;

	@Bean
	@ConditionalOnMissingBean
	public WebSocketConfigurer webSocketConfigurer(List<HandshakeInterceptor> handshakeInterceptor,
			WebSocketHandler webSocketHandler) {
		return registry -> registry.addHandler(webSocketHandler, webSocketProperties.getPath())
			.setAllowedOrigins(webSocketProperties.getAllowOrigins())
			.addInterceptors(handshakeInterceptor.toArray(new HandshakeInterceptor[0]));
	}

	/**
	 * 初始化时将所有的jsonMessageHandler注册到JsonMessageHandlerHolder中
	 */
	@PostConstruct
	public void initJsonMessageHandlerHolder() {
		for (JsonMessageHandler jsonMessageHandler : jsonMessageHandlerList) {
			JsonMessageHandlerHolder.addHandler(jsonMessageHandler);
		}
	}

}
