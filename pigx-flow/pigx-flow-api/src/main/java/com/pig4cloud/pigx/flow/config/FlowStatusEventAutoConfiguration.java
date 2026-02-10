package com.pig4cloud.pigx.flow.config;

import com.pig4cloud.pigx.flow.event.FlowStatusEventListener;
import com.pig4cloud.pigx.flow.event.FlowStatusEventPublisher;
import com.pig4cloud.pigx.flow.event.IProcessInstanceStatusEventService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.List;

/**
 * 流程状态事件自动配置
 *
 * @author lengleng
 * @date 2026-02-10
 */
@AutoConfiguration
@ConditionalOnClass(StringRedisTemplate.class)
public class FlowStatusEventAutoConfiguration {

	/**
	 * 事件发布者 - 所有依赖 flow-api 的模块可用
	 */
	@Bean
	@ConditionalOnMissingBean
	public FlowStatusEventPublisher flowStatusEventPublisher(StringRedisTemplate redisTemplate) {
		return new FlowStatusEventPublisher(redisTemplate);
	}

	/**
	 * 事件监听器 - 仅当有 IProcessInstanceStatusEventService 实现时启用
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(IProcessInstanceStatusEventService.class)
	public FlowStatusEventListener flowStatusEventListener(List<IProcessInstanceStatusEventService> eventServices,
			StringRedisTemplate redisTemplate) {
		return new FlowStatusEventListener(eventServices, redisTemplate);
	}

	/**
	 * 注册监听器到 Redis 容器
	 */
	@Bean
	@ConditionalOnBean(FlowStatusEventListener.class)
	public ChannelTopic flowStatusTopic(RedisMessageListenerContainer container, FlowStatusEventListener listener) {
		ChannelTopic topic = new ChannelTopic(FlowStatusEventPublisher.CHANNEL);
		container.addMessageListener(listener, topic);
		return topic;
	}

}
