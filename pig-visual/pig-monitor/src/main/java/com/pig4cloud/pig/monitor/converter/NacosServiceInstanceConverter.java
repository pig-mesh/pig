package com.pig4cloud.pig.monitor.converter;

import de.codecentric.boot.admin.server.cloud.discovery.DefaultServiceInstanceConverter;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;

/**
 * Nacos 2.x 服务注册转换器，用于处理服务实例元数据转换
 *
 * @author lengleng
 * @date 2025/05/31
 */
@Configuration(proxyBeanMethods = false)
public class NacosServiceInstanceConverter extends DefaultServiceInstanceConverter {

	/**
	 * 获取服务实例的元数据
	 * @param instance 服务实例
	 * @return 过滤后的元数据映射，不包含空键或空值的条目
	 */
	@Override
	protected Map<String, String> getMetadata(ServiceInstance instance) {
		return (instance.getMetadata() != null) ? instance.getMetadata()
			.entrySet()
			.stream()
			.filter((e) -> e.getKey() != null && e.getValue() != null)
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)) : emptyMap();
	}

}
