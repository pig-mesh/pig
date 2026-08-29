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
package com.pig4cloud.pig.codegen.service.impl;

import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.pig4cloud.pig.codegen.entity.GenDatasourceConf;
import com.pig4cloud.pig.codegen.util.JdbcUrlSecurityValidator;
import com.pig4cloud.pig.common.core.exception.CheckedException;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * 数据源配置安全校验测试。
 *
 * @author lengleng
 */
class GenDatasourceConfServiceImplTests {

	@ParameterizedTest
	@ValueSource(strings = { "allowLoadLocalInfile=true", "allowUrlInLocalInfile=true", "allowLoadLocalInfileInPath=/",
			"ALLOWLOADLOCALINFILE=true", "allow%4CoadLocalInfile=true" })
	void saveDsByEncShouldRejectUnsafeMysqlParametersBeforeConnecting(String query) {
		GenDatasourceConfServiceImpl service = new GenDatasourceConfServiceImpl(mock(StringEncryptor.class),
				mock(DataSourceCreator.class));
		GenDatasourceConf conf = new GenDatasourceConf();
		conf.setName("unsafe");
		conf.setDsType("mysql");
		conf.setConfType(1);
		conf.setUrl("jdbc:mysql://127.0.0.1:1/test?" + query);
		conf.setUsername("test");
		conf.setPassword("test");

		assertThatThrownBy(() -> service.saveDsByEnc(conf)).isInstanceOf(CheckedException.class)
			.hasMessage("JDBC URL 包含不安全参数");
	}

	@ParameterizedTest
	@ValueSource(strings = { "allowLoadLocalInfile=true", "allowUrlInLocalInfile=true", "allowLoadLocalInfileInPath=/",
			"ALLOWLOADLOCALINFILE=true", "allow%4CoadLocalInfile=true" })
	void addDynamicDataSourceShouldRejectUnsafeUrlBeforeCreatingDataSource(String query) {
		DataSourceCreator dataSourceCreator = mock(DataSourceCreator.class);
		GenDatasourceConfServiceImpl service = new GenDatasourceConfServiceImpl(mock(StringEncryptor.class),
				dataSourceCreator);
		GenDatasourceConf conf = new GenDatasourceConf();
		conf.setDsType("mysql");
		conf.setUrl("jdbc:mysql://127.0.0.1:3306/test?" + query);

		assertThatThrownBy(() -> service.addDynamicDataSource(conf)).isInstanceOf(CheckedException.class)
			.hasMessage("JDBC URL 包含不安全参数");
		verifyNoInteractions(dataSourceCreator);
	}

	@ParameterizedTest
	@ValueSource(strings = { "jdbc:mysql://127.0.0.1:3306/test?allowLoadLocalInfile=true",
			"jdbc:mysql://127.0.0.1:3306/test?allowUrlInLocalInfile=true",
			"jdbc:mysql://127.0.0.1:3306/test?allowLoadLocalInfileInPath=/",
			"jdbc:mysql://127.0.0.1:3306/test?propertiesTransform=example.UnsafeTransform",
			"jdbc:mysql://127.0.0.1:3306/test?serverRSAPublicKeyFile=/tmp/server-key.pem",
			"jdbc:mysql://address=(host=127.0.0.1)(allowLoadLocalInfile=true)/test",
			"jdbc:mysql://(host=127.0.0.1,allowUrlInLocalInfile=true)/test",
			"jdbc:postgresql://127.0.0.1:5432/test?socketFactory=example.UnsafeSocketFactory",
			"jdbc:postgresql://127.0.0.1:5432/test?service=unsafe-service",
			"jdbc:postgresql://127.0.0.1:5432/test?sslkey=/tmp/client-key.pk8",
			"jdbc:highgo://127.0.0.1:5866/test?xmlFactoryFactory=LEGACY_INSECURE",
			"jdbc:highgo://127.0.0.1:5866/test?bulkloadCtlFile=/tmp/load.ctl",
			"jdbc:sqlserver://127.0.0.1:1433;trustManagerClass=example.UnsafeTrustManager",
			"jdbc:sqlserver://127.0.0.1:1433;clientCertificate=/tmp/client.pem",
			"jdbc:oracle:thin:@127.0.0.1:1521:test?TNS_ADMIN=/tmp/wallet",
			"jdbc:oracle:thin:@127.0.0.1:1521:test?oracle.net.wallet_location=/tmp/wallet",
			"jdbc:db2://127.0.0.1:50000/test:pluginClassName=example.UnsafePlugin;",
			"jdbc:db2://127.0.0.1:50000/test:traceFile=/tmp/db2.log;",
			"jdbc:dm://127.0.0.1:5236/test?customFilter=example.UnsafeFilter" })
	void validateJdbcUrlShouldRejectUnsafeParameters(String url) {
		assertThatThrownBy(() -> JdbcUrlSecurityValidator.validate(url)).isInstanceOf(CheckedException.class)
			.hasMessage("JDBC URL 包含不安全参数");
	}

	@ParameterizedTest
	@ValueSource(strings = { "jdbc:mysql://127.0.0.1:3306/test?characterEncoding=utf8&allowMultiQueries=true",
			"jdbc:postgresql://127.0.0.1:5432/test?ssl=true", "jdbc:highgo://127.0.0.1:5866/test?connectTimeout=10",
			"jdbc:sqlserver://127.0.0.1:1433;databaseName=test;encrypt=true", "jdbc:oracle:thin:@127.0.0.1:1521:test",
			"jdbc:db2://127.0.0.1:50000/test:user=test;", "jdbc:dm://127.0.0.1:5236/test?schema=test",
			"jdbc:mysql://127.0.0.1:3306/test?note=allowLoadLocalInfile%3Dtrue" })
	void validateJdbcUrlShouldAllowSafeParameters(String url) {
		assertThatCode(() -> JdbcUrlSecurityValidator.validate(url)).doesNotThrowAnyException();
	}

}
