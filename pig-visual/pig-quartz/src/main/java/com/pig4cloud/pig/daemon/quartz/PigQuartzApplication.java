package com.pig4cloud.pig.daemon.quartz;

import com.pig4cloud.pig.common.feign.annotation.EnablePigFeignClients;
import com.pig4cloud.pig.common.security.annotation.EnablePigResourceServer;
import com.pig4cloud.pig.common.swagger.annotation.EnablePigDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * PigQuartz应用启动类
 * <p>
 * 集成定时任务、Feign客户端、资源服务及服务发现功能
 *
 * @author lengleng
 * @author frwcloud
 * @date 2025/05/31
 */
@EnablePigDoc("job")
@EnablePigFeignClients
@EnablePigResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class PigQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigQuartzApplication.class, args);
	}

}
