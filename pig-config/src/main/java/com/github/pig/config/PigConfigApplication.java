package com.github.pig.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author lengleng
 */
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class PigConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(PigConfigApplication.class, args);
    }
}
