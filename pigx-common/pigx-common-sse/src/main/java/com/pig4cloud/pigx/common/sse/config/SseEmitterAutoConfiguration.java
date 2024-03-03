package com.pig4cloud.pigx.common.sse.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * websocket自动配置
 *
 * @author Yakir
 */
@EnableConfigurationProperties(SseEmitterProperties.class)
public class SseEmitterAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SseEmitterEndpoint sseEmitterEndpoint() {
		return new SseEmitterEndpoint();
	}

}
