package com.pig4cloud.pigx.common.websocket.handler;

import lombok.Data;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

/**
 * 并发使用 WebSocketSession 的相关配置
 *
 * @author hccake
 */
@Data
public class ConcurrentWebSocketSessionOptions {

	/**
	 * 是否在多线程环境下进行发送
	 */
	private boolean enable = false;

	/**
	 * 发送时间的限制（ms）
	 */
	private int sendTimeLimit = 1000 * 5;

	/**
	 * 发送消息缓冲上限 (byte)
	 */
	private int bufferSizeLimit = 1024 * 100;

	/**
	 * 溢出时的执行策略
	 */
	ConcurrentWebSocketSessionDecorator.OverflowStrategy overflowStrategy = ConcurrentWebSocketSessionDecorator.OverflowStrategy.TERMINATE;

}
