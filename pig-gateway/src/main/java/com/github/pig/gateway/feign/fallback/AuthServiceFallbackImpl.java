package com.github.pig.gateway.feign.fallback;

import com.github.pig.gateway.feign.AuthSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;

/**
 * @author lengleng
 * @date 2018/1/9
 */
@Service
public class AuthServiceFallbackImpl implements AuthSerivce {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 获取 OAuth2AccessToken
     *
     * @param params 参数信息
     * @return OAuth2AccessToken
     */
    @Override
    public OAuth2AccessToken createToken(String params) {
        return null;
    }
}
