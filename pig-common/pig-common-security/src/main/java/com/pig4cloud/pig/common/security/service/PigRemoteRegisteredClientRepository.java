package com.pig4cloud.pig.common.security.service;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.admin.api.feign.RemoteClientDetailsService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.RetOps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationException;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

/**
 * 查询客户端相关信息实现类，支持Redis缓存
 *
 * @author lengleng
 * @date 2025/05/31
 */
@RequiredArgsConstructor
public class PigRemoteRegisteredClientRepository implements RegisteredClientRepository {

	/**
	 * 刷新令牌有效期默认 30 天
	 */
	private final static int refreshTokenValiditySeconds = 60 * 60 * 24 * 30;

	/**
	 * 请求令牌有效期默认 12 小时
	 */
	private final static int accessTokenValiditySeconds = 60 * 60 * 12;

	/**
	 * 远程客户端详情服务
	 */
	private final RemoteClientDetailsService clientDetailsService;

	/**
	 * 保存注册的客户端
	 *
	 * <p>
	 * 重要提示：敏感信息应在实现外部进行编码，例如 {@link RegisteredClient#getClientSecret()}
	 * </p>
	 * @param registeredClient 要保存的注册客户端
	 */
	@Override
	public void save(RegisteredClient registeredClient) {
	}

	/**
	 * 根据ID查找已注册的客户端
	 * @param id 注册标识符
	 * @return 找到的{@link RegisteredClient}，未找到则返回{@code null}
	 */
	@Override
	public RegisteredClient findById(String id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 根据客户端ID查询注册客户端信息，支持Redis缓存
	 * @param clientId 客户端ID
	 * @return 注册客户端信息
	 * @throws OAuth2AuthorizationCodeRequestAuthenticationException 客户端查询异常时抛出
	 */
	@Override
	@SneakyThrows
	@Cacheable(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId", unless = "#result == null")
	public RegisteredClient findByClientId(String clientId) {

		SysOauthClientDetails clientDetails = RetOps.of(clientDetailsService.getClientDetailsById(clientId))
			.getData()
			.orElseThrow(() -> new OAuth2AuthorizationCodeRequestAuthenticationException(
					new OAuth2Error("客户端查询异常，请检查数据库链接"), null));

		RegisteredClient.Builder builder = RegisteredClient.withId(clientDetails.getClientId())
			.clientId(clientDetails.getClientId())
			.clientSecret(SecurityConstants.NOOP + clientDetails.getClientSecret())
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);

		for (String authorizedGrantType : clientDetails.getAuthorizedGrantTypes()) {
			builder.authorizationGrantType(new AuthorizationGrantType(authorizedGrantType));

		}
		// 回调地址
		Optional.ofNullable(clientDetails.getWebServerRedirectUri())
			.ifPresent(redirectUri -> Arrays.stream(redirectUri.split(StrUtil.COMMA))
				.filter(StrUtil::isNotBlank)
				.forEach(builder::redirectUri));

		// scope
		Optional.ofNullable(clientDetails.getScope())
			.ifPresent(scope -> Arrays.stream(scope.split(StrUtil.COMMA))
				.filter(StrUtil::isNotBlank)
				.forEach(builder::scope));

		return builder
			.tokenSettings(TokenSettings.builder()
				.accessTokenFormat(OAuth2TokenFormat.REFERENCE)
				.accessTokenTimeToLive(Duration.ofSeconds(
						Optional.ofNullable(clientDetails.getAccessTokenValidity()).orElse(accessTokenValiditySeconds)))
				.refreshTokenTimeToLive(Duration.ofSeconds(Optional.ofNullable(clientDetails.getRefreshTokenValidity())
					.orElse(refreshTokenValiditySeconds)))
				.build())
			.clientSettings(ClientSettings.builder()
				.requireAuthorizationConsent(!BooleanUtil.toBoolean(clientDetails.getAutoapprove()))
				.build())
			.build();

	}

}
