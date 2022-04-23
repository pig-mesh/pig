package com.pig4cloud.pigx.common.websocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.websocket.exception.ErrorJsonMessageException;
import com.pig4cloud.pigx.common.websocket.holder.JsonMessageHandlerHolder;
import com.pig4cloud.pigx.common.websocket.message.JsonWebSocketMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * @author Hccake 2020/12/31
 * @version 1.0
 */
@Slf4j
public class CustomWebSocketHandler extends TextWebSocketHandler {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	static {
		// 有特殊需要转义字符, 不报错
		MAPPER.enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature());
	}

	private PlanTextMessageHandler planTextMessageHandler;

	public CustomWebSocketHandler() {
	}

	public CustomWebSocketHandler(PlanTextMessageHandler planTextMessageHandler) {
		this.planTextMessageHandler = planTextMessageHandler;
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		// 空消息不处理
		if (message.getPayloadLength() == 0) {
			return;
		}

		// 获取消息载荷
		String payload = message.getPayload();
		try {
			// 尝试使用 json 处理
			handleWithJson(session, payload);
		}
		catch (ErrorJsonMessageException ex) {
			log.debug("消息载荷 [{}] 回退使用 PlanTextMessageHandler，原因：{}", payload, ex.getMessage());
			// fallback 使用普通文本处理
			if (planTextMessageHandler != null) {
				planTextMessageHandler.handle(session, payload);
			}
			else {
				log.error("[handleTextMessage] 普通文本消息（{}）没有对应的消息处理器", payload);
			}
		}

	}

	private void handleWithJson(WebSocketSession session, String payload) {
		// 消息类型必有一属性type，先解析，获取该属性
		JsonNode jsonNode = null;
		try {
			jsonNode = MAPPER.readTree(payload);
		}
		catch (JsonProcessingException e) {
			throw new ErrorJsonMessageException("json 解析异常");
		}

		// 必须是 object 类型
		if (!jsonNode.isObject()) {
			throw new ErrorJsonMessageException("json 格式异常！非 object 类型！");
		}

		JsonNode typeNode = jsonNode.get(JsonWebSocketMessage.TYPE_FIELD);
		String messageType = typeNode.asText();
		if (messageType == null) {
			throw new ErrorJsonMessageException("json 无 type 属性");
		}

		// 获得对应的消息处理器
		JsonMessageHandler<JsonWebSocketMessage> jsonMessageHandler = JsonMessageHandlerHolder.getHandler(messageType);
		if (jsonMessageHandler == null) {
			log.error("[handleTextMessage] 消息类型（{}）不存在对应的消息处理器", messageType);
			return;
		}
		// 消息处理
		Class<? extends JsonWebSocketMessage> messageClass = jsonMessageHandler.getMessageClass();
		JsonWebSocketMessage websocketMessageJson;
		try {
			websocketMessageJson = MAPPER.treeToValue(jsonNode, messageClass);
		}
		catch (JsonProcessingException e) {
			throw new ErrorJsonMessageException("消息序列化异常，class " + messageClass);
		}
		jsonMessageHandler.handle(session, websocketMessageJson);
	}

}
