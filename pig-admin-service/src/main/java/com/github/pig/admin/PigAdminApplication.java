package com.github.pig.admin;

import com.github.pig.common.web.BaseController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lengleng
 * @date 2017年10月27日13:59:05
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PigAdminApplication extends BaseController {
    public static void main(String[] args) {
        SpringApplication.run(PigAdminApplication.class, args);
    }
}