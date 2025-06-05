package com.pig4cloud.pig.common.swagger.config;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * OpenAPI 元数据配置类，用于配置并注册OpenAPI相关元数据
 *
 * @author lengleng
 * @date 2025/05/31
 */
public class OpenAPIMetadataConfiguration implements InitializingBean, ApplicationContextAware {

	/**
	 * 应用上下文
	 */
	private ApplicationContext applicationContext;

	@Setter
	private String path;

	/**
	 * 在属性设置完成后执行，将spring-doc路径信息注册到ServiceInstance的元数据中
	 * @throws Exception 如果执行过程中发生错误
	 */
	@Override
	public void afterPropertiesSet() throws Exception {

		String[] beanNamesForType = applicationContext.getBeanNamesForType(ServiceInstance.class);

		if (beanNamesForType.length == 0) {
			return;
		}

		ServiceInstance serviceInstance = applicationContext.getBean(ServiceInstance.class);
		serviceInstance.getMetadata().put("spring-doc", path);
	}

	/**
	 * 设置应用上下文
	 * @param applicationContext 应用上下文对象
	 * @throws BeansException 如果设置上下文时发生错误
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
