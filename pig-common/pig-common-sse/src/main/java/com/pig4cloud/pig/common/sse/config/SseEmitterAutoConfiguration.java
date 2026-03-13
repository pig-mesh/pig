package com.pig4cloud.pig.common.sse.config;

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

    /**
     * 创建并返回一个SseEmitterEndpoint实例
     *
     * @return SseEmitterEndpoint实例
     * @ConditionalOnMissingBean 当容器中不存在该类型的bean时才会创建
     */
	@Bean
	@ConditionalOnMissingBean
	public SseEmitterEndpoint sseEmitterEndpoint() {
		return new SseEmitterEndpoint();
	}

}
