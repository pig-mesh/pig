package com.pig4cloud.pig.gateway.config;

import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.NotifyCenter;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.alibaba.nacos.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2022/3/26
 * <p>
 * swagger 3.0 展示
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
@ConditionalOnProperty(name = "springdoc.api-docs.enabled", matchIfMissing = true)
public class SpringDocConfiguration implements InitializingBean {

	private final SwaggerUiConfigProperties swaggerUiConfigProperties;

	private final DiscoveryClient discoveryClient;

	/**
	 * 在初始化后调用的方法，用于注册SwaggerDocRegister订阅器
	 */
	@Override
	public void afterPropertiesSet() {
		SwaggerDocRegister swaggerDocRegister = new SwaggerDocRegister(swaggerUiConfigProperties, discoveryClient);
		// 手动调用一次，避免监听事件掉线问题
		swaggerDocRegister.onEvent(null);
		NotifyCenter.registerSubscriber(swaggerDocRegister);
	}

}

/**
 * Swagger文档注册器，继承自Subscriber<InstancesChangeEvent>
 */
@RequiredArgsConstructor
class SwaggerDocRegister extends Subscriber<InstancesChangeEvent> {

	private final SwaggerUiConfigProperties swaggerUiConfigProperties;

	private final DiscoveryClient discoveryClient;

	/**
	 * 事件回调方法，处理InstancesChangeEvent事件
	 * @param event 事件对象
	 */
	@Override
	public void onEvent(InstancesChangeEvent event) {
		Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> swaggerUrlSet = discoveryClient.getServices()
			.stream()
			.flatMap(serviceId -> discoveryClient.getInstances(serviceId).stream())
			.filter(instance -> StringUtils.isNotBlank(instance.getMetadata().get("spring-doc")))
			.map(instance -> {
				AbstractSwaggerUiConfigProperties.SwaggerUrl swaggerUrl = new AbstractSwaggerUiConfigProperties.SwaggerUrl();
				swaggerUrl.setName(instance.getServiceId());
				swaggerUrl.setUrl(String.format("/%s/v3/api-docs", instance.getMetadata().get("spring-doc")));
				return swaggerUrl;
			})
			.collect(Collectors.toSet());

		swaggerUiConfigProperties.setUrls(swaggerUrlSet);
	}

	/**
	 * 订阅类型方法，返回订阅的事件类型
	 * @return 订阅的事件类型
	 */
	@Override
	public Class<? extends Event> subscribeType() {
		return InstancesChangeEvent.class;
	}

}
