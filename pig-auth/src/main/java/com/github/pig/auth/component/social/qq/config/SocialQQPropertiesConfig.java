package com.github.pig.auth.component.social.qq.config;

import com.github.pig.auth.config.SocialPropertiesConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lengleng
 * @date 2018/1/19
 * QQ登录参数配置
 */
@Configuration
@ConfigurationProperties(prefix = "pig.social.qq")
public class SocialQQPropertiesConfig extends SocialPropertiesConfig {
}
