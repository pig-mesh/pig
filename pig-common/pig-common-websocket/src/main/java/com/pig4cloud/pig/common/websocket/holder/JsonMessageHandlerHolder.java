package com.pig4cloud.pig.common.websocket.holder;

import com.pig4cloud.pig.common.websocket.handler.JsonMessageHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JSON 消息处理器持有者
 * <p>
 * 负责管理和存储所有注册的 {@link JsonMessageHandler} 实例。 通过消息类型（{@code type}）可以快速查找对应的处理器。
 * </p>
 *
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public final class JsonMessageHandlerHolder {

	private JsonMessageHandlerHolder() {
	}

	private static final Map<String, JsonMessageHandler> MESSAGE_HANDLER_MAP = new ConcurrentHashMap<>();

	/**
	 * 根据消息类型获取对应的 JSON 消息处理器。
	 * @param type 消息类型字符串。
	 * @return 对应的 {@link JsonMessageHandler} 实例，如果不存在则返回 {@code null}。
	 */
	public static JsonMessageHandler getHandler(String type) {
		return MESSAGE_HANDLER_MAP.get(type);
	}

	/**
	 * 添加一个 JSON 消息处理器到持有者中。
	 * <p>
	 * 处理器会根据其 {@link JsonMessageHandler#type()} 方法返回的类型进行注册。
	 * </p>
	 * @param jsonMessageHandler 待添加的 JSON 消息处理器实例。
	 */
	public static void addHandler(JsonMessageHandler jsonMessageHandler) {
		MESSAGE_HANDLER_MAP.put(jsonMessageHandler.type(), jsonMessageHandler);
	}

}
