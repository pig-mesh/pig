package com.github.pig.auth.config;

import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.constant.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author lengleng
 * @date 2017/10/27
 * 认证服务器逻辑实现
 */

@Configuration
@EnableAuthorizationServer
public class PigAuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthServerConfig authServerConfig;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(authServerConfig.getClientId())
                .secret(authServerConfig.getClientSecret())
                .authorizedGrantTypes(SecurityConstants.REFRESH_TOKEN, SecurityConstants.PASSWORD)
                .scopes(authServerConfig.getScope());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(new RedisTokenStore(redisConnectionFactory))
                .accessTokenConverter(jwtAccessTokenConverter())
                .authenticationManager(authenticationManager)
                .reuseRefreshTokens(false)
                .userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(CommonConstant.SIGN_KEY);
        return jwtAccessTokenConverter;
    }

}
