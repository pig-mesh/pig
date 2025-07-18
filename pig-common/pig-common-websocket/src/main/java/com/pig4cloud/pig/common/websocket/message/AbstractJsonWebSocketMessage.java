package com.pig4cloud.pig.common.websocket.message;

/**
 * 抽象的 JSON WebSocket 消息基类
 * <p>
 * 提供了 JSON 格式 WebSocket 消息的通用结构，包含一个类型字段 {@code type}， 用于标识消息的具体类型，以便于消息分发和处理。
 * </p>
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public abstract class AbstractJsonWebSocketMessage implements JsonWebSocketMessage {

	/**
	 * 消息类型字段的名称。
	 */
	public static final String TYPE_FIELD = "type";

	private final String type;

	/**
	 * 构造函数，用于创建抽象 JSON WebSocket 消息实例。
	 * @param type 消息的类型标识符。
	 */
	protected AbstractJsonWebSocketMessage(String type) {
		this.type = type;
	}

	/**
	 * 获取消息的类型。
	 * @return 消息类型字符串。
	 */
	@Override
	public String getType() {
		return type;
	}

}
