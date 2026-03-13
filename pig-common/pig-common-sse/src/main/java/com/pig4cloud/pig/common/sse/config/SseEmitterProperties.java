package com.pig4cloud.pig.common.sse.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * websocket props
 *
 * @author Yakir
 */
@Data
@ConfigurationProperties(SseEmitterProperties.PREFIX)
public class SseEmitterProperties {

	public static final String PREFIX = "pig.sse";

	/**
     * SSE信息路径
	 */
	private String path = "/sse/info";

	/**
	 * 消息分发器：local | redis，默认 local, 如果自定义的话，可以配置为其他任意值
	 */
	private String messageDistributor = MessageDistributorTypeConstants.LOCAL;

}
