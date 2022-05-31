package com.pig4cloud.pig.common.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

@RequiredArgsConstructor
public class PigRedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		redisTemplate.opsForHash().put(buildKey(authorizationConsent), authorizationConsent.getRegisteredClientId(), authorizationConsent);
	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		redisTemplate.opsForHash().delete(buildKey(authorizationConsent),  authorizationConsent.getRegisteredClientId());
	}

	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		return (OAuth2AuthorizationConsent)  redisTemplate.opsForHash().get(buildKey(registeredClientId, principalName), registeredClientId);
	}


	private static String buildKey(String registeredClientId, String principalName) {
		return "OAC::" + registeredClientId + "::" + principalName;
	}

	private static String buildKey(OAuth2AuthorizationConsent authorizationConsent) {
		return buildKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}


}


