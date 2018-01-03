package com.github.pig.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

/**
 * @author lengleng
 * @date 2017-12-29 13:02:29
 * zipkin 链路追踪
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableZipkinStreamServer
public class PigZipkinElkApplication {

    public static void main(String[] args) {
        SpringApplication.run(PigZipkinElkApplication.class, args);
    }
}
