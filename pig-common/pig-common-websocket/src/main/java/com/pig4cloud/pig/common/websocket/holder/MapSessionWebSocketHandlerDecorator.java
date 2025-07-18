package com.pig4cloud.pig.common.websocket.holder;

import com.pig4cloud.pig.common.websocket.config.WebSocketProperties;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * WebSocketHandler 装饰器
 * <p>
 * 此装饰器主要用于在 WebSocket 连接建立和关闭时，对会话进行映射存储和释放。 它确保了会话的生命周期管理，并支持并发消息发送的控制。
 * </p>
 *
 * @author Hccake 2020/12/31
 * @version 1.0
 */
public class MapSessionWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

	private final SessionKeyGenerator sessionKeyGenerator;

	private final WebSocketProperties webSocketProperties;

	/**
	 * 构造一个新的 {@code MapSessionWebSocketHandlerDecorator} 实例。
	 * @param delegate 被装饰的原始 {@link WebSocketHandler}。
	 * @param sessionKeyGenerator 会话密钥生成器，用于生成会话的唯一标识。
	 * @param webSocketProperties WebSocket 配置属性，包含发送时间限制和缓冲区大小限制。
	 */
	public MapSessionWebSocketHandlerDecorator(WebSocketHandler delegate, SessionKeyGenerator sessionKeyGenerator,
			WebSocketProperties webSocketProperties) {
		super(delegate);
		this.sessionKeyGenerator = sessionKeyGenerator;
		this.webSocketProperties = webSocketProperties;
	}

	/**
	 * 在 WebSocket 连接建立后执行的动作。
	 * <p>
	 * 此方法会生成会话的唯一标识，并将带有并发控制的 {@link WebSocketSession} 存储到 {@link WebSocketSessionHolder}
	 * 中。 {@link ConcurrentWebSocketSessionDecorator}
	 * 确保一次只有一个线程可以发送消息，并根据配置的缓冲区大小和发送时间限制管理会话。
	 * </p>
	 * @param session 建立的 WebSocket 会话对象。
	 * @throws Exception 如果在处理过程中发生错误。
	 */
	@Override
	public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
		Object sessionKey = sessionKeyGenerator.sessionKey(session);
		WebSocketSessionHolder.addSession(sessionKey, new ConcurrentWebSocketSessionDecorator(session,
				webSocketProperties.getSendTimeLimit(), webSocketProperties.getSendBufferSizeLimit()));
	}

	/**
	 * 在 WebSocket 连接关闭后执行的动作。
	 * <p>
	 * 此方法会根据会话的唯一标识从 {@link WebSocketSessionHolder} 中移除对应的会话。
	 * </p>
	 * @param session 关闭的 WebSocket 会话对象。
	 * @param closeStatus 关闭状态对象，包含关闭原因和状态码。
	 * @throws Exception 如果在处理过程中发生错误。
	 */
	@Override
	public void afterConnectionClosed(final WebSocketSession session, CloseStatus closeStatus) throws Exception {
		Object sessionKey = sessionKeyGenerator.sessionKey(session);
		WebSocketSessionHolder.removeSession(sessionKey);
	}

}
