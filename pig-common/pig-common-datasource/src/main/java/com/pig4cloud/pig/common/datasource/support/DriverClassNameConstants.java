package com.pig4cloud.pig.common.datasource.support;

/**
 * 数据库驱动ClassName
 *
 * @author huyuanxin
 * @date 2021-10-08
 */
public enum DriverClassNameConstants {

	/**
	 * MySQL Driver
	 * After mysql-connector-java 6
	 */
	MYSQL_DRIVER("com.mysql.cj.jdbc.Driver"),

	/**
	 * Old MySQL Driver
	 * Before mysql-connector-java 5
	 */
	OLD_MYSQL_DRIVER("com.mysql.jdbc.Driver"),

	/**
	 * Oracle Driver
	 */
	ORACLE_DRIVER("oracle.jdbc.OracleDriver"),

	/**
	 * Mariadb Driver
	 */
	MARIADB("org.mariadb.jdbc.Driver"),

	/**
	 * SqlServer Driver
	 * SqlServer 2005+
	 */
	SQLSERVER_DRIVER("com.microsoft.sqlserver.jdbc.SQLServerDriver"),

	/**
	 * Old SqlServer Driver
	 * SqlServer 2000
	 */
	OLD_SQLSERVER_DRIVER(" com.microsoft.jdbc.sqlserver.SQLServerDriver"),

	/**
	 * DB2 Driver
	 */
	DB2("com.ibm.db2.jcc.DB2Driver"),

	/**
	 * PostgreSQL Driver
	 */
	POSTGRE_SQL("org.postgresql.Driver"),

	/**
	 * Neo4j Bolt Driver
	 */
	NEO4J_BOLT("org.neo4j.jdbc.bolt.BoltDriver"),

	/**
	 * Neo4j Http Driver
	 */
	NEO4J_HTTP("org.neo4j.jdbc.http.HttpDriver");

	private final String driverClassName;


	DriverClassNameConstants(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

}
