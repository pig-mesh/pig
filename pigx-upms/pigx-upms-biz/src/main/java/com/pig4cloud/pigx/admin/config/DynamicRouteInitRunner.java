/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.admin.config;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.service.SysRouteConfService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.gateway.support.DynamicRouteInitEvent;
import com.pig4cloud.pigx.common.gateway.vo.RouteDefinitionVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.event.TransactionalEventListener;

import java.net.URI;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/10/31
 * <p>
 * 容器启动后保存配置文件里面的路由信息到Redis
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DynamicRouteInitRunner implements InitializingBean {

	private final RedisTemplate<String, String> redisTemplate;

	private final SysRouteConfService routeConfService;

	private final RedisMessageListenerContainer listenerContainer;

	/**
	 * WebServerInitializedEvent 使用 TransactionalEventListener 时启动时无法获取到事件
	 */
	@Async
	@Order
	@EventListener({ WebServerInitializedEvent.class })
	public void WebServerInit() {
		this.initRoute();
	}

	@Async
	@Order
	@TransactionalEventListener({ DynamicRouteInitEvent.class })
	public void initRoute() {
		redisTemplate.delete(CacheConstants.ROUTE_KEY);
		log.info("开始初始化网关路由");

		routeConfService.list().forEach(route -> {
			RouteDefinitionVo vo = new RouteDefinitionVo();
			vo.setRouteName(route.getRouteName());
			vo.setId(route.getRouteId());
			vo.setUri(URI.create(route.getUri()));
			vo.setOrder(route.getSortOrder());

			JSONArray filterObj = JSONUtil.parseArray(route.getFilters());
			vo.setFilters(filterObj.toList(FilterDefinition.class));
			JSONArray predicateObj = JSONUtil.parseArray(route.getPredicates());
			vo.setPredicates(predicateObj.toList(PredicateDefinition.class));
			vo.setMetadata(JSONUtil.toBean(route.getMetadata(), Map.class));
			log.info("加载路由ID：{},{}", route.getRouteId(), vo);

			redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RouteDefinitionVo.class));
			HashOperations<String, String, RouteDefinitionVo> stringStringValueOperations = redisTemplate.opsForHash();
			stringStringValueOperations.put(CacheConstants.ROUTE_KEY, route.getRouteId(), vo);
		});

		// 通知网关重置路由
		redisTemplate.convertAndSend(CacheConstants.ROUTE_JVM_RELOAD_TOPIC, "路由信息,网关缓存更新");
		log.debug("初始化网关路由结束 ");
	}

	/**
	 * redis 监听配置,监听 upms_redis_route_reload_topic,重新加载Redis
	 */
	@Override
	public void afterPropertiesSet() {
		listenerContainer.addMessageListener((message, bytes) -> {
			log.warn("接收到重新Redis 重新加载路由事件");
			initRoute();
		}, new ChannelTopic(CacheConstants.ROUTE_REDIS_RELOAD_TOPIC));
	}

}
