package com.github.pig.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import zipkin.server.EnableZipkinServer;

/**
 * @author lengleng
 * @date 2018-01-24
 * zipkin mysql 存储实现
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZipkinServer
public class PigZipkinDbApplication {
    public static void main(String[] args) {
        SpringApplication.run(PigZipkinDbApplication.class, args);
    }
}
