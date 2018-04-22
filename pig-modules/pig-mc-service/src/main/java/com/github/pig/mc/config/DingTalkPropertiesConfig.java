package com.github.pig.mc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lengleng
 * @date 2018/4/2
 * 钉钉服务配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sms.dingtalk")
public class DingTalkPropertiesConfig {
    /**
     * webhook
     */
    private String webhook;

}
