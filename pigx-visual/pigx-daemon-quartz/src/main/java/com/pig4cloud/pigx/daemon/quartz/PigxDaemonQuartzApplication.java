package com.pig4cloud.pigx.daemon.quartz;

import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import com.pig4cloud.pigx.common.security.annotation.EnablePigxResourceServer;
import com.pig4cloud.pigx.common.swagger.annotation.EnableOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author frwcloud
 * @date 2019/01/23 定时任务模块
 */
@EnableOpenApi("job")
@EnablePigxFeignClients
@EnablePigxResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class PigxDaemonQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxDaemonQuartzApplication.class, args);
	}

}
