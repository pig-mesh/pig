package com.pig4cloud.pig.common.datasource.config;

import com.baomidou.dynamic.datasource.creator.druid.DruidConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lengleng
 * @date 2019-05-14
 * <p>
 * 参考DruidDataSourceWrapper
 */
@Data
@ConfigurationProperties("spring.datasource.druid")
public class DruidDataSourceProperties extends DruidConfig {

	/**
	 * 数据源用户名
	 */
	private String username;

	/**
	 * 数据源密码
	 */
	private String password;

	/**
	 * jdbcurl
	 */
	private String url;

	/**
	 * 数据源驱动
	 */
	private String driverClassName;

	/**
	 * 查询数据源的 SQL（使用参数化查询防止 SQL 注入）
	 */
	private String queryDsSql = "select * from gen_datasource_conf where del_flag = ?";

}
