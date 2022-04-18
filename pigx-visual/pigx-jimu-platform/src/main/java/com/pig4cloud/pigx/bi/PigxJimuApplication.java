/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.bi;

import com.pig4cloud.pigx.common.feign.annotation.EnablePigxFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author lengleng
 * @date 2022-04-06
 * <p>
 */
@EnablePigxFeignClients
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = { "org.jeecg.modules.jmreport", "com.pig4cloud.pigx.bi" },
		exclude = MongoAutoConfiguration.class)
public class PigxJimuApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxJimuApplication.class, args);
	}

}
