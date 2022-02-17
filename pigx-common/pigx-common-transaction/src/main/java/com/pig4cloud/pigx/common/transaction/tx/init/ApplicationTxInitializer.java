package com.pig4cloud.pigx.common.transaction.tx.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author lengleng
 * @date 22022-02-17
 * <p>
 * 通过环境变量的形式注入 允许循环依赖
 */
public class ApplicationTxInitializer implements EnvironmentPostProcessor, Ordered {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		System.setProperty("spring.main.allow-circular-references", "true");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
