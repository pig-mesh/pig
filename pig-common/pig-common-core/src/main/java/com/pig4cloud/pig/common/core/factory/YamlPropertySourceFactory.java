package com.pig4cloud.pig.common.core.factory;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * YAML属性源工厂类：用于读取自定义YAML文件并转换为属性源
 *
 * @author lengleng
 * @date 2025/05/30
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

	/**
	 * 创建属性源
	 * @param name 属性源名称，可为空
	 * @param resource 编码资源
	 * @return 属性源对象
	 * @throws IOException 读取资源时可能抛出IO异常
	 */
	@Override
	public PropertySource<?> createPropertySource(@Nullable String name, EncodedResource resource) throws IOException {
		Properties propertiesFromYaml = loadYamlIntoProperties(resource);
		String sourceName = name != null ? name : resource.getResource().getFilename();
		return new PropertiesPropertySource(sourceName, propertiesFromYaml);
	}

	/**
	 * 将YAML资源加载为Properties对象
	 * @param resource 编码后的资源对象
	 * @return 加载后的Properties对象
	 * @throws FileNotFoundException 当资源文件不存在时抛出
	 */
	private Properties loadYamlIntoProperties(EncodedResource resource) throws FileNotFoundException {
		try {
			YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
			factory.setResources(resource.getResource());
			factory.afterPropertiesSet();
			return factory.getObject();
		}
		catch (IllegalStateException e) {
			Throwable cause = e.getCause();
			if (cause instanceof FileNotFoundException) {
				throw (FileNotFoundException) e.getCause();
			}
			throw e;
		}
	}

}
