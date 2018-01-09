package com.github.pig.auth.controller;

import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.util.R;
import com.xiaoleilu.hutool.util.MapUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author lengleng
 * @date 2017/10/26
 */
@RestController
public class UserController {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @RequestMapping("/user")
    public Object user(Principal user) {
        return user;
    }

    /**
     * 清除Redis中 accesstoken refreshtoken
     *
     * @param accesstoken  accesstoken
     * @param refreshToken refreshToken
     * @return true/false
     */
    @PostMapping("/removeToken")
    @CacheEvict(value = SecurityConstants.TOKEN_USER_DETAIL, key = "#accesstoken")
    public R<Boolean> removeToken(String accesstoken, String refreshToken) {
        RedisTokenStore tokenStore = new RedisTokenStore(redisConnectionFactory);
        tokenStore.removeRefreshToken(refreshToken);
        tokenStore.removeAccessToken(accesstoken);
        return new R<>(Boolean.TRUE);
    }

    /**
     * 自定义token
     *
     * @param params 参数信息
     * @return OAuth2AccessToken
     */
    @RequestMapping(value = "/oauth/token_key")
    public OAuth2AccessToken getKey(String params) {
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(params);
//        if (clientDetails == null) {
//            throw new UnapprovedClientAuthenticationException("请求头中client信息不存在 clientId:" + clientId);
//        } else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
//            throw new UnapprovedClientAuthenticationException("请求头中client信息不合法 clientSecret:" + clientSecret);
//        }
//
//        TokenRequest tokenRequest = new TokenRequest(MapUtil.newHashMap(), clientId, clientDetails.getScope(), "mobile");
//        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
//        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
//        return authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        return null;
    }
}