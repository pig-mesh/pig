package com.pig4cloud.pigx.codegen.config;

import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.pig4cloud.pigx.common.core.factory.YamlPropertySourceFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.ssssssss.magicapi.core.interceptor.ResultProvider;
import org.ssssssss.magicapi.core.resource.DatabaseResource;
import org.ssssssss.magicapi.core.resource.Resource;
import org.ssssssss.magicapi.datasource.model.MagicDynamicDataSource;

import java.util.List;

/**
 * 低代码设计相关
 *
 * @author lengleng
 * @date 2022/5/20
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@PropertySource(value = "classpath:magic/magic.yml", factory = YamlPropertySourceFactory.class)
public class MagicApiConfiguration implements InitializingBean {

	private final MagicDynamicDataSource magicDynamicDataSource;

	private final List<DynamicDataSourceProvider> providers;

	/**
	 * 定义 magic-api 存储
	 * @param jdbcTemplate
	 * @return 文件存储实现
	 */
	@Bean
	public Resource databaseResource(JdbcTemplate jdbcTemplate) {
		return new DatabaseResource(jdbcTemplate, "gen_api_file");
	}

	/**
	 * 格式化magic 结果输出
	 * @return
	 */
	@Bean
	public ResultProvider resultProvider() {
		return new RResultProvider();
	}

	@Override
	public void afterPropertiesSet() {
		for (DynamicDataSourceProvider provider : providers) {
			provider.loadDataSources().forEach(magicDynamicDataSource::add);
		}
	}

}
