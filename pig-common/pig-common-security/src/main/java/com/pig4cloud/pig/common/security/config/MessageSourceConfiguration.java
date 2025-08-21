package com.pig4cloud.pig.common.security.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * SA Token 核心配置
 *
 * @author lengleng
 * @date 2024/07/23
 */
@Configuration(proxyBeanMethods = false)
public class MessageSourceConfiguration {

	/**
	 * 安全异常消息源
	 * @return {@link MessageSource }
	 */
	@Bean
	public MessageSource securityMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/errors/messages");
		return messageSource;
	}

}
