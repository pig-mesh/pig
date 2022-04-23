package com.pig4cloud.pigx.common.websocket.message;

/**
 * @author Hccake 2021/1/4
 * @version 1.0
 */
@SuppressWarnings("AlibabaAbstractClassShouldStartWithAbstractNaming")
public abstract class JsonWebSocketMessage {

	public static final String TYPE_FIELD = "type";

	private final String type;

	protected JsonWebSocketMessage(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}

