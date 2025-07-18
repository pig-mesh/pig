package com.pig4cloud.pig.common.websocket.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * WebSocket 属性配置类
 * <p>
 * 用于配置 WebSocket 的各种行为，如路径、心跳、消息分发等。
 * </p>
 *
 * @author Yakir
 */
@Data
@ConfigurationProperties(WebSocketProperties.PREFIX)
public class WebSocketProperties {

	/**
	 * WebSocket 配置属性的前缀。
	 */
	public static final String PREFIX = "pigx.websocket";

	/**
	 * WebSocket 连接路径。
	 * <p>
	 * 支持路径参数，例如：/ws/{param} 或 /ws/{param1}/{param2}。 同时支持查询参数，例如：/ws?uid=1&name=test。
	 * </p>
	 */
	private String path = "/ws/info";

	/**
	 * 允许的跨域来源，默认为 "*"，表示允许所有来源。
	 */
	private String allowOrigins = "*";

	/**
	 * 是否支持部分消息传输，默认为 {@code false}。
	 */
	private boolean supportPartialMessages = false;

	/**
	 * 是否启用心跳处理，默认为 {@code true}。
	 */
	private boolean heartbeat = true;

	/**
	 * 是否开启会话映射记录，默认为 {@code true}。
	 * <p>
	 * 开启后，会话将与唯一标识符关联，方便进行单点消息发送。
	 * </p>
	 */
	private boolean mapSession = true;

	/**
	 * 消息分发器类型，默认为 "local"。
	 * <p>
	 * 可选值为 "local"（本地内存）或 "redis"（Redis Pub/Sub）。 也可配置为其他值以使用自定义分发器。
	 * </p>
	 */
	private String messageDistributor = MessageDistributorTypeConstants.LOCAL;

	/**
	 * 消息发送时间限制（毫秒），默认为 10000。
	 */
	private Integer sendTimeLimit = 10000;

	/**
	 * 消息发送缓冲区大小限制（字节），默认为 64000。
	 */
	private Integer sendBufferSizeLimit = 64000;

}
