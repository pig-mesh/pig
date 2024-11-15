package com.pig4cloud.pig.common.security.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaTokenConfig;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SaTokenCoreConfiguration implements InitializingBean {

	@Autowired
	public void setSaConfig(SaTokenConfig cfg) {
		cfg.setTokenName(SecurityConstants.PROJECT_PREFIX);
	}

	/**
	 * 安全异常消息源
	 *
	 * @return {@link MessageSource }
	 */
	@Bean
	public MessageSource securityMessageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:i18n/errors/messages");
		return messageSource;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 允许同时在线
		SaManager.getConfig().setIsConcurrent(true);
		// 不允许复用 token
		SaManager.getConfig().setIsShare(false);
		// 关闭打印日志
		SaManager.getConfig().setIsPrint(false);
	}



}
