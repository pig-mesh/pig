package com.github.pig.monitor;

import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * @author lengleng
 * @date 2017-12-26 10:15:30
 * 监控模块
 */
@EnableAdminServer
@EnableTurbine
@EnableDiscoveryClient
@SpringBootApplication
public class PigMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PigMonitorApplication.class, args);
    }
}
