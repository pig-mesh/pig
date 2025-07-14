/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pig.common.datasource.config;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.pig4cloud.pig.common.datasource.support.DataSourceConstants;
import com.pig4cloud.pig.common.datasource.util.DsConfTypeEnum;
import com.pig4cloud.pig.common.datasource.util.DsJdbcUrlEnum;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * JDBC动态数据源提供者：从数据源中获取配置信息
 *
 * @author lengleng
 * @date 2025/07/14
 */
@Slf4j
public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

	private final DataSourceProperties properties;

	private final StringEncryptor stringEncryptor;

	public JdbcDynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator,
			StringEncryptor stringEncryptor, DataSourceProperties properties) {
		super(defaultDataSourceCreator, properties.getDriverClassName(), properties.getUrl(), properties.getUsername(),
				properties.getPassword());
		this.stringEncryptor = stringEncryptor;
		this.properties = properties;
	}

	/**
	 * 执行语句获得数据源参数
	 * @param statement 语句
	 * @return 数据源参数
	 * @throws SQLException sql异常
	 */
	@Override
	protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {

		Map<String, DataSourceProperty> map = new HashMap<>(8);

		try {
			ResultSet rs = statement.executeQuery(properties.getQueryDsSql());

			while (rs.next()) {
				String name = rs.getString(DataSourceConstants.NAME);
				String username = rs.getString(DataSourceConstants.DS_USER_NAME);
				String password = rs.getString(DataSourceConstants.DS_USER_PWD);
				Integer confType = rs.getInt(DataSourceConstants.DS_CONFIG_TYPE);
				String dsType = rs.getString(DataSourceConstants.DS_TYPE);

				DataSourceProperty property = new DataSourceProperty();
				property.setUsername(username);
				property.setPassword(stringEncryptor.decrypt(password));

				String url;
				// JDBC 配置形式
				DsJdbcUrlEnum urlEnum = DsJdbcUrlEnum.get(dsType);
				if (DsConfTypeEnum.JDBC.getType().equals(confType)) {
					url = rs.getString(DataSourceConstants.DS_JDBC_URL);
				}
				else {
					String host = rs.getString(DataSourceConstants.DS_HOST);
					String port = rs.getString(DataSourceConstants.DS_PORT);
					String dsName = rs.getString(DataSourceConstants.DS_NAME);
					url = String.format(urlEnum.getUrl(), host, port, dsName);
				}
				property.setUrl(url);
				map.put(name, property);
			}

		}
		catch (Exception e) {
			log.warn("动态数据源配置表异常:{}", e.getMessage());
		}

		return map;
	}

}
