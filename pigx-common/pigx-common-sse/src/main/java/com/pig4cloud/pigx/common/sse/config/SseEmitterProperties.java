package com.pig4cloud.pigx.common.sse.config;

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

	public static final String PREFIX = "pigx.sse";

	/**
	 * 路径: 无参: /ws 有参: PathVariable: 单参: /ws/{test} 多参: /ws/{test1}/{test2} query:
	 * /ws?uid=1&name=test
	 */
	private String path = "/sse/info";

	/**
	 * 消息分发器：local | redis，默认 local, 如果自定义的话，可以配置为其他任意值
	 */
	private String messageDistributor = MessageDistributorTypeConstants.LOCAL;

}
