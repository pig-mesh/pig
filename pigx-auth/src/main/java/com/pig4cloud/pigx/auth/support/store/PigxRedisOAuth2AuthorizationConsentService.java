package com.pig4cloud.pigx.auth.support.store;

import com.pig4cloud.pigx.common.data.cache.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class PigxRedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	private final static Long TIMEOUT = 10L;

	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");

        RedisUtils.set(buildKey(authorizationConsent), authorizationConsent, TIMEOUT * 60);

	}

	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
        RedisUtils.delete(buildKey(authorizationConsent));
	}

	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
        return RedisUtils.get(buildKey(registeredClientId, principalName));
	}

	private static String buildKey(String registeredClientId, String principalName) {
		return "token:consent:" + registeredClientId + ":" + principalName;
	}

	private static String buildKey(OAuth2AuthorizationConsent authorizationConsent) {
		return buildKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}

}
