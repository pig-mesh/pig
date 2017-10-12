package com.github.pig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PigGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigGatewayApplication.class, args);
	}
}
