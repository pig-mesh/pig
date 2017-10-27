package com.github.pig.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author lengleng
 */
@EnableDiscoveryClient
@SpringBootApplication
public class PigServiceAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(PigServiceAuthApplication.class, args);
    }



}
