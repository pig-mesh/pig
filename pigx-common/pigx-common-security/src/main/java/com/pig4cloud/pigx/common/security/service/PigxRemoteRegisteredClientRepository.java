package com.pig4cloud.pigx.common.security.service;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pig4cloud.pigx.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pigx.admin.api.feign.RemoteClientDetailsService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.RetOps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 查询客户端相关信息实现
 *
 * @author lengleng
 * @date 2022/5/29
 */
@RequiredArgsConstructor
public class PigxRemoteRegisteredClientRepository implements RegisteredClientRepository {

    /**
     * 刷新令牌有效期默认 30 天
     */
    private final static int refreshTokenValiditySeconds = 60 * 60 * 24 * 30;

    /**
     * 请求令牌有效期默认 12 小时
     */
    private final static int accessTokenValiditySeconds = 60 * 60 * 12;

    private final RemoteClientDetailsService clientDetailsService;

    private final CacheManager cacheManager;

    /**
     * Saves the registered client.
     *
     * <p>
     * IMPORTANT: Sensitive information should be encoded externally from the
     * implementation, e.g. {@link RegisteredClient#getClientSecret()}
     *
     * @param registeredClient the {@link RegisteredClient}
     */
    @Override
    public void save(RegisteredClient registeredClient) {
    }

    /**
     * Returns the registered client identified by the provided {@code id}, or
     * {@code null} if not found.
     *
     * @param id the registration identifier
     * @return the {@link RegisteredClient} if found, otherwise {@code null}
     */
    @Override
    public RegisteredClient findById(String id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the registered client identified by the provided {@code clientId}, or
     * {@code null} if not found.
     * @param clientId the client identifier
     * @return the {@link RegisteredClient} if found, otherwise {@code null}
     */

    /**
     * 按客户端 ID 查找
     *
     * @param clientId 客户端 ID
     * @return {@link RegisteredClient }
     */
    @Override
    @SneakyThrows
    public RegisteredClient findByClientId(String clientId) {

        Cache cache = cacheManager.getCache(CacheConstants.CLIENT_DETAILS_KEY);
        SysOauthClientDetails clientDetails = null;
        if (Objects.nonNull(cache) && Objects.nonNull(cache.get(clientId))) {
            clientDetails = cache.get(clientId, SysOauthClientDetails.class);
        }

        if (Objects.isNull(clientDetails)) {
            SysOauthClientDetails fetchedClientDetails = RetOps
                    .of(clientDetailsService.getClientDetailsById(clientId))
                    .getData()
                    .orElseThrow(() -> new OAuth2AuthorizationCodeRequestAuthenticationException(
                            new OAuth2Error("客户端查询异常，请检查数据库链接"), null));

            clientDetails = fetchedClientDetails;
            Optional.ofNullable(cache).ifPresent(c -> c.put(clientId, fetchedClientDetails));
        }

        final SysOauthClientDetails finalClientDetails = clientDetails;

        RegisteredClient.Builder builder = RegisteredClient.withId(finalClientDetails.getClientId())
                .clientId(finalClientDetails.getClientId())
                .clientSecret(SecurityConstants.NOOP + finalClientDetails.getClientSecret())
                .clientAuthenticationMethods(clientAuthenticationMethods -> {
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                    clientAuthenticationMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_POST);
                });

        // 授权模式
        Arrays.stream(finalClientDetails.getAuthorizedGrantTypes())
                .forEach(grant -> builder.authorizationGrantType(new AuthorizationGrantType(grant)));

        // 回调地址
        Optional.ofNullable(finalClientDetails.getWebServerRedirectUri())
                .ifPresent(redirectUri -> Arrays.stream(redirectUri.split(StrUtil.COMMA))
                        .filter(StrUtil::isNotBlank)
                        .forEach(builder::redirectUri));

        // scope
        Optional.ofNullable(finalClientDetails.getScope())
                .ifPresent(scope -> Arrays.stream(scope.split(StrUtil.COMMA))
                        .filter(StrUtil::isNotBlank)
                        .forEach(builder::scope));

        // 注入扩展配置
        Optional.ofNullable(finalClientDetails.getAdditionalInformation()).ifPresent(ext -> {
            Map<String, Object> map = JSONUtil.parseObj(ext).toBean(Map.class);
            builder.clientSettings(ClientSettings.withSettings(map)
                    .requireProofKey(false)
                    .requireAuthorizationConsent(!BooleanUtil.toBoolean(finalClientDetails.getAutoapprove()))
                    .build());
        });

        return builder
                .tokenSettings(TokenSettings.builder()
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                        .accessTokenTimeToLive(Duration.ofSeconds(
                                Optional.ofNullable(finalClientDetails.getAccessTokenValidity()).orElse(accessTokenValiditySeconds)))
                        .refreshTokenTimeToLive(Duration.ofSeconds(Optional.ofNullable(finalClientDetails.getRefreshTokenValidity())
                                .orElse(refreshTokenValiditySeconds)))
                        .build())
                .build();
    }

}
