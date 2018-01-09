package com.github.pig.gateway.feign;

import com.github.pig.gateway.feign.fallback.AuthServiceFallbackImpl;
import com.github.pig.gateway.feign.fallback.MenuServiceFallbackImpl;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lengleng
 * @date 2018/1/8
 */
@FeignClient(name = "pig-auth-service", fallback = AuthServiceFallbackImpl.class)
public interface AuthSerivce {
    /**
     * 获取 OAuth2AccessToken
     *
     * @param params 参数信息
     * @return OAuth2AccessToken
     */
    @PostMapping(value = "/oauth/token_key")
    OAuth2AccessToken createToken(@RequestParam("params") String params);
}
