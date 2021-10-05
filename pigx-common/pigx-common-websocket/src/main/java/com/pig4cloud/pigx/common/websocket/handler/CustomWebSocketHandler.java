package com.pig4cloud.pigx.common.websocket.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.websocket.holder.JsonMessageHandlerHolder;
import com.pig4cloud.pigx.common.websocket.message.AbstractJsonWebSocketMessage;
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
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
		// 空消息不处理
		if (message.getPayloadLength() == 0) {
			return;
		}

		// 消息类型必有一属性type，先解析，获取该属性
		String payload = message.getPayload();
		JsonNode jsonNode = MAPPER.readTree(payload);
		JsonNode typeNode = jsonNode.get(AbstractJsonWebSocketMessage.TYPE_FIELD);

		if (typeNode == null) {
			if (planTextMessageHandler != null) {
				planTextMessageHandler.handle(session, payload);
			}
			else {
				log.error("[handleTextMessage] 普通文本消息（{}）没有对应的消息处理器", payload);
			}
		}
		else {
			String messageType = typeNode.asText();
			// 获得对应的消息处理器
			JsonMessageHandler jsonMessageHandler = JsonMessageHandlerHolder.getHandler(messageType);
			if (jsonMessageHandler == null) {
				log.error("[handleTextMessage] 消息类型（{}）不存在对应的消息处理器", messageType);
				return;
			}
			// 消息处理
			Class<? extends JsonWebSocketMessage> messageClass = jsonMessageHandler.getMessageClass();
			JsonWebSocketMessage websocketMessageJson = MAPPER.treeToValue(jsonNode, messageClass);
			jsonMessageHandler.handle(session, websocketMessageJson);
		}
	}

}
