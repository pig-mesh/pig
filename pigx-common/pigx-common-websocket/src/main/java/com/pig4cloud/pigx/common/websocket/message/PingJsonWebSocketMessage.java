package com.pig4cloud.pigx.common.websocket.message;

/**
 * @author Hccake 2021/1/4
 * @version 1.0
 */
public class PingJsonWebSocketMessage extends JsonWebSocketMessage {

	public PingJsonWebSocketMessage() {
		super(WebSocketMessageTypeEnum.PING.getValue());
	}

}
