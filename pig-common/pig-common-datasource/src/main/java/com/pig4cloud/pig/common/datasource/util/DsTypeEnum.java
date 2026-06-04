package com.pig4cloud.pig.common.datasource.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author lengleng
 * @date 2020/12/11
 * <p>
 * 数据源类型枚举，包含 JDBC URL 模板、连接验证语句、描述及 AnyLine 解析适配器等元数据。
 */
@Getter
@AllArgsConstructor
public enum DsTypeEnum {

	/**
	 * mysql 数据库
	 */
	MYSQL("mysql",
			"jdbc:mysql://%s:%s/%s?characterEncoding=utf8"
					+ "&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true"
					+ "&useLegacyDatetimeCode=false&allowMultiQueries=true&allowPublicKeyRetrieval=true",
			"select 1", "mysql8 链接", "org.anyline.data.jdbc.mysql.MySQLAdapter"),

	/**
	 * pg 数据库
	 */
	PG("pg", "jdbc:postgresql://%s:%s/%s", "select 1", "postgresql 链接",
			"org.anyline.data.jdbc.postgresql.PostgresqlAdapter"),

	/**
	 * SQL SERVER
	 */
	MSSQL("mssql", "jdbc:sqlserver://%s:%s;database=%s;characterEncoding=UTF-8", "select 1", "sqlserver 链接",
			"org.anyline.data.jdbc.mssql.MSSQLAdapter"),

	/**
	 * oracle
	 */
	ORACLE("oracle", "jdbc:oracle:thin:@%s:%s:%s", "select 1 from dual", "oracle 链接",
			"org.anyline.data.jdbc.oracle.OracleAdapter"),

	/**
	 * db2
	 */
	DB2("db2", "jdbc:db2://%s:%s/%s", "select 1 from sysibm.sysdummy1", "DB2 TYPE4 连接",
			"org.anyline.data.jdbc.db2.DB2Adapter"),

	/**
	 * 达梦
	 */
	DM("dm", "jdbc:dm://%s:%s/%s", "select 1 from dual", "达梦连接", "org.anyline.data.jdbc.dm.DMAdapter"),

	/**
	 * highgo 数据库
	 */
	HIGHGO("highgo", "jdbc:highgo://%s:%s/%s", "select 1", "highgo 链接", "org.anyline.data.jdbc.highgo.HighgoAdapter");

	private final String dbName;

	private final String url;

	private final String validationQuery;

	private final String description;

	/**
	 * AnyLine JDBC 解析适配器的全类名，用于运行时检测对应数据库方言插件是否已加载。
	 */
	private final String anylineAdapter;

	public static DsTypeEnum get(String dsType) {
		return Arrays.stream(DsTypeEnum.values())
			.filter(dsTypeEnum -> dsType.equals(dsTypeEnum.getDbName()))
			.findFirst()
			.get();
	}

}
