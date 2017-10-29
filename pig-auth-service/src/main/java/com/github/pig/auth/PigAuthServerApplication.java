package com.github.pig.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * @author lengleng
 * 获取用户信息也是通过这个应用实现
 * 这里既是认证服务器，也是资源服务器
 * EnableResourceServer
 */
@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
public class PigAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PigAuthServerApplication.class, args);
    }

}
