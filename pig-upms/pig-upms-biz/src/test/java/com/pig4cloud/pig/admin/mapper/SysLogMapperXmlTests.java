/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pig.admin.mapper;

import com.pig4cloud.pig.common.data.mybatis.MybatisPlusConfiguration;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.session.Configuration;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SysLogMapperXmlTests {

	private static final String MAPPER_RESOURCE = "mapper/SysLogMapper.xml";

	private static final String STATEMENT_ID = "com.pig4cloud.pig.admin.mapper.SysLogMapper.selectLogSum";

	@Test
	void selectLogSumUsesDialectSpecificDateBucketsAndExcludesDeletedLogs() throws IOException {
		assertThat(sql(null)).contains("DATE(create_time)").contains("del_flag = '0'");
		assertThat(sql("oracle")).contains("TRUNC(create_time)").contains("del_flag = '0'");
		assertThat(sql("mssql")).contains("CAST(create_time AS date)").contains("del_flag = '0'");
		assertThat(sql("dm")).contains("TO_CHAR(create_time, 'YYYY-MM-DD')").contains("del_flag = '0'");
	}

	@Test
	void databaseIdProviderRecognizesSupportedDialectProductNames() throws SQLException {
		DatabaseIdProvider provider = new MybatisPlusConfiguration().databaseIdProvider();

		assertThat(databaseId(provider, "Oracle")).isEqualTo("oracle");
		assertThat(databaseId(provider, "Microsoft SQL Server")).isEqualTo("mssql");
		assertThat(databaseId(provider, "DM DBMS")).isEqualTo("dm");
	}

	private String sql(String databaseId) throws IOException {
		Configuration configuration = new Configuration();
		configuration.setDatabaseId(databaseId);
		try (InputStream inputStream = Resources.getResourceAsStream(MAPPER_RESOURCE)) {
			XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(inputStream, configuration, MAPPER_RESOURCE,
					configuration.getSqlFragments());
			mapperBuilder.parse();
		}
		return configuration.getMappedStatement(STATEMENT_ID)
			.getBoundSql(Map.of("startTime", LocalDateTime.now()))
			.getSql()
			.replaceAll("\\s+", " ");
	}

	private String databaseId(DatabaseIdProvider provider, String productName) throws SQLException {
		DataSource dataSource = mock(DataSource.class);
		Connection connection = mock(Connection.class);
		DatabaseMetaData metadata = mock(DatabaseMetaData.class);
		when(dataSource.getConnection()).thenReturn(connection);
		when(connection.getMetaData()).thenReturn(metadata);
		when(metadata.getDatabaseProductName()).thenReturn(productName);
		return provider.getDatabaseId(dataSource);
	}

}
