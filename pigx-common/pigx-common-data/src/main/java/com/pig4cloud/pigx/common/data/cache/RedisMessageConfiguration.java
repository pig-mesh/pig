package com.pig4cloud.pigx.common.data.cache;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author lengleng
 * @date 2022/2/4
 *
 * redis message 信道相关配置
 */
@Configuration(proxyBeanMethods = false)
public class RedisMessageConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		return container;
	}

}
