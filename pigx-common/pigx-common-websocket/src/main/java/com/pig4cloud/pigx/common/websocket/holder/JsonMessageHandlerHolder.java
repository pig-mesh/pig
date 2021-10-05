package com.pig4cloud.pigx.common.websocket.holder;

import com.pig4cloud.pigx.common.websocket.handler.JsonMessageHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public final class JsonMessageHandlerHolder {

	private JsonMessageHandlerHolder() {
	}

	private static final Map<String, JsonMessageHandler> MESSAGE_HANDLER_MAP = new ConcurrentHashMap<>();

	public static JsonMessageHandler getHandler(String type) {
		return MESSAGE_HANDLER_MAP.get(type);
	}

	public static void addHandler(JsonMessageHandler jsonMessageHandler) {
		MESSAGE_HANDLER_MAP.put(jsonMessageHandler.type(), jsonMessageHandler);
	}

}
