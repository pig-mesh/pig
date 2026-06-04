package com.pig4cloud.pig.common.security.captcha;

import com.anji.captcha.service.CaptchaCacheService;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 行为验证码缓存的 Redis 实现，通过 SPI 加载替换 anji-captcha 默认本地实现， 让多实例部署共享同一份验证码缓存。
 *
 * @author lengleng
 * @date 2026-05-20
 */
public class CaptchaCacheServiceProvider implements CaptchaCacheService {

	private static final String REDIS = "redis";

	@Override
	public void set(String key, String value, long expiresInSeconds) {
		redisTemplate().opsForValue().set(key, value, expiresInSeconds, TimeUnit.SECONDS);
	}

	@Override
	public boolean exists(String key) {
		return Boolean.TRUE.equals(redisTemplate().hasKey(key));
	}

	@Override
	public void delete(String key) {
		redisTemplate().delete(key);
	}

	@Override
	public String get(String key) {
		Object value = redisTemplate().opsForValue().get(key);
		return value == null ? null : value.toString();
	}

	@Override
	public String type() {
		return REDIS;
	}

	@Override
	public Long increment(String key, long val) {
		return redisTemplate().opsForValue().increment(key, val);
	}

	@SuppressWarnings("unchecked")
	private RedisTemplate<String, Object> redisTemplate() {
		return SpringContextHolder.getBean(RedisTemplate.class);
	}

}
