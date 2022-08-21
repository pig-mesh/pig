package com.anjiplus.template.gaea.business.config;

import com.anjiplus.template.gaea.business.runner.ApplicationInitRunner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * business配置类
 *
 * @author lr
 * @since 2021-04-08
 */
@Configuration
@MapperScan(basePackages = { "com.anjiplus.template.gaea.business.modules.*.dao",
		"com.anjiplus.template.gaea.business.modules.*.**.dao" })
public class BusinessAutoConfiguration {

	/**
	 * 系统启动完执行
	 * @return
	 */
	@Bean
	public ApplicationInitRunner applicationInitRunner() {
		return new ApplicationInitRunner();
	}

}
