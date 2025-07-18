package com.pig4cloud.pig.common.websocket.message;

/**
 * JSON WebSocket 消息接口
 * <p>
 * 定义了所有 JSON 格式 WebSocket 消息应遵循的契约， 强制实现类提供一个消息类型标识符。
 * </p>
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public interface JsonWebSocketMessage {

	/**
	 * 获取消息类型。
	 * <p>
	 * 消息类型主要用于匹配对应的消息处理器，实现消息的路由和分发。
	 * </p>
	 * @return 当前消息的类型字符串。
	 */
	String getType();

}
