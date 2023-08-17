package com.pig4cloud.pig.common.datasource.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author lengleng
 * @date 2020/12/11
 * <p>
 * jdbc-url
 */
@Getter
@AllArgsConstructor
public enum DsJdbcUrlEnum {

	/**
	 * mysql 数据库
	 */
	MYSQL("mysql",
			"jdbc:mysql://%s:%s/%s?characterEncoding=utf8"
					+ "&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true"
					+ "&useLegacyDatetimeCode=false&allowMultiQueries=true&allowPublicKeyRetrieval=true",
			"select 1", "mysql8 链接"),

	/**
	 * pg 数据库
	 */
	PG("pg", "jdbc:postgresql://%s:%s/%s", "select 1", "postgresql 链接"),

	/**
	 * SQL SERVER
	 */
	MSSQL("mssql", "jdbc:sqlserver://%s:%s;database=%s;characterEncoding=UTF-8", "select 1", "sqlserver 链接"),

	/**
	 * oracle
	 */
	ORACLE("oracle", "jdbc:oracle:thin:@%s:%s:%s", "select 1 from dual", "oracle 链接"),

	/**
	 * db2
	 */
	DB2("db2", "jdbc:db2://%s:%s/%s", "select 1 from sysibm.sysdummy1", "DB2 TYPE4 连接"),

	/**
	 * 达梦
	 */
	DM("dm", "jdbc:dm://%s:%s/%s", "select 1 from dual", "达梦连接"),

	/**
	 * pg 数据库
	 */
	HIGHGO("highgo", "jdbc:highgo://%s:%s/%s", "select 1", "highgo 链接");

	private final String dbName;

	private final String url;

	private final String validationQuery;

	private final String description;

	public static DsJdbcUrlEnum get(String dsType) {
		return Arrays.stream(DsJdbcUrlEnum.values())
			.filter(dsJdbcUrlEnum -> dsType.equals(dsJdbcUrlEnum.getDbName()))
			.findFirst()
			.get();
	}

}
