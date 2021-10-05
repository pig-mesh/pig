package com.pig4cloud.pigx.common.websocket.message;

/**
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public abstract class AbstractJsonWebSocketMessage implements JsonWebSocketMessage {

	public static final String TYPE_FIELD = "type";

	private final String type;

	protected AbstractJsonWebSocketMessage(String type) {
		this.type = type;
	}

	@Override
	public String getType() {
		return type;
	}

}
