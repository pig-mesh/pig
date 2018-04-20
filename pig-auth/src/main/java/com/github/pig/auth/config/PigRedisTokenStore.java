package com.github.pig.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lengleng
 * @date 2018/4/13
 * Pipeline is currently not supported for JedisClusterConnection.
 */
@Slf4j
public class PigRedisTokenStore implements TokenStore {

    private static final String ACCESS = "access:";
    private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private static final String AUTH = "auth:";
    private static final String REFRESH_AUTH = "refresh_auth:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    private static final String REFRESH = "refresh:";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access:";
    private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    private static final String UNAME_TO_ACCESS = "uname_to_access:";

    private RedisTemplate<String, Object> redisTemplate;

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        String key = authenticationKeyGenerator.extractKey(authentication);
        OAuth2AccessToken accessToken = (OAuth2AccessToken) redisTemplate.opsForValue().get(AUTH_TO_ACCESS + key);
        if (accessToken != null
                && !key.equals(authenticationKeyGenerator.extractKey(readAuthentication(accessToken.getValue())))) {
            storeAccessToken(accessToken, authentication);
        }
        return accessToken;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        return (OAuth2Authentication) this.redisTemplate.opsForValue().get(AUTH + token);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        return (OAuth2Authentication) this.redisTemplate.opsForValue().get(REFRESH_AUTH + token);
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {

        this.redisTemplate.opsForValue().set(ACCESS + token.getValue(), token);
        this.redisTemplate.opsForValue().set(AUTH + token.getValue(), authentication);
        this.redisTemplate.opsForValue().set(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication), token);
        if (!authentication.isClientOnly()) {
            redisTemplate.opsForList().rightPush(UNAME_TO_ACCESS + getApprovalKey(authentication), token);
        }

        redisTemplate.opsForList().rightPush(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId(), token);

        if (token.getExpiration() != null) {

            int seconds = token.getExpiresIn();
            redisTemplate.expire(ACCESS + token.getValue(), seconds, TimeUnit.SECONDS);
            redisTemplate.expire(AUTH + token.getValue(), seconds, TimeUnit.SECONDS);

            redisTemplate.expire(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication), seconds, TimeUnit.SECONDS);
            redisTemplate.expire(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId(), seconds, TimeUnit.SECONDS);
            redisTemplate.expire(UNAME_TO_ACCESS + getApprovalKey(authentication), seconds, TimeUnit.SECONDS);
        }
        if (token.getRefreshToken() != null && token.getRefreshToken().getValue() != null) {
            this.redisTemplate.opsForValue().set(REFRESH_TO_ACCESS + token.getRefreshToken().getValue(), token.getValue());
            this.redisTemplate.opsForValue().set(ACCESS_TO_REFRESH + token.getValue(), token.getRefreshToken().getValue());
        }
    }

    private String getApprovalKey(OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication() == null ? "" : authentication.getUserAuthentication()
                .getName();
        return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
    }

    private String getApprovalKey(String clientId, String userName) {
        return clientId + (userName == null ? "" : ":" + userName);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken accessToken) {
        removeAccessToken(accessToken.getValue());
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        return (OAuth2AccessToken) this.redisTemplate.opsForValue().get(ACCESS + tokenValue);
    }

    public void removeAccessToken(String tokenValue) {
        OAuth2AccessToken removed = (OAuth2AccessToken) redisTemplate.opsForValue().get(ACCESS + tokenValue);
        // caller to do that
        OAuth2Authentication authentication = (OAuth2Authentication) this.redisTemplate.opsForValue().get(AUTH + tokenValue);

        this.redisTemplate.delete(AUTH + tokenValue);
        redisTemplate.delete(ACCESS + tokenValue);
        this.redisTemplate.delete(ACCESS_TO_REFRESH + tokenValue);

        if (authentication != null) {
            this.redisTemplate.delete(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication));

            String clientId = authentication.getOAuth2Request().getClientId();
            redisTemplate.opsForList().leftPop(UNAME_TO_ACCESS + getApprovalKey(clientId, authentication.getName()));

            redisTemplate.opsForList().leftPop(CLIENT_ID_TO_ACCESS + clientId);

            this.redisTemplate.delete(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication));
        }
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        this.redisTemplate.opsForValue().set(REFRESH + refreshToken.getValue(), refreshToken);
        this.redisTemplate.opsForValue().set(REFRESH_AUTH + refreshToken.getValue(), authentication);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        return (OAuth2RefreshToken) this.redisTemplate.opsForValue().get(REFRESH + tokenValue);
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        removeRefreshToken(refreshToken.getValue());
    }

    public void removeRefreshToken(String tokenValue) {
        this.redisTemplate.delete(REFRESH + tokenValue);
        this.redisTemplate.delete(REFRESH_AUTH + tokenValue);
        this.redisTemplate.delete(REFRESH_TO_ACCESS + tokenValue);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    private void removeAccessTokenUsingRefreshToken(String refreshToken) {

        String token = (String) this.redisTemplate.opsForValue().get(REFRESH_TO_ACCESS + refreshToken);

        if (token != null) {
            redisTemplate.delete(ACCESS + token);
        }
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        List<Object> result = redisTemplate.opsForList().range(UNAME_TO_ACCESS + getApprovalKey(clientId, userName), 0, -1);

        if (result == null || result.size() == 0) {
            return Collections.emptySet();
        }
        List<OAuth2AccessToken> accessTokens = new ArrayList<>(result.size());

        for (Iterator<Object> it = result.iterator(); it.hasNext(); ) {
            OAuth2AccessToken accessToken = (OAuth2AccessToken) it.next();
            accessTokens.add(accessToken);
        }

        return Collections.unmodifiableCollection(accessTokens);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        List<Object> result = redisTemplate.opsForList().range((CLIENT_ID_TO_ACCESS + clientId), 0, -1);

        if (result == null || result.size() == 0) {
            return Collections.emptySet();
        }
        List<OAuth2AccessToken> accessTokens = new ArrayList<>(result.size());
        for (Iterator<Object> it = result.iterator(); it.hasNext(); ) {
            OAuth2AccessToken accessToken = (OAuth2AccessToken) it.next();
            accessTokens.add(accessToken);
        }

        return Collections.unmodifiableCollection(accessTokens);
    }
}