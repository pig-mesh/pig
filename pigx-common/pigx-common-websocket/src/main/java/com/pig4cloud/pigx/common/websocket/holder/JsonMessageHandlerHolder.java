package com.pig4cloud.pigx.common.websocket.holder;

import com.pig4cloud.pigx.common.websocket.handler.JsonMessageHandler;
import com.pig4cloud.pigx.common.websocket.message.JsonWebSocketMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public final class JsonMessageHandlerHolder {

	private JsonMessageHandlerHolder() {
	}

	private static final Map<String, JsonMessageHandler<JsonWebSocketMessage>> MESSAGE_HANDLER_MAP = new ConcurrentHashMap<>();

	public static JsonMessageHandler<JsonWebSocketMessage> getHandler(String type) {
		return MESSAGE_HANDLER_MAP.get(type);
	}

	public static void addHandler(JsonMessageHandler<JsonWebSocketMessage> jsonMessageHandler) {
		MESSAGE_HANDLER_MAP.put(jsonMessageHandler.type(), jsonMessageHandler);
	}

}
