package com.pig4cloud.pigx.common.sse.config;

import com.pig4cloud.pigx.common.sse.distribute.MessageDistributor;
import com.pig4cloud.pigx.common.sse.distribute.RedisMessageDistributor;
import com.pig4cloud.pigx.common.sse.distribute.RedisSseEmitterMessageListener;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * 基于 Redis Pub/Sub 的消息分发器
 *
 * @author lengleng
 */
@ConditionalOnClass(StringRedisTemplate.class)
@ConditionalOnProperty(prefix = SseEmitterProperties.PREFIX, name = "message-distributor",
		havingValue = MessageDistributorTypeConstants.REDIS, matchIfMissing = true)
@Configuration(proxyBeanMethods = false)
public class RedisMessageDistributorConfiguration {

	@Bean
	@ConditionalOnMissingBean(MessageDistributor.class)
	public RedisMessageDistributor messageDistributor(StringRedisTemplate stringRedisTemplate) {
		return new RedisMessageDistributor(stringRedisTemplate);
	}

	@Bean
	@ConditionalOnBean(RedisMessageDistributor.class)
	@ConditionalOnMissingBean
	public RedisSseEmitterMessageListener redisWebsocketMessageListener(StringRedisTemplate stringRedisTemplate) {
		return new RedisSseEmitterMessageListener(stringRedisTemplate);
	}

	@Bean
	@ConditionalOnBean(RedisMessageDistributor.class)
	@ConditionalOnMissingBean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(MessageDistributor.class)
	@RequiredArgsConstructor
	static class RedisMessageListenerRegisterConfiguration {

		private final RedisMessageListenerContainer redisMessageListenerContainer;

		private final RedisSseEmitterMessageListener redisWebsocketMessageListener;

		@PostConstruct
		public void addMessageListener() {
			redisMessageListenerContainer.addMessageListener(redisWebsocketMessageListener,
					new PatternTopic(RedisSseEmitterMessageListener.CHANNEL));
		}

	}

}
