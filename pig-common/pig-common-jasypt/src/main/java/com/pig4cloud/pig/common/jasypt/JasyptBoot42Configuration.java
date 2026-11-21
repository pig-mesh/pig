package com.pig4cloud.pig.common.jasypt;

import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyDetector;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertySourceConverter;
import com.ulisesbocchio.jasyptspringboot.configuration.BootstrapSpringApplicationListener;
import com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesBeanFactoryPostProcessor;
import com.ulisesbocchio.jasyptspringboot.wrapper.EncryptableSystemEnvironmentPropertySourceWrapper;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.logging.LoggingSystemProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Jasypt 4.0.4 的环境后处理器仍引用 Boot 4.2 已删除的接口。
 * 保留上游属性解密和日志初始化逻辑，仅替换触发旧接口加载的调用。
 * 上游修复后应移除此适配。
 * 逻辑对应上游 jasypt-spring-boot-parent-4.0.4 的 EnableEncryptablePropertiesBeanFactoryPostProcessor。
 */
@AutoConfiguration
public class JasyptBoot42Configuration {

	@Bean
	static BeanDefinitionRegistryPostProcessor jasyptBoot42PostProcessorReplacement() {
		return new BeanDefinitionRegistryPostProcessor() {
			@Override
			public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
				String name = "enableEncryptablePropertySourcesPostProcessor";
				if (registry.containsBeanDefinition(name)
						&& name.equals(registry.getBeanDefinition(name).getFactoryMethodName())
						&& "com.ulisesbocchio.jasyptspringboot.configuration.EnableEncryptablePropertiesConfiguration"
								.equals(registry.getBeanDefinition(name).getBeanClassName())) {
					RootBeanDefinition replacement = new RootBeanDefinition(Boot42EncryptablePostProcessor.class);
					replacement.setAutowireMode(RootBeanDefinition.AUTOWIRE_CONSTRUCTOR);
					registry.removeBeanDefinition(name);
					registry.registerBeanDefinition(name, replacement);
				}
			}
		};
	}

	static final class Boot42EncryptablePostProcessor extends EnableEncryptablePropertiesBeanFactoryPostProcessor {

		private final ConfigurableEnvironment environment;

		private final EncryptablePropertySourceConverter converter;

		private ApplicationContext applicationContext;

		Boot42EncryptablePostProcessor(ConfigurableEnvironment environment, EncryptablePropertySourceConverter converter) {
			super(environment, converter);
			this.environment = environment;
			this.converter = converter;
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) {
			this.applicationContext = applicationContext;
		}

		@Override
		public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
			converter.convertPropertySources(environment.getPropertySources());
			var source = environment.getPropertySources().get(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME);
			if (source instanceof EncryptableSystemEnvironmentPropertySourceWrapper wrapper) {
				// Boot 4.2 绑定环境变量时会复制整张 Map。复制时调用 Jasypt 会递归读取环境。
				// 单独提供已解密的环境变量，让 Boot 继续按原规则绑定变量名和列表下标。
				var detector = beanFactory.getBean("lazyEncryptablePropertyDetector", EncryptablePropertyDetector.class);
				Map<String, Object> decrypted = new LinkedHashMap<>();
				wrapper.getSource().forEach((key, value) -> {
					if (value instanceof String text && detector.isEncrypted(text)) {
						decrypted.put(key, wrapper.getProperty(key));
					}
				});
				if (!decrypted.isEmpty()) {
					environment.getPropertySources().addBefore(source.getName(),
							new SystemEnvironmentPropertySource("jasypt-" + source.getName(), decrypted));
				}
			}
			// 与 Jasypt 4.0.4 一致：让日志配置重新读取已解密的属性。
			var event = BootstrapSpringApplicationListener.getApplicationEnvironmentPreparedEvent();
			if (event != null && applicationContext instanceof AbstractApplicationContext context) {
				context.getApplicationListeners().stream().filter(LoggingApplicationListener.class::isInstance)
						.map(LoggingApplicationListener.class::cast).findFirst().ifPresent(listener -> {
							LoggingSystem.get(event.getSpringApplication().getClassLoader()).cleanUp();
							for (LoggingSystemProperty property : LoggingSystemProperty.values()) {
								System.clearProperty(property.getEnvironmentVariableName());
							}
							listener.onApplicationEvent(event);
						});
			}
		}

	}

}
