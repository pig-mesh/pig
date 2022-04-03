package com.pig4cloud.pig.gateway.config;

import lombok.Data;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2022/3/26
 * <p>
 * swagger 3.0 展示
 */
@Configuration(proxyBeanMethods = false)
public class SpringDocConfiguration {

	@Bean
	@Lazy(false)
	@ConditionalOnProperty(name = "springdoc.api-docs.enabled", matchIfMissing = true)
	public List<GroupedOpenApi> apis(SwaggerUiConfigParameters swaggerUiConfigParameters,
			SwaggerProperties properties) {
		List<GroupedOpenApi> groups = new ArrayList<>();
		for (String value : properties.services.values()) {
			swaggerUiConfigParameters.addGroup(value);
		}
		return groups;
	}

	@Data
	@Component
	@ConfigurationProperties("swagger")
	class SwaggerProperties {

		private Map<String, String> services;

	}

}
