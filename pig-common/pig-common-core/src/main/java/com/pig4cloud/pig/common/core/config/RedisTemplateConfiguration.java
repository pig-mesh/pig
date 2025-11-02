/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis 配置类
 * <p>
 * 该配置类用于配置Redis序列化策略。默认使用JDK序列化机制来存储Value值。
 * <p>
 * <b>关于JDK序列化（RedisSerializer.java()）：</b>
 * <ul>
 * <li>优点：支持所有实现了Serializable接口的Java对象，无需额外配置</li>
 * <li>缺点：存储的数据为二进制格式，在Redis客户端中查看时会显示为乱码</li>
 * <li>示例：字符串"13"在Redis中显示为 "\xac\xed\x00\x05t\x00\x0213"</li>
 * <li>说明：\xac\xed 是Java序列化的魔数标识，后续字节包含类型和数据信息</li>
 * </ul>
 * <p>
 * <b>其他可选的序列化方式：</b>
 * <ul>
 * <li>RedisSerializer.string()：适用于纯字符串，Redis中显示为可读文本</li>
 * <li>RedisSerializer.json()：使用JSON格式，可读性好，但需要类型信息</li>
 * <li>GenericJackson2JsonRedisSerializer：自动处理JSON序列化，包含类型信息</li>
 * </ul>
 * <p>
 * 如果需要在Redis客户端中直接查看可读数据，建议将ValueSerializer改为JSON格式。但需注意：修改序列化方式后，旧数据将无法正常反序列化。
 *
 * @author lengleng
 * @date 2025/05/30
 */
@EnableCaching
@AutoConfiguration
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisTemplateConfiguration {

	/**
	 * 创建并配置RedisTemplate实例
	 * <p>
	 * 当前配置说明：
	 * <ul>
	 * <li>Key序列化：使用String序列化，确保Redis中的Key为可读字符串</li>
	 * <li>Value序列化：使用JDK序列化，支持所有Java对象但Redis中显示为二进制格式</li>
	 * <li>HashKey序列化：使用String序列化</li>
	 * <li>HashValue序列化：使用JDK序列化</li>
	 * </ul>
	 * @param factory Redis连接工厂
	 * @return 配置好的RedisTemplate实例
	 */
	@Bean
	@Primary
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		// Key使用String序列化，确保在Redis中显示为可读文本
		redisTemplate.setKeySerializer(RedisSerializer.string());
		redisTemplate.setHashKeySerializer(RedisSerializer.string());
		// Value使用JDK序列化，支持所有Java对象，但在Redis中显示为二进制格式（乱码）
		// 如需要可读性，可改为：RedisSerializer.json() 或 new GenericJackson2JsonRedisSerializer()
		redisTemplate.setValueSerializer(RedisSerializer.java());
		redisTemplate.setHashValueSerializer(RedisSerializer.java());
		redisTemplate.setConnectionFactory(factory);
		return redisTemplate;
	}

	/**
	 * 创建并返回HashOperations实例
	 * @param redisTemplate Redis模板
	 * @return HashOperations实例
	 */
	@Bean
	public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForHash();
	}

	/**
	 * 创建并返回用于操作Redis String类型数据的ValueOperations实例
	 * @param redisTemplate Redis模板，用于操作Redis
	 * @return ValueOperations实例，提供对Redis String类型数据的操作
	 */
	@Bean
	public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
		return redisTemplate.opsForValue();
	}

	/**
	 * 创建并返回ListOperations实例
	 * @param redisTemplate Redis模板
	 * @return ListOperations实例
	 */
	@Bean
	public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForList();
	}

	/**
	 * 创建并返回SetOperations实例
	 * @param redisTemplate Redis模板
	 * @return SetOperations实例
	 */
	@Bean
	public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForSet();
	}

	/**
	 * 创建并返回ZSetOperations实例
	 * @param redisTemplate Redis模板对象
	 * @return ZSetOperations实例
	 */
	@Bean
	public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForZSet();
	}

}
