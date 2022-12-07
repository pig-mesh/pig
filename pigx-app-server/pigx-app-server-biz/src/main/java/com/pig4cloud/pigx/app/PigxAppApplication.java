package com.pig4cloud.pigx.app;

import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnablePigxSwagger2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author pigx archetype
 * <p>
 * 项目启动类
 */
@EnablePigxSwagger2
@EnablePigxFeignClients
@EnableDiscoveryClient
@EnablePigxResourceServer
@SpringBootApplication
public class PigxAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxAppApplication.class, args);
	}

}
