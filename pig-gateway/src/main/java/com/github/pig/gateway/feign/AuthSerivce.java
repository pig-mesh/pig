package com.github.pig.gateway.feign;

import com.github.pig.gateway.feign.fallback.MenuServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * @author lengleng
 * @date 2018/1/8
 */
@FeignClient(name = "pig-auth-service", fallback = MenuServiceFallbackImpl.class)
public interface AuthSerivce {
    /**
     * 获取 OAuth2AccessToken
     *
     * @param clientId       客户端ID
     * @param clientSecret   客户端Secret
     * @param authentication 认证信息
     * @return OAuth2AccessToken
     */
    OAuth2AccessToken createToken(String clientId, String clientSecret, Authentication authentication);
}
