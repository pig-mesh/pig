package com.pig4cloud.pig.common.websocket.config;

import com.pig4cloud.pig.common.websocket.handler.JsonMessageHandler;
import com.pig4cloud.pig.common.websocket.holder.JsonMessageHandlerHolder;
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
 * WebSocket 自动配置类
 * <p>
 * 负责初始化和配置 WebSocket 功能，包括处理器、拦截器和消息分发机制。
 * </p>
 *
 * @author Yakir
 */
@Import(WebSocketHandlerConfig.class)
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketAutoConfiguration {

	private final WebSocketProperties webSocketProperties;

	private final List<JsonMessageHandler> jsonMessageHandlerList;

	/**
	 * 配置 WebSocket 连接处理器。
	 * @param handshakeInterceptor 握手拦截器列表，用于在 WebSocket 握手阶段执行自定义逻辑。
	 * @param webSocketHandler WebSocket 处理器，负责处理 WebSocket 连接和消息。
	 * @return 返回一个 {@link WebSocketConfigurer} 实例，用于注册 WebSocket 处理器和拦截器。
	 */
	@Bean
	@ConditionalOnMissingBean
	public WebSocketConfigurer webSocketConfigurer(List<HandshakeInterceptor> handshakeInterceptor,
			WebSocketHandler webSocketHandler) {
		return registry -> registry.addHandler(webSocketHandler, webSocketProperties.getPath())
			.setAllowedOrigins(webSocketProperties.getAllowOrigins())
			.addInterceptors(handshakeInterceptor.toArray(new HandshakeInterceptor[0]));
	}

	/**
	 * 初始化 JSON 消息处理器持有者。
	 * <p>
	 * 在应用启动时，将所有实现了 {@link JsonMessageHandler} 接口的处理器注册到 {@link JsonMessageHandlerHolder}
	 * 中， 以便后续根据消息类型快速查找和调用。
	 * </p>
	 */
	@PostConstruct
	public void initJsonMessageHandlerHolder() {
		for (JsonMessageHandler jsonMessageHandler : jsonMessageHandlerList) {
			JsonMessageHandlerHolder.addHandler(jsonMessageHandler);
		}
	}

}
