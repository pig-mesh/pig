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

import lombok.Getter;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.annotation.ImportCandidates;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Feign客户端注册器，用于自动配置Feign客户端
 *
 * @author lengleng
 * @date 2025/05/31
 * @see ImportBeanDefinitionRegistrar
 * @see BeanClassLoaderAware
 * @see EnvironmentAware
 */
public class PigFeignClientsRegistrar implements ImportBeanDefinitionRegistrar, BeanClassLoaderAware, EnvironmentAware {

	private final static String BASE_URL = "http://127.0.0.1:${server.port}${server.servlet.context-path}";

	@Getter
	private ClassLoader beanClassLoader;

	@Getter
	private Environment environment;

	/**
	 * 注册Bean定义
	 * @param metadata 注解元数据
	 * @param registry Bean定义注册器
	 */
	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
		registerFeignClients(registry);
	}

	/**
	 * 设置Bean类加载器
	 * @param classLoader 要设置的类加载器
	 */
	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	/**
	 * 注册Feign客户端到Spring容器
	 * @param registry Bean定义注册器，用于注册Bean定义
	 */
	private void registerFeignClients(BeanDefinitionRegistry registry) {
		List<String> feignClients = new ArrayList<>();

		// 支持 springboot 2.7 + 最新版本的配置方式
		ImportCandidates.load(FeignClient.class, getBeanClassLoader()).forEach(feignClients::add);

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

				// 如果是单体项目自动注入 & url 为空
				Boolean isMicro = environment.getProperty("spring.cloud.nacos.discovery.enabled", Boolean.class, true);
				// 如果已经存在该 bean，支持原生的 Feign
				if (registry.containsBeanDefinition(className) && isMicro) {
					continue;
				}

				registerClientConfiguration(registry, getClientName(attributes), className,
						attributes.get("configuration"));

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
				definition.addPropertyValue("dismiss404",
						Boolean.parseBoolean(String.valueOf(attributes.get("dismiss404"))));
				Object fallbackFactory = attributes.get("fallbackFactory");
				if (fallbackFactory != null) {
					definition.addPropertyValue("fallbackFactory", fallbackFactory instanceof Class ? fallbackFactory
							: ClassUtils.resolveClassName(fallbackFactory.toString(), null));
				}
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
	 * 验证Feign客户端配置属性
	 * @param attributes 配置属性Map
	 */
	private void validate(Map<String, Object> attributes) {
		AnnotationAttributes annotation = AnnotationAttributes.fromMap(attributes);
		// This blows up if an aliased property is overspecified
		FeignClientsRegistrar.validateFallback(annotation.getClass("fallback"));
		FeignClientsRegistrar.validateFallbackFactory(annotation.getClass("fallbackFactory"));
	}

	/**
	 * 从属性Map中获取名称
	 * @param attributes 属性Map
	 * @return 解析后的名称
	 */
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

	/**
	 * 获取上下文ID
	 * @param attributes 属性Map
	 * @return 解析后的上下文ID，如果contextId为空则返回名称
	 */
	private String getContextId(Map<String, Object> attributes) {
		String contextId = (String) attributes.get("contextId");
		if (!StringUtils.hasText(contextId)) {
			return getName(attributes);
		}

		contextId = resolve(contextId);
		return FeignClientsRegistrar.getName(contextId);
	}

	/**
	 * 解析字符串中的占位符
	 * @param value 待解析的字符串
	 * @return 解析后的字符串，若输入为空则返回原值
	 */
	private String resolve(String value) {
		if (StringUtils.hasText(value)) {
			return this.environment.resolvePlaceholders(value);
		}
		return value;
	}

	/**
	 * 获取URL地址
	 * @param attributes 属性集合
	 * @return 解析后的URL地址，微服务环境下返回null
	 */
	private String getUrl(Map<String, Object> attributes) {

		// 如果是单体项目自动注入 & url 为空
		Boolean isMicro = environment.getProperty("spring.cloud.nacos.discovery.enabled", Boolean.class, true);

		if (isMicro) {
			return null;
		}

		Object objUrl = attributes.get("url");

		String url = "";
		if (StringUtils.hasText(objUrl.toString())) {
			url = resolve(objUrl.toString());
		}
		else {
			url = resolve(BASE_URL);
		}

		return FeignClientsRegistrar.getUrl(url);
	}

	/**
	 * 获取路径
	 * @param attributes 属性Map，包含路径信息
	 * @return 解析后的路径
	 */
	private String getPath(Map<String, Object> attributes) {
		String path = resolve((String) attributes.get("path"));
		return FeignClientsRegistrar.getPath(path);
	}

	/**
	 * 从客户端信息中获取qualifier字段值
	 * @param client 客户端信息映射表，可能为null
	 * @return qualifier字段值，若无有效值则返回null
	 */
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

	/**
	 * 获取客户端名称，依次从contextId、value、name、serviceId字段中获取
	 * @param client 客户端信息Map，可为null
	 * @return 客户端名称，可能为null
	 * @throws IllegalStateException 当无法从client中获取到有效名称时抛出
	 */
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

	/**
	 * 注册客户端配置到BeanDefinitionRegistry
	 * @param registry Bean定义注册器
	 * @param name 客户端名称
	 * @param className 类名
	 * @param configuration 配置对象
	 */
	private void registerClientConfiguration(BeanDefinitionRegistry registry, Object name, Object className,
			Object configuration) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(FeignClientSpecification.class);
		builder.addConstructorArgValue(name);
		builder.addConstructorArgValue(className);
		builder.addConstructorArgValue(configuration);
		registry.registerBeanDefinition(name + "." + FeignClientSpecification.class.getSimpleName(),
				builder.getBeanDefinition());
	}

	/**
	 * 设置环境变量
	 * @param environment 要设置的环境变量对象
	 */
	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

}
