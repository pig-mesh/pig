package com.pig4cloud.pigx.common.excel.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lengleng
 * @date 2020/3/29
 */
@Data
@ConfigurationProperties(prefix = ExcelConfigProperties.PREFIX)
public class ExcelConfigProperties {

	static final String PREFIX = "excel";

	/**
	 * 模板路径
	 */
	private String templatePath = "excel";

}
