package com.pig4cloud.pig.common.security.config;

import cn.dev33.satoken.config.SaTokenConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * SA Token 核心配置
 *
 * @author lengleng
 * @date 2024/07/23
 */
@Configuration(proxyBeanMethods = false)
public class SaTokenCoreConfiguration {

	@Autowired
	public void setSaConfig(SaTokenConfig cfg) {
		cfg.setTokenName("pig");
	}

}
