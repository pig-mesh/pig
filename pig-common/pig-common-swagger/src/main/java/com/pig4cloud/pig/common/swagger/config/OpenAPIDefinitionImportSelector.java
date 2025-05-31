package com.pig4cloud.pig.common.swagger.config;

import com.pig4cloud.pig.common.swagger.annotation.EnablePigDoc;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * OpenAPI 配置类，用于动态注册 OpenAPI 相关 Bean 定义
 *
 * @author lengleng
 * @date 2025/05/31
 */
public class OpenAPIDefinitionImportSelector implements ImportBeanDefinitionRegistrar {

	/**
	 * 注册Bean定义，根据注解元数据配置OpenAPI相关Bean
	 * @param metadata 注解元数据
	 * @param registry Bean定义注册器
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnablePigDoc.class.getName(), true);
		Object value = annotationAttributes.get("value");
		if (Objects.isNull(value)) {
			return;
		}

		BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(OpenAPIDefinition.class);
		definition.addPropertyValue("path", value);
		definition.setPrimary(true);

		registry.registerBeanDefinition("openAPIDefinition", definition.getBeanDefinition());

		// 如果是微服务架构则，引入了服务发现声明相关的元数据配置
		Object isMicro = annotationAttributes.getOrDefault("isMicro", true);
		if (isMicro.equals(false)) {
			return;
		}

		BeanDefinitionBuilder openAPIMetadata = BeanDefinitionBuilder
			.genericBeanDefinition(OpenAPIMetadataConfiguration.class);
		openAPIMetadata.addPropertyValue("path", value);
		registry.registerBeanDefinition("openAPIMetadata", openAPIMetadata.getBeanDefinition());
	}

}
