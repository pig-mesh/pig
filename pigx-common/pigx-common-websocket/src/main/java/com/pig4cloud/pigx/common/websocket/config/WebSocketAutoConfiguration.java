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
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import java.util.List;

/**
 * WebSocket 自动配置，注册端点、容器参数和消息处理器。
 *
 * @author Yakir
 * @date 2026-05-14
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
     * 配置 Servlet 容器的 WebSocket 消息缓冲区大小。允许通过注册自定义 Bean 覆盖默认配置。
     *
     * @return 配置了文本和二进制消息缓冲区上限的容器工厂 Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public ServletServerContainerFactoryBean servletServerContainerFactoryBean() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(webSocketProperties.getMaxTextMessageBufferSize());
        container.setMaxBinaryMessageBufferSize(webSocketProperties.getMaxBinaryMessageBufferSize());
        return container;
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
