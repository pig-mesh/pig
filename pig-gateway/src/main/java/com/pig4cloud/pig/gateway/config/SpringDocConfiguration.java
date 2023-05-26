package com.pig4cloud.pig.gateway.config;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lengleng
 * @date 2022/3/26
 * <p>
 * swagger 3.0 展示
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SpringDocConfiguration implements InitializingBean {

	private final SwaggerUiConfigProperties swaggerUiConfigProperties;

	private final DiscoveryClient discoveryClient;

	@Override
	public void afterPropertiesSet() throws Exception {
		Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrlSet = new HashSet<>();

		for (String serviceId : discoveryClient.getServices()) {
			for (ServiceInstance instance : discoveryClient.getInstances(serviceId)) {
				String doc = instance.getMetadata().get("spring-doc");
				if (StrUtil.isNotBlank(doc)) {
					AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
					swaggerUrl.setName(serviceId);
					swaggerUrl.setUrl(String.format("/%s/v3/api-docs", doc));
					swaggerUrlSet.add(swaggerUrl);
				}
			}
		}

		swaggerUiConfigProperties.setUrls(swaggerUrlSet);

	}

}
