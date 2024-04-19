package com.pig4cloud.pig.common.swagger.config;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author lengleng
 * @date 2023/1/4
 */
public class OpenAPIMetadataConfiguration implements InitializingBean, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Setter
	private String path;

	@Override
	public void afterPropertiesSet() throws Exception {
		String[] beanNamesForType = applicationContext.getBeanNamesForType(ServiceInstance.class);

		if (beanNamesForType.length == 0) {
			return;
		}

		ServiceInstance serviceInstance = applicationContext.getBean(ServiceInstance.class);
		serviceInstance.getMetadata().put("spring-doc", path);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
