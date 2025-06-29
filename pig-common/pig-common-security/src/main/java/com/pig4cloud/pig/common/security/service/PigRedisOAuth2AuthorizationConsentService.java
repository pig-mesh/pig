package com.pig4cloud.pig.common.security.service;

import com.pig4cloud.pig.common.core.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redis实现的OAuth2授权同意服务
 *
 * @author lengleng
 * @date 2025/05/31
 */
@RequiredArgsConstructor
public class PigRedisOAuth2AuthorizationConsentService implements OAuth2AuthorizationConsentService {

	private final static Long TIMEOUT = 10L;

	/**
	 * 保存OAuth2授权同意信息
	 * @param authorizationConsent 授权同意信息，不能为null
	 * @throws IllegalArgumentException 当authorizationConsent为null时抛出
	 */
	@Override
	public void save(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");

		RedisUtils.set(buildKey(authorizationConsent), authorizationConsent, TIMEOUT, TimeUnit.MINUTES);
	}

	/**
	 * 移除OAuth2授权同意信息
	 * @param authorizationConsent 授权同意信息，不能为null
	 * @throws IllegalArgumentException 当authorizationConsent为null时抛出
	 */
	@Override
	public void remove(OAuth2AuthorizationConsent authorizationConsent) {
		Assert.notNull(authorizationConsent, "authorizationConsent cannot be null");
		RedisUtils.delete(buildKey(authorizationConsent));
	}

	/**
	 * 根据注册客户端ID和主体名称查找OAuth2授权同意信息
	 * @param registeredClientId 注册客户端ID，不能为空
	 * @param principalName 主体名称，不能为空
	 * @return 查找到的OAuth2授权同意信息，可能为null
	 */
	@Override
	public OAuth2AuthorizationConsent findById(String registeredClientId, String principalName) {
		Assert.hasText(registeredClientId, "registeredClientId cannot be empty");
		Assert.hasText(principalName, "principalName cannot be empty");
		return RedisUtils.get(buildKey(registeredClientId, principalName));
	}

	/**
	 * 构建授权确认信息的key
	 * @param registeredClientId 注册客户端ID
	 * @param principalName 主体名称
	 * @return 拼接后的key字符串
	 */
	private static String buildKey(String registeredClientId, String principalName) {
		return "token:consent:" + registeredClientId + ":" + principalName;
	}

	/**
	 * 构建授权同意的键值
	 * @param authorizationConsent 授权同意对象
	 * @return 构建的键值字符串
	 */
	private static String buildKey(OAuth2AuthorizationConsent authorizationConsent) {
		return buildKey(authorizationConsent.getRegisteredClientId(), authorizationConsent.getPrincipalName());
	}

}
