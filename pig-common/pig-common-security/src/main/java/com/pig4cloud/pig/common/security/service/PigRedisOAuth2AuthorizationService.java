package com.pig4cloud.pig.common.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.util.Assert;

/**
 * @author lengleng
 * @date 2022/5/27
 */
@RequiredArgsConstructor
public class PigRedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

	private static final String AUTHORIZATION = "token";

	private final RedisTemplate<String, String> redisTemplate;

	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		redisTemplate.opsForHash().put(AUTHORIZATION, authorization.getId(), authorization);
	}

	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");
		redisTemplate.opsForHash().delete(AUTHORIZATION, authorization.getId());
	}

	@Override
	@Nullable
	public OAuth2Authorization findById(String id) {
		Assert.hasText(id, "id cannot be empty");
		return (OAuth2Authorization) redisTemplate.opsForHash().get(AUTHORIZATION, id);
	}

	@Override
	@Nullable
	public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
		Assert.hasText(token, "token cannot be empty");
		for (Object authorization : redisTemplate.opsForHash().values(AUTHORIZATION)) {
			if (hasToken((OAuth2Authorization) authorization, token, tokenType)) {
				return (OAuth2Authorization) authorization;
			}
		}
		return null;
	}

	private static boolean hasToken(OAuth2Authorization authorization, String token,
			@Nullable OAuth2TokenType tokenType) {
		if (tokenType != null) {
			if ("state".equals(tokenType.getValue())) {
				return matchesState(authorization, token);
			}
			else if ("code".equals(tokenType.getValue())) {
				return matchesAuthorizationCode(authorization, token);
			}
			else if (OAuth2TokenType.ACCESS_TOKEN.equals(tokenType)) {
				return matchesAccessToken(authorization, token);
			}
			else {
				return OAuth2TokenType.REFRESH_TOKEN.equals(tokenType) && matchesRefreshToken(authorization, token);
			}
		}
		else {
			return matchesState(authorization, token) || matchesAuthorizationCode(authorization, token)
					|| matchesAccessToken(authorization, token) || matchesRefreshToken(authorization, token);
		}
	}

	private static boolean matchesState(OAuth2Authorization authorization, String token) {
		return token.equals(authorization.getAttribute("state"));
	}

	private static boolean matchesAuthorizationCode(OAuth2Authorization authorization, String token) {
		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
				.getToken(OAuth2AuthorizationCode.class);
		return authorizationCode != null && authorizationCode.getToken().getTokenValue().equals(token);
	}

	private static boolean matchesAccessToken(OAuth2Authorization authorization, String token) {
		OAuth2Authorization.Token<OAuth2AccessToken> accessToken = authorization.getToken(OAuth2AccessToken.class);
		return accessToken != null && accessToken.getToken().getTokenValue().equals(token);
	}

	private static boolean matchesRefreshToken(OAuth2Authorization authorization, String token) {
		OAuth2Authorization.Token<OAuth2RefreshToken> refreshToken = authorization.getToken(OAuth2RefreshToken.class);
		return refreshToken != null && refreshToken.getToken().getTokenValue().equals(token);
	}

}
