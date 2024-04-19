package com.pig4cloud.pigx.common.log.init;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author lengleng
 * @date 2019-05-22
 * <p>
 * 通过环境变量的形式注入 logging.file 自动维护 Spring Boot Admin Logger Viewer
 */
public class ApplicationLoggerInitializer implements EnvironmentPostProcessor, Ordered {

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		String appName = environment.getProperty("spring.application.name");
		String logBase = environment.getProperty("LOGGING_PATH", "logs");

		// spring boot admin 直接加载日志
		System.setProperty("logging.file.name", String.format("%s/%s/debug.log", logBase, appName));
		// 避免 sentinel 1.8.4+ 心跳日志过大
		System.setProperty("csp.sentinel.log.level", "OFF");
		// 避免各种依赖的地方组件造成 BeanPostProcessorChecker 警告
		System.setProperty("logging.level.org.springframework.context.support.PostProcessorRegistrationDelegate",
				"ERROR");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

}
