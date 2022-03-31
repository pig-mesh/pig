package com.pig4cloud.pigx.common.websocket.config;

import com.pig4cloud.pigx.common.websocket.handler.JsonMessageHandler;
import com.pig4cloud.pigx.common.websocket.handler.PingJsonMessageHandler;
import com.pig4cloud.pigx.common.websocket.holder.JsonMessageHandlerInitializer;
import com.pig4cloud.pigx.common.websocket.message.JsonWebSocketMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.SockJsServiceRegistration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;

/**
 * websocket自动配置
 *
 * @author Yakir Hccake
 */
@Import({ WebSocketHandlerConfig.class, LocalMessageDistributorConfig.class, RedisMessageDistributorConfig.class })
@EnableWebSocket
@RequiredArgsConstructor
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketAutoConfiguration {

	private final WebSocketProperties webSocketProperties;

	@Bean
	@ConditionalOnMissingBean
	public WebSocketConfigurer webSocketConfigurer(List<HandshakeInterceptor> handshakeInterceptor,
			WebSocketHandler webSocketHandler,
			@Autowired(required = false) SockJsServiceConfigurer sockJsServiceConfigurer) {
		return registry -> {
			WebSocketHandlerRegistration registration = registry
					.addHandler(webSocketHandler, webSocketProperties.getPath())
					.addInterceptors(handshakeInterceptor.toArray(new HandshakeInterceptor[0]));

			String[] allowedOrigins = webSocketProperties.getAllowedOrigins();
			if (allowedOrigins != null && allowedOrigins.length > 0) {
				registration.setAllowedOrigins(allowedOrigins);
			}

			String[] allowedOriginPatterns = webSocketProperties.getAllowedOriginPatterns();
			if (allowedOriginPatterns != null && allowedOriginPatterns.length > 0) {
				registration.setAllowedOriginPatterns(allowedOriginPatterns);
			}

			if (webSocketProperties.isWithSockjs()) {
				SockJsServiceRegistration sockJsServiceRegistration = registration.withSockJS();
				if (sockJsServiceConfigurer != null) {
					sockJsServiceConfigurer.config(sockJsServiceRegistration);
				}
			}
		};
	}

	/**
	 * 心跳处理器
	 * @return PingJsonMessageHandler
	 */
	@Bean
	@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "heartbeat", havingValue = "true",
			matchIfMissing = true)
	public PingJsonMessageHandler pingJsonMessageHandler() {
		return new PingJsonMessageHandler();
	}

	/**
	 * 注册 JsonMessageHandlerInitializer 收集所有的 json 类型消息处理器
	 * @param jsonMessageHandlerList json 类型消息处理器
	 * @return JsonMessageHandlerInitializer
	 */
	@Bean
	@ConditionalOnMissingBean
	public JsonMessageHandlerInitializer jsonMessageHandlerInitializer(
			List<JsonMessageHandler<? extends JsonWebSocketMessage>> jsonMessageHandlerList) {
		return new JsonMessageHandlerInitializer(jsonMessageHandlerList);
	}

}
