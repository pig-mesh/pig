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
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pig.common.sentinel.handle;

import com.pig4cloud.pig.common.core.constant.CommonConstants;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.BadSqlGrammarException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link GlobalBizExceptionHandler} 数据库异常脱敏测试
 *
 * @author cgrs5722
 */
class GlobalBizExceptionHandlerTests {

	private final GlobalBizExceptionHandler handler = new GlobalBizExceptionHandler();

	/**
	 * MyBatis 包装的 DataAccessException 消息携带完整 SQL 与敏感列名，必须返回固定文案而不是回显
	 */
	@Test
	void handleDataAccessExceptionMustNotExposeSql() {
		String sql = "select u.password, u.salt from sys_user u where u.username = 'admin'";
		BadSqlGrammarException exception = new BadSqlGrammarException("badSqlGrammar", sql,
				new SQLException("syntax error"));

		var result = handler.handleDataAccessException(exception);

		assertThat(result.getCode()).isEqualTo(CommonConstants.FAIL);
		assertThat(result.getMsg()).isEqualTo("数据库调用异常，请联系管理员处理");
		assertThat(result.getMsg()).doesNotContain("select").doesNotContain("password");
	}

}
