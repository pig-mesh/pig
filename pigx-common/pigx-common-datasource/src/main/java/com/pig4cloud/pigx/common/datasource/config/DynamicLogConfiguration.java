package com.pig4cloud.pigx.common.datasource.config;

import com.pig4cloud.pigx.common.core.factory.YamlPropertySourceFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.PropertySource;

/**
 * @author lengleng
 * @date 2022/8/8
 *
 * 注入SQL 格式化的插件
 */
@ConditionalOnClass(name = "com.pig4cloud.pigx.common.data.mybatis.DruidSqlLogFilter")
@PropertySource(value = "classpath:dynamic-ds-log.yaml", factory = YamlPropertySourceFactory.class)
public class DynamicLogConfiguration {

}
