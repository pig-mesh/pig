package com.github.pig.auth.controller;

import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author lengleng
 * @date 2017/10/26
 */
@RestController
public class UserController {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;


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
}