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

package com.pig4cloud.pigx.common.data.cache;

import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.jackson.PigxJavaTimeModule;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizers;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 扩展redis-cache支持注解cacheName添加超时时间
 *
 * @author L.cm
 */
@Configuration
@AutoConfigureAfter({RedisAutoConfiguration.class})
@ConditionalOnBean({RedisConnectionFactory.class})
@EnableConfigurationProperties(CacheProperties.class)
public class RedisCacheAutoConfiguration {

    private final CacheProperties cacheProperties;

    private final CacheManagerCustomizers customizerInvoker;

    @Nullable
    private final RedisCacheConfiguration redisCacheConfiguration;

    RedisCacheAutoConfiguration(CacheProperties cacheProperties, CacheManagerCustomizers customizerInvoker,
                                ObjectProvider<RedisCacheConfiguration> redisCacheConfiguration) {
        this.cacheProperties = cacheProperties;
        this.customizerInvoker = customizerInvoker;
        this.redisCacheConfiguration = redisCacheConfiguration.getIfAvailable();
    }

    @Primary
    @Bean("cacheResolver")
    public CacheManager redisCacheManager(ObjectProvider<RedisConnectionFactory> connectionFactoryObjectProvider) {
        RedisConnectionFactory connectionFactory = connectionFactoryObjectProvider.getIfAvailable();
        Objects.requireNonNull(connectionFactory, "Bean RedisConnectionFactory is null.");
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        RedisCacheConfiguration cacheConfiguration = this.determineConfiguration();
        List<String> cacheNames = this.cacheProperties.getCacheNames();
        Map<String, RedisCacheConfiguration> initialCaches = new LinkedHashMap<>();
        if (!cacheNames.isEmpty()) {
            Map<String, RedisCacheConfiguration> cacheConfigMap = new LinkedHashMap<>(cacheNames.size());
            cacheNames.forEach(it -> cacheConfigMap.put(it, cacheConfiguration));
            initialCaches.putAll(cacheConfigMap);
        }
        boolean allowInFlightCacheCreation = true;
        boolean enableTransactions = false;
        RedisAutoCacheManager cacheManager = new RedisAutoCacheManager(
                redisCacheWriter, cacheConfiguration, allowInFlightCacheCreation, initialCaches
        );
        cacheManager.setTransactionAware(enableTransactions);
        return this.customizerInvoker.customize(cacheManager);
    }

    private RedisCacheConfiguration determineConfiguration() {
        if (this.redisCacheConfiguration != null) {
            return this.redisCacheConfiguration;
        } else {
            CacheProperties.Redis redisProperties = this.cacheProperties.getRedis();
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

            RedisSerializer<Object> jsonRedisSerializer = RedisSerializer.json();
            ObjectMapper objectMapper = (ObjectMapper) ReflectUtil.getFieldValue(jsonRedisSerializer, "mapper");
            objectMapper.registerModules(new PigxJavaTimeModule());
            config = config.serializeValuesWith(RedisSerializationContext.SerializationPair
                    .fromSerializer(jsonRedisSerializer));
            if (redisProperties.getTimeToLive() != null) {
                config = config.entryTtl(redisProperties.getTimeToLive());
            }

            if (redisProperties.getKeyPrefix() != null) {
                config = config.prefixCacheNameWith(redisProperties.getKeyPrefix());
            }

            if (!redisProperties.isCacheNullValues()) {
                config = config.disableCachingNullValues();
            }

            if (!redisProperties.isUseKeyPrefix()) {
                config = config.disableKeyPrefix();
            }

            return config;
        }
    }

}
