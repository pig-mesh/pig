package com.pig4cloud.pig.common.swagger.config;

import com.pig4cloud.pig.common.swagger.annotation.EnablePigDoc;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;

/**
 * openapi 配置类
 *
 * @author lengleng
 * @date 2023/1/1
 */
public class OpenAPIDefinitionImportSelector implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(EnablePigDoc.class.getName(), true);
		Object value = annotationAttributes.get("value");
		if (Objects.isNull(value)) {
			return;
		}

		BeanDefinitionBuilder openAPIMetadataRegister = BeanDefinitionBuilder
			.genericBeanDefinition(OpenAPIMetadataRegister.class);
		openAPIMetadataRegister.addPropertyValue("path", value);

		registry.registerBeanDefinition("openAPIMetadataRegister", openAPIMetadataRegister.getBeanDefinition());

		BeanDefinitionBuilder openAPIDefinition = BeanDefinitionBuilder.genericBeanDefinition(OpenAPIDefinition.class);
		openAPIDefinition.addPropertyValue("path", value);
		registry.registerBeanDefinition("openAPIDefinition", openAPIDefinition.getBeanDefinition());

	}

}
