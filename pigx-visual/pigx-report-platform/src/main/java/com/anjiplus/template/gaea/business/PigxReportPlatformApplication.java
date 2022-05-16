package com.anjiplus.template.gaea.business;

import com.anji.plus.gaea.annotation.enabled.EnabledGaeaConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 业务模板
 *
 * @author lr
 * @since 2021-02-03
 */
@EnabledGaeaConfiguration
@SpringBootApplication(scanBasePackages = { "com.anjiplus.template.gaea", "com.anji.plus" })
@MapperScan(basePackages = { "com.anjiplus.template.gaea.business.modules.*.dao",
		"com.anjiplus.template.gaea.business.modules.*.**.dao", "com.anji.plus.gaea.*.module.*.dao" })
public class PigxReportPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(PigxReportPlatformApplication.class);
	}

}
