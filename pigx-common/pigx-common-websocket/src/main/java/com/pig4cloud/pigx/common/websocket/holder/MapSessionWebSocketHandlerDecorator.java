package com.pig4cloud.pigx.common.websocket.holder;

import com.pig4cloud.pigx.common.websocket.config.WebSocketProperties;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

/**
 * WebSocketHandler 装饰器，该装饰器主要用于在开启和关闭连接时，进行session的映射存储与释放
 *
 * @author Hccake 2020/12/31
 * @version 1.0
 */
public class MapSessionWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

    private final SessionKeyGenerator sessionKeyGenerator;

    private final WebSocketProperties webSocketProperties;

    public MapSessionWebSocketHandlerDecorator(WebSocketHandler delegate, SessionKeyGenerator sessionKeyGenerator, WebSocketProperties webSocketProperties) {
        super(delegate);
        this.sessionKeyGenerator = sessionKeyGenerator;
        this.webSocketProperties = webSocketProperties;
    }

    /**
     * websocket 连接时执行的动作
     *
     * @param session websocket session 对象
     *                ConcurrentWebSocketSessionDecorator确保一次只有一个线程可以发送消息。
     *                如果某个线程的发送操作较慢，其他线程尝试发送消息时将无法获取锁，消息将被缓冲。
     *                此时，装饰器会检查指定的缓冲区大小限制和发送时间限制，如果超过这些限制，会关闭会话
     * @throws Exception 异常对象
     */
    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        Object sessionKey = sessionKeyGenerator.sessionKey(session);
        WebSocketSessionHolder.addSession(sessionKey
                , new ConcurrentWebSocketSessionDecorator(session, webSocketProperties.getSendTimeLimit(), webSocketProperties.getSendBufferSizeLimit()));
    }

    /**
     * websocket 关闭连接时执行的动作
     *
     * @param session     websocket session 对象
     * @param closeStatus 关闭状态对象
     * @throws Exception 异常对象
     */
    @Override
    public void afterConnectionClosed(final WebSocketSession session, CloseStatus closeStatus) throws Exception {
        Object sessionKey = sessionKeyGenerator.sessionKey(session);
        WebSocketSessionHolder.removeSession(sessionKey);
    }

}
