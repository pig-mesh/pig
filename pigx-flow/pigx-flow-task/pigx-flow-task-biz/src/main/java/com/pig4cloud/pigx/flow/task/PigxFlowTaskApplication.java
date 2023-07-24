package com.pig4cloud.pigx.flow.task;

import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnableOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author pigx archetype
 * <p>
 * 项目启动类
 */
@EnableOpenApi("task")
@EnablePigxFeignClients
@EnableDiscoveryClient
@EnablePigxResourceServer
@SpringBootApplication
public class PigxFlowTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxFlowTaskApplication.class, args);
	}

}
