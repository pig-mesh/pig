package com.github.pig.daemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lengleng
 * @date 2018年02月07日20:35:35
 * 分布式任务调度模块
 */
@EnableDiscoveryClient
@SpringBootApplication
public class PigDaemonApplication {

    public static void main(String[] args) {
        SpringApplication.run(PigDaemonApplication.class, args);
    }

}
