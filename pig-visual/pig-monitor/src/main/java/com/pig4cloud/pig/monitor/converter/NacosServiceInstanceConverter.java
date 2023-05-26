package com.pig4cloud.pig.monitor.converter;

import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;

/**
 * 针对 nacos 2.x 服务注册处理
 *
 * @author lengleng
 * @date 2021/12/20
 */
@Configuration(proxyBeanMethods = false)
public class NacosServiceInstanceConverter extends DefaultServiceInstanceConverter {

	@Override
	protected Map<String, String> getMetadata(ServiceInstance instance) {
		return (instance.getMetadata() != null) ? instance.getMetadata()
			.entrySet()
			.stream()
			.filter((e) -> e.getKey() != null && e.getValue() != null)
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : emptyMap();
	}

}
