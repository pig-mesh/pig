package com.github.pig.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author lengleng
 * @date 2017年10月27日13:59:05
 */
@EnableAsync
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.github.pig.admin", "com.github.pig.common.bean"})
public class PigAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(PigAdminApplication.class, args);
    }
}