package com.pig4cloud.pig.gateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pig.gateway.filter.PigRequestGlobalFilter;
import com.pig4cloud.pig.gateway.handler.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关配置
 *
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
public class GatewayConfiguration {

	/**
	 * 创建PigRequest全局过滤器
	 * @return PigRequest全局过滤器
	 */
	@Bean
	public PigRequestGlobalFilter pigRequestGlobalFilter() {
		return new PigRequestGlobalFilter();
	}

	/**
	 * 创建全局异常处理程序
	 * @param objectMapper 对象映射器
	 * @return 全局异常处理程序
	 */
	@Bean
	public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
		return new GlobalExceptionHandler(objectMapper);
	}

}
