package com.pig4cloud.pigx.flow.engine;

import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author pigx archetype
 * <p>
 * 项目启动类
 */
@EnablePigxFeignClients
@EnableDiscoveryClient
@EnablePigxResourceServer
@SpringBootApplication
public class PigxFlowEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxFlowEngineApplication.class, args);
	}

}
