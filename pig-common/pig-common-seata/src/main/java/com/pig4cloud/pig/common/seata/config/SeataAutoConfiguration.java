package com.pig4cloud.pig.common.seata.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.pig4cloud.pig.common.core.factory.YamlPropertySourceFactory;

/**
 * Seata 配置类
 *
 * @author lengleng
 * @date 2022/3/29
 */
@PropertySource(value = "classpath:seata-config.yml", factory = YamlPropertySourceFactory.class)
@Configuration(proxyBeanMethods = false)
public class SeataAutoConfiguration {

}
