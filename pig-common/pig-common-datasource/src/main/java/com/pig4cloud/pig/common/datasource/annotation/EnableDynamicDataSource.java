package com.pig4cloud.pig.common.datasource.annotation;

import com.pig4cloud.pig.common.datasource.DynamicDataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启动态数据源注解
 * <p>
 * 用于启用动态数据源自动配置功能
 *
 * @author lengleng
 * @date 2025/07/14
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(DynamicDataSourceAutoConfiguration.class)
public @interface EnableDynamicDataSource {

}
