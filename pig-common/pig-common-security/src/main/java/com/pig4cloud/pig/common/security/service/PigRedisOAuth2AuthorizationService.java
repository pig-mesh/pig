package com.pig4cloud.pig.common.security.service;

import com.pig4cloud.pig.common.core.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.util.Assert;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis实现的OAuth2授权服务类
 *
 * @author lengleng
 * @date 2025/05/31
 */
@RequiredArgsConstructor
public class PigRedisOAuth2AuthorizationService implements OAuth2AuthorizationService {

	private final static Long TIMEOUT = 10L;

	private static final String AUTHORIZATION = "token";

	/**
	 * 保存OAuth2授权信息到Redis
	 * @param authorization 授权信息对象，不能为null
	 * @throws IllegalArgumentException 当authorization为null时抛出异常
	 */
	@Override
	public void save(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");

		if (isState(authorization)) {
			String token = authorization.getAttribute("state");
			RedisUtils.set(buildKey(OAuth2ParameterNames.STATE, token), authorization, TIMEOUT, TimeUnit.MINUTES);
		}

		if (isCode(authorization)) {
			OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
				.getToken(OAuth2AuthorizationCode.class);
			OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
			long between = ChronoUnit.MINUTES.between(authorizationCodeToken.getIssuedAt(),
					authorizationCodeToken.getExpiresAt());
			RedisUtils.set(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()), authorization,
					between, TimeUnit.MINUTES);
		}

		if (isRefreshToken(authorization)) {
			OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
			long between = ChronoUnit.SECONDS.between(refreshToken.getIssuedAt(), refreshToken.getExpiresAt());
			RedisUtils.set(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()), authorization,
					between, TimeUnit.SECONDS);
		}

		if (isAccessToken(authorization)) {
			OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
			long between = ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt());
			RedisUtils.set(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()), authorization,
					between, TimeUnit.SECONDS);
		}
	}

	/**
	 * 移除OAuth2授权信息
	 * @param authorization 要移除的授权信息，不能为null
	 * @throws IllegalArgumentException 当authorization为null时抛出
	 */
	@Override
	public void remove(OAuth2Authorization authorization) {
		Assert.notNull(authorization, "authorization cannot be null");

		List<String> keys = new ArrayList<>();
		if (isState(authorization)) {
			String token = authorization.getAttribute("state");
			keys.add(buildKey(OAuth2ParameterNames.STATE, token));
		}

		if (isCode(authorization)) {
			OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
				.getToken(OAuth2AuthorizationCode.class);
			OAuth2AuthorizationCode authorizationCodeToken = authorizationCode.getToken();
			keys.add(buildKey(OAuth2ParameterNames.CODE, authorizationCodeToken.getTokenValue()));
		}

		if (isRefreshToken(authorization)) {
			OAuth2RefreshToken refreshToken = authorization.getRefreshToken().getToken();
			keys.add(buildKey(OAuth2ParameterNames.REFRESH_TOKEN, refreshToken.getTokenValue()));
		}

		if (isAccessToken(authorization)) {
			OAuth2AccessToken accessToken = authorization.getAccessToken().getToken();
			keys.add(buildKey(OAuth2ParameterNames.ACCESS_TOKEN, accessToken.getTokenValue()));
		}
		RedisUtils.delete(keys.toArray(String[]::new));
	}

	/**
	 * 根据ID查询OAuth2授权信息
	 * @param id 授权ID
	 * @return 授权信息，可能为null
	 * @throws UnsupportedOperationException 当前不支持此操作
	 */
	@Override
	@Nullable
	public OAuth2Authorization findById(String id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 根据token和token类型查询OAuth2授权信息
	 * @param token token值
	 * @param tokenType token类型，不能为null
	 * @return 授权信息对象，可能为null
	 * @throws IllegalArgumentException 当token为空或tokenType为null时抛出
	 */
	@Override
	@Nullable
	public OAuth2Authorization findByToken(String token, @Nullable OAuth2TokenType tokenType) {
		Assert.hasText(token, "token cannot be empty");
		Assert.notNull(tokenType, "tokenType cannot be empty");
		return RedisUtils.get(buildKey(tokenType.getValue(), token));
	}

	/**
	 * 构建key
	 * @param type 类型
	 * @param id ID
	 * @return 拼接后的key字符串
	 */
	private String buildKey(String type, String id) {
		return String.format("%s::%s::%s", AUTHORIZATION, type, id);
	}

	/**
	 * 检查授权对象是否包含state属性
	 * @param authorization 授权对象
	 * @return 如果包含state属性返回true，否则返回false
	 */
	private static boolean isState(OAuth2Authorization authorization) {
		return Objects.nonNull(authorization.getAttribute("state"));
	}

	/**
	 * 检查授权对象是否包含授权码
	 * @param authorization 授权对象
	 * @return 如果包含授权码返回true，否则返回false
	 */
	private static boolean isCode(OAuth2Authorization authorization) {
		OAuth2Authorization.Token<OAuth2AuthorizationCode> authorizationCode = authorization
			.getToken(OAuth2AuthorizationCode.class);
		return Objects.nonNull(authorizationCode);
	}

	/**
	 * 判断授权是否包含刷新令牌
	 * @param authorization 授权信息
	 * @return 如果包含刷新令牌返回true，否则返回false
	 */
	private static boolean isRefreshToken(OAuth2Authorization authorization) {
		return Objects.nonNull(authorization.getRefreshToken());
	}

	/**
	 * 判断授权对象是否包含访问令牌
	 * @param authorization 授权对象
	 * @return 如果包含访问令牌返回true，否则返回false
	 */
	private static boolean isAccessToken(OAuth2Authorization authorization) {
		return Objects.nonNull(authorization.getAccessToken());
	}

}
