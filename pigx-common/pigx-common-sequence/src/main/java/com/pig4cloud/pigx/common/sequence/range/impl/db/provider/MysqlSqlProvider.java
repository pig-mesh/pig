package com.pig4cloud.pigx.common.sequence.range.impl.db.provider;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.stereotype.Component;

/**
 * 数据源提供者
 *
 * @author lishangbu
 * @date 2021/12/29
 */
@Component
public class MysqlSqlProvider implements SqlProvider {

	/**
	 * 获取建表语句
	 * @return
	 */
	@Override
	public String getCreateTableSql() {
		return "CREATE TABLE IF NOT EXISTS #tableName(" + "id bigint(20) NOT NULL AUTO_INCREMENT,"
				+ "value bigint(20) NOT NULL," + "name varchar(64) NOT NULL," + "gmt_create DATETIME NOT NULL,"
				+ "gmt_modified DATETIME NOT NULL," + "PRIMARY KEY (`id`),UNIQUE uk_name (`name`)" + ")";
	}

	@Override
	public Boolean support(DbType dbType) {
		return DbType.MYSQL.equals(dbType);
	}

}
