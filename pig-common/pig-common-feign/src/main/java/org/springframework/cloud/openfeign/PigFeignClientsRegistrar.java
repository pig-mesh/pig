/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package org.springframework.cloud.openfeign;

import com.pig4cloud.pig.common.feign.PigFeignAutoConfiguration;
import lombok.Getter;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author L.cm
 * @date 2020/2/8
 * <p>
 * feign 自动配置功能 from mica
 */
public class PigFeignClientsRegistrar implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware, EnvironmentAware {

	@Getter
	private ClassLoader beanClassLoader;

	@Getter
	private Environment environment;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
		registerFeignClients(registry);
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	private void registerFeignClients(BeanDefinitionRegistry registry) {
		List<String> feignClients = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
				getBeanClassLoader());
		// 如果 spring.factories 里为空
		if (feignClients.isEmpty()) {
			return;
		}
		for (String className : feignClients) {
			try {
				Class<?> clazz = beanClassLoader.loadClass(className);
				AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(clazz,
						FeignClient.class);
				if (attributes == null) {
					continue;
				}
				// 如果已经存在该 bean，支持原生的 Feign
				if (registry.containsBeanDefinition(className)) {
					continue;
				}
				registerClientConfiguration(registry, getClientName(attributes), attributes.get("configuration"));

				validate(attributes);
				BeanDefinitionBuilder definition = BeanDefinitionBuilder
						.genericBeanDefinition(FeignClientFactoryBean.class);
				definition.addPropertyValue("url", getUrl(attributes));
				definition.addPropertyValue("path", getPath(attributes));
				String name = getName(attributes);
				definition.addPropertyValue("name", name);

				// 兼容最新版本的 spring-cloud-openfeign，尚未发布
				StringBuilder aliasBuilder = new StringBuilder(18);
				if (attributes.containsKey("contextId")) {
					String contextId = getContextId(attributes);
					aliasBuilder.append(contextId);
					definition.addPropertyValue("contextId", contextId);
				}
				else {
					aliasBuilder.append(name);
				}

				definition.addPropertyValue("type", className);
				definition.addPropertyValue("decode404", attributes.get("decode404"));
				definition.addPropertyValue("fallback", attributes.get("fallback"));
				definition.addPropertyValue("fallbackFactory", attributes.get("fallbackFactory"));
				definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);

				AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();

				// alias
				String alias = aliasBuilder.append("FeignClient").toString();

				// has a default, won't be null
				boolean primary = (Boolean) attributes.get("primary");

				beanDefinition.setPrimary(primary);

				String qualifier = getQualifier(attributes);
				if (StringUtils.hasText(qualifier)) {
					alias = qualifier;
				}

				BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className,
						new String[] { alias });
				BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);

			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Return the class used by {@link SpringFactoriesLoader} to load configuration
	 * candidates.
	 * @return the factory class
	 */
	private Class<?> getSpringFactoriesLoaderFactoryClass() {
		return PigFeignAutoConfiguration.class;
	}

	private void validate(Map<String, Object> attributes) {
		AnnotationAttributes annotation = AnnotationAttributes.fromMap(attributes);
		// This blows up if an aliased property is overspecified
		FeignClientsRegistrar.validateFallback(annotation.getClass("fallback"));
		FeignClientsRegistrar.validateFallbackFactory(annotation.getClass("fallbackFactory"));
	}

	private String getName(Map<String, Object> attributes) {
		String name = (String) attributes.get("serviceId");
		if (!StringUtils.hasText(name)) {
			name = (String) attributes.get("name");
		}
		if (!StringUtils.hasText(name)) {
			name = (String) attributes.get("value");
		}
		name = resolve(name);
		return FeignClientsRegistrar.getName(name);
	}

	private String getContextId(Map<String, Object> attributes) {
		String contextId = (String) attributes.get("contextId");
		if (!StringUtils.hasText(contextId)) {
			return getName(attributes);
		}

		contextId = resolve(contextId);
		return FeignClientsRegistrar.getName(contextId);
	}

	private String resolve(String value) {
		if (StringUtils.hasText(value)) {
			return this.environment.resolvePlaceholders(value);
		}
		return value;
	}

	private String getUrl(Map<String, Object> attributes) {
		String url = resolve((String) attributes.get("url"));
		return FeignClientsRegistrar.getUrl(url);
	}

	private String getPath(Map<String, Object> attributes) {
		String path = resolve((String) attributes.get("path"));
		return FeignClientsRegistrar.getPath(path);
	}

	@Nullable
	private String getQualifier(@Nullable Map<String, Object> client) {
		if (client == null) {
			return null;
		}
		String qualifier = (String) client.get("qualifier");
		if (StringUtils.hasText(qualifier)) {
			return qualifier;
		}
		return null;
	}

	@Nullable
	private String getClientName(@Nullable Map<String, Object> client) {
		if (client == null) {
			return null;
		}
		String value = (String) client.get("contextId");
		if (!StringUtils.hasText(value)) {
			value = (String) client.get("value");
		}
		if (!StringUtils.hasText(value)) {
			value = (String) client.get("name");
		}
		if (!StringUtils.hasText(value)) {
			value = (String) client.get("serviceId");
		}
		if (StringUtils.hasText(value)) {
			return value;
		}

		throw new IllegalStateException(
				"Either 'name' or 'value' must be provided in @" + FeignClient.class.getSimpleName());
	}

	private void registerClientConfiguration(BeanDefinitionRegistry registry, Object name, Object configuration) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(FeignClientSpecification.class);
		builder.addConstructorArgValue(name);
		builder.addConstructorArgValue(configuration);
		registry.registerBeanDefinition(name + "." + FeignClientSpecification.class.getSimpleName(),
				builder.getBeanDefinition());
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
