package com.pig4cloud.pig.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import zipkin.storage.mysql.MySQLStorage;
import zipkin2.server.internal.EnableZipkinServer;

import javax.sql.DataSource;

/**
 * 服务链路追踪
 *
 * @author lishangbu
 * @date 2019/2/23
 */
@EnableZipkinServer
@SpringCloudApplication
public class PigZipkinApplication {
	public static void main(String[] args) {
		SpringApplication.run(PigZipkinApplication.class, args);
	}

	@Bean
	public MySQLStorage mySQLStorage(DataSource datasource) {
		return MySQLStorage.builder().datasource(datasource).executor(Runnable::run).build();
	}
}
