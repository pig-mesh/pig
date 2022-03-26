package com.pig4cloud.pigx.common.websocket.config;

import com.pig4cloud.pigx.common.websocket.distribute.MessageDistributor;
import com.pig4cloud.pigx.common.websocket.distribute.RedisMessageDistributor;
import com.pig4cloud.pigx.common.websocket.distribute.RedisMessageListenerInitializer;
import com.pig4cloud.pigx.common.websocket.session.WebSocketSessionStore;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 基于 Redis Pub/Sub 的消息分发器
 *
 * @author hccake
 */
@ConditionalOnClass(StringRedisTemplate.class)
@ConditionalOnProperty(prefix = WebSocketProperties.PREFIX, name = "message-distributor",
		havingValue = MessageDistributorTypeConstants.REDIS)
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class RedisMessageDistributorConfig {

	private final WebSocketSessionStore webSocketSessionStore;

	@Bean
	@ConditionalOnMissingBean(MessageDistributor.class)
	public RedisMessageDistributor messageDistributor(StringRedisTemplate stringRedisTemplate) {
		return new RedisMessageDistributor(webSocketSessionStore, stringRedisTemplate);
	}

	@Bean
	@ConditionalOnBean(RedisMessageDistributor.class)
	@ConditionalOnMissingBean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}

	@Bean
	@ConditionalOnMissingBean
	public RedisMessageListenerInitializer redisMessageListenerInitializer(
			RedisMessageListenerContainer redisMessageListenerContainer,
			RedisMessageDistributor redisWebsocketMessageListener) {
		return new RedisMessageListenerInitializer(redisMessageListenerContainer, redisWebsocketMessageListener);
	}

}
