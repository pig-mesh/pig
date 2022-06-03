package com.pig4cloud.pig.common.security.component;

import com.pig4cloud.pig.common.security.service.PigRedisOAuth2AuthorizationConsentService;
import com.pig4cloud.pig.common.security.service.PigRedisOAuth2AuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;

/**
 * @author lengleng
 * @date 2022-06-02
 */
public class PigTokenStoreAutoConfiguration {

	@Bean
	public OAuth2AuthorizationService authorizationService(RedisTemplate redisTemplate) {
		return new PigRedisOAuth2AuthorizationService(redisTemplate);
	}

	@Bean
	public OAuth2AuthorizationConsentService auth2AuthorizationConsentService(RedisTemplate redisTemplate) {
		return new PigRedisOAuth2AuthorizationConsentService(redisTemplate);
	}

}
