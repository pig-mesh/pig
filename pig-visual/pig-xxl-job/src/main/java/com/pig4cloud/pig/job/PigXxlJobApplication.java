package com.pig4cloud.pig.job;

import com.pig4cloud.pig.common.job.annotation.EnablePigXxlJob;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * pig分布式任务调度,基于xxl-job
 *
 * @author lishangbu
 * @date 2020/9/14
 */
@EnablePigXxlJob
@SpringCloudApplication
public class PigXxlJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigXxlJobApplication.class, args);
	}

}
