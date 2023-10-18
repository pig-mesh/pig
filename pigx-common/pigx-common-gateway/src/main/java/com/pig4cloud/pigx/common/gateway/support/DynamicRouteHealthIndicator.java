package com.pig4cloud.pigx.common.gateway.support;

import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author lengleng
 * @date 2020/11/19
 * <p>
 * 动态路由检查检查
 */
@Slf4j
@RequiredArgsConstructor
public class DynamicRouteHealthIndicator extends AbstractHealthIndicator {

	private final RedisTemplate redisTemplate;

	@Override
	protected void doHealthCheck(Health.Builder builder) {
		if (redisTemplate.hasKey(CacheConstants.ROUTE_KEY)) {
			builder.up();
		}
		else {
			log.warn("动态路由监控检查失败，当前无路由配置");
			builder.down();
		}
	}

}
