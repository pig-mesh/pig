package com.pig4cloud.pigx.common.websocket.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Hccake 2021/1/4
 * @version 1.0
 */
@Getter
@RequiredArgsConstructor
public enum WebSocketMessageTypeEnum {

	PING("ping"), PONG("pong");

	private final String value;

}
