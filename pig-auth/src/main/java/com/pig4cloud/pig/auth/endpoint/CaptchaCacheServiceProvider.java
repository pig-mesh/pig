package com.pig4cloud.pig.auth.endpoint;

import com.anji.captcha.service.CaptchaCacheService;
import com.pig4cloud.pig.common.data.cache.RedisUtils;

/**
 * @author lengleng
 * @date 2020/8/27
 * <p>
 * 验证码 缓存提供支持集群,需要通过SPI
 */
public class CaptchaCacheServiceProvider implements CaptchaCacheService {

	private static final String REDIS = "redis";

	@Override
	public void set(String key, String value, long expiresInSeconds) {
        RedisUtils.set(key, value, expiresInSeconds);
	}

	@Override
	public boolean exists(String key) {
        return RedisUtils.hasKey(key);
	}

	@Override
	public void delete(String key) {
        RedisUtils.delete(key);
	}

	@Override
	public String get(String key) {
        return RedisUtils.get(key);
	}

	@Override
	public String type() {
		return REDIS;
	}

	@Override
	public Long increment(String key, long val) {
		return RedisUtils.increment(key, val);
	}

}
