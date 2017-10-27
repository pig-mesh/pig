package com.github.pig.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author lengleng
 * @date 2017年10月27日13:59:05
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class DemoResourceApplication {
    @GetMapping("/demo")
    public String demo() {
        return "demo";
    }

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoResourceApplication.class, args);
    }

//    @Bean
//    LoadBalancerInterceptor loadBalancerInterceptor(LoadBalancerClient loadBalance) {
//        return new LoadBalancerInterceptor(loadBalance);
//    }
}