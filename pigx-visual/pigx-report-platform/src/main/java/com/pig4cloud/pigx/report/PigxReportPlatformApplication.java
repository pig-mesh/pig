package com.pig4cloud.pigx.report;

import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 业务模板
 *
 * @author lr
 * @since 2023-04-05
 */
@EnablePigxFeignClients
@SpringBootApplication
public class PigxReportPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxReportPlatformApplication.class);
	}

}
