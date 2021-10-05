package com.pig4cloud.pigx.common.websocket.message;

/**
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public interface JsonWebSocketMessage {

	/**
	 * 消息类型，主要用于匹配对应的消息处理器
	 * @return 当前消息类型
	 */
	String getType();

}
