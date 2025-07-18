package com.pig4cloud.pig.common.websocket.config;

import com.pig4cloud.pig.common.websocket.custom.PigxSessionKeyGenerator;
import com.pig4cloud.pig.common.websocket.custom.UserAttributeHandshakeInterceptor;
import com.pig4cloud.pig.common.websocket.handler.CustomPlanTextMessageHandler;
import com.pig4cloud.pig.common.websocket.handler.CustomWebSocketHandler;
import com.pig4cloud.pig.common.websocket.handler.PingJsonMessageHandler;
import com.pig4cloud.pig.common.websocket.handler.PlanTextMessageHandler;
import com.pig4cloud.pig.common.websocket.holder.MapSessionWebSocketHandlerDecorator;
import com.pig4cloud.pig.common.websocket.holder.SessionKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * WebSocket 处理器配置类
 * <p>
 * 负责配置和创建 WebSocket 相关的处理器、拦截器和会话管理组件。
 * </p>
 *
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketHandlerConfig {

	private final WebSocketProperties webSocketProperties;

	/**
	 * 创建会话密钥生成器，用于生成 WebSocket 会话的唯一标识。
	 * @return 返回一个 {@link SessionKeyGenerator} 实例。
	 */
	@Bean
	@ConditionalOnMissingBean(SessionKeyGenerator.class)
	public SessionKeyGenerator sessionKeyGenerator() {
		return new PigxSessionKeyGenerator();
	}

	/**
	 * 创建握手拦截器，用于在 WebSocket 握手阶段添加用户属性。
	 * @return 返回一个 {@link HandshakeInterceptor} 实例。
	 */
	@Bean
	public HandshakeInterceptor handshakeInterceptor() {
		return new UserAttributeHandshakeInterceptor();
	}

	/**
	 * 创建默认的纯文本消息处理器。
	 * @return 返回一个 {@link PlanTextMessageHandler} 实例。
	 */
	@Bean
	@ConditionalOnMissingBean(PlanTextMessageHandler.class)
	public PlanTextMessageHandler planTextMessageHandler() {
		return new CustomPlanTextMessageHandler();
	}

	/**
	 * 创建 WebSocket 处理器，当没有纯文本消息处理器时使用。
	 * @param sessionKeyGenerator 会话密钥生成器。
	 * @return 返回一个 {@link WebSocketHandler} 实例。
	 */
	@Bean
	@ConditionalOnMissingBean({ TextWebSocketHandler.class, PlanTextMessageHandler.class })
	public WebSocketHandler webSocketHandler1(@Autowired(required = false) SessionKeyGenerator sessionKeyGenerator) {
		CustomWebSocketHandler customWebSocketHandler = new CustomWebSocketHandler();
		if (webSocketProperties.isMapSession()) {
			return new MapSessionWebSocketHandlerDecorator(customWebSocketHandler, sessionKeyGenerator,
					webSocketProperties);
		}
		return customWebSocketHandler;
	}

	/**
	 * 创建 WebSocket 处理器，当存在纯文本消息处理器时使用。
	 * @param sessionKeyGenerator 会话密钥生成器。
	 * @param planTextMessageHandler 纯文本消息处理器。
	 * @return 返回一个 {@link WebSocketHandler} 实例。
	 */
	@Bean
	@ConditionalOnBean(PlanTextMessageHandler.class)
	@ConditionalOnMissingBean(TextWebSocketHandler.class)
	public WebSocketHandler webSocketHandler2(@Autowired(required = false) SessionKeyGenerator sessionKeyGenerator,
			PlanTextMessageHandler planTextMessageHandler) {
		CustomWebSocketHandler customWebSocketHandler = new CustomWebSocketHandler(planTextMessageHandler);
		if (webSocketProperties.isMapSession()) {
			return new MapSessionWebSocketHandlerDecorator(customWebSocketHandler, sessionKeyGenerator,
					webSocketProperties);
		}
		return customWebSocketHandler;
	}

	/**
	 * 创建 Ping 消息处理器，用于处理心跳检测。
	 * @return 返回一个 {@link PingJsonMessageHandler} 实例。
	 */
	@Bean
	@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "heartbeat", havingValue = "true",
			matchIfMissing = true)
	public PingJsonMessageHandler pingJsonMessageHandler() {
		return new PingJsonMessageHandler();
	}

}
