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
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * RedisTemplate 配置
 *
 * @author L.cm
 */
@EnableCaching
@Configuration
@AutoConfigureBefore(name = { "org.redisson.spring.starter.RedissonAutoConfigurationV2",
		"org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration" })
public class RedisTemplateConfiguration {

	@Bean
	@Primary
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setHashKeySerializer(RedisSerializer.string());

		// 设置 redisTemplate 的序列化器为 json
		RedisSerializer<Object> jsonRedisSerializer = RedisSerializer.json();
		ObjectMapper objectMapper = (ObjectMapper) ReflectUtil.getFieldValue(jsonRedisSerializer, "mapper");
		objectMapper.registerModules(new PigxJavaTimeModule());
		redisTemplate.setValueSerializer(jsonRedisSerializer);
		redisTemplate.setHashValueSerializer(jsonRedisSerializer);

		redisTemplate.setConnectionFactory(redisConnectionFactory);
		return redisTemplate;
	}

}
