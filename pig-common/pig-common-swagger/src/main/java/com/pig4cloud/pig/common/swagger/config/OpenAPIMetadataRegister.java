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
public class OpenAPIMetadataRegister implements InitializingBean, ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Setter
	private String path;

	/**
	 * 设置应用程序上下文
	 * @param applicationContext 应用程序上下文
	 * @throws BeansException 可能抛出的异常
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * 在属性设置后执行的初始化方法
	 */
	@Override
	public void afterPropertiesSet() {
		// 获取ServiceInstance实例
		ServiceInstance serviceInstance = applicationContext.getBean(ServiceInstance.class);
		// 设置元数据
		serviceInstance.getMetadata().put("spring-doc", path);
	}

}
