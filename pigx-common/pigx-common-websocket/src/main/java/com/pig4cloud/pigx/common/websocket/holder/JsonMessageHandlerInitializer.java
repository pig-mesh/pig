package com.pig4cloud.pigx.common.websocket.holder;

import com.pig4cloud.pigx.common.websocket.handler.JsonMessageHandler;
import com.pig4cloud.pigx.common.websocket.message.JsonWebSocketMessage;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>
 * JsonMessageHandler 初始化器
 * <p/>
 * 将所有的 jsonMessageHandler 收集到 JsonMessageHandlerHolder 中
 *
 * @author Hccake
 */
@RequiredArgsConstructor
public class JsonMessageHandlerInitializer {

	private final List<JsonMessageHandler<? extends JsonWebSocketMessage>> jsonMessageHandlerList;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void initJsonMessageHandlerHolder() {
		for (JsonMessageHandler<? extends JsonWebSocketMessage> jsonMessageHandler : jsonMessageHandlerList) {
			JsonMessageHandlerHolder.addHandler((JsonMessageHandler<JsonWebSocketMessage>) jsonMessageHandler);
		}
	}

}