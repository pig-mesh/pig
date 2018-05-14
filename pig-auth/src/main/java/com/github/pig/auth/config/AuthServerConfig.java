package com.github.pig.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2017/10/28
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthServerConfig {
    private List<AuthServer> clients = new ArrayList<>();

    @Data
    class AuthServer {
        private String clientId;
        private String clientSecret;
        private String scope;
    }
}
