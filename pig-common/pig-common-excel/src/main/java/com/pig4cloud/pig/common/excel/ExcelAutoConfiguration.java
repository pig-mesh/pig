package com.pig4cloud.pig.common.excel;

import com.pig4cloud.pig.common.excel.provider.RemoteDictDataProvider;
import com.pig4cloud.plugin.excel.handler.DictDataProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * excel 自动装配类
 *
 * @author lengleng
 * @date 2024/9/1
 */
@AutoConfiguration
public class ExcelAutoConfiguration {

	/**
	 * dict 数据提供程序
	 * @param restTemplate REST 模板
	 * @return {@link DictDataProvider }
	 */
	@Bean
	@ConditionalOnMissingBean
	public DictDataProvider dictDataProvider(RestTemplate restTemplate) {
		return new RemoteDictDataProvider(restTemplate);
	}

}
