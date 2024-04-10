package com.pig4cloud.pig.daemon.quartz;

import com.pig4cloud.pig.common.security.annotation.EnablePigResourceServer;
import com.pig4cloud.pig.common.swagger.annotation.EnablePigDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author frwcloud
 * @date 2023-07-05
 */
@EnablePigDoc(value = "job")
@EnablePigResourceServer
@EnableDiscoveryClient
@SpringBootApplication
public class PigQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigQuartzApplication.class, args);
	}

}
