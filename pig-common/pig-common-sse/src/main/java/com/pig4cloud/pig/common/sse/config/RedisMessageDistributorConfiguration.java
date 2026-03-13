package com.pig4cloud.pig.common.sse.config;

import com.pig4cloud.pig.common.sse.distribute.MessageDistributor;
import com.pig4cloud.pig.common.sse.distribute.RedisMessageDistributor;
import com.pig4cloud.pig.common.sse.distribute.RedisSseEmitterMessageListener;
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

    /**
     * 创建Redis消息分发器Bean
     *
     * @param stringRedisTemplate Redis字符串模板
     * @return Redis消息分发器实例
     * @ConditionalOnMissingBean 当容器中不存在MessageDistributor类型的Bean时创建
     */
	@Bean
	@ConditionalOnMissingBean(MessageDistributor.class)
    public RedisMessageDistributor sseMessageDistributor(StringRedisTemplate stringRedisTemplate) {
		return new RedisMessageDistributor(stringRedisTemplate);
    }

    /**
     * 创建RedisSseEmitterMessageListener实例
     *
     * @param stringRedisTemplate Redis字符串模板
     * @return RedisSseEmitterMessageListener实例
     * @see RedisSseEmitterMessageListener
	 */
	@Bean
	@ConditionalOnBean(RedisMessageDistributor.class)
	@ConditionalOnMissingBean
    public RedisSseEmitterMessageListener sseRedisWebsocketMessageListener(StringRedisTemplate stringRedisTemplate) {
		return new RedisSseEmitterMessageListener(stringRedisTemplate);
    }

    /**
     * 创建Redis消息监听容器
     *
     * @param connectionFactory Redis连接工厂
     * @return Redis消息监听容器实例
     * @see RedisMessageListenerContainer
	 */
	@Bean
	@ConditionalOnBean(RedisMessageDistributor.class)
	@ConditionalOnMissingBean
    public RedisMessageListenerContainer sseRedisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		return container;
    }

    /**
     * SSE Redis消息监听器注册配置类
     * <p>
     * 用于配置Redis消息监听器，实现SSE消息分发
     *
     * @author lengleng
     * @date 2025/06/06
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingBean(MessageDistributor.class)
	@RequiredArgsConstructor
    static class SseRedisMessageListenerRegisterConfiguration {

		private final RedisMessageListenerContainer redisMessageListenerContainer;

		private final RedisSseEmitterMessageListener redisWebsocketMessageListener;

		@PostConstruct
		public void addMessageListener() {
			redisMessageListenerContainer.addMessageListener(redisWebsocketMessageListener,
					new PatternTopic(RedisSseEmitterMessageListener.CHANNEL));
		}

	}

}
