package com.pig4cloud.pigx.common.sequence.range.impl.db.provider;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 * 数据源提供者
 *
 * @author lishangbu
 * @date 2021/12/29
 */
public interface SqlProvider {

	/**
	 * 获取建表语句
	 * @return
	 */
	default String getCreateTableSql() {
		return "CREATE TABLE IF NOT EXISTS #tableName(" + "id bigint(20) NOT NULL AUTO_INCREMENT,"
				+ "value bigint(20) NOT NULL," + "name varchar(32) NOT NULL," + "gmt_create DATETIME NOT NULL,"
				+ "gmt_modified DATETIME NOT NULL," + "PRIMARY KEY (`id`),UNIQUE uk_name (`name`)" + ")";
	}

	/**
	 * 获取插入范围语句
	 * @return
	 */
	String getInsertRangeSql();

	/**
	 * 获取更新范围语句
	 * @return
	 */
	default String getUpdateRangeSql() {
		return "UPDATE #tableName SET value=?,gmt_modified=? WHERE name=? AND " + "value=?";
	}

	/**
	 * 获取查询范围语句
	 * @return
	 */
	default String getSelectRangeSql() {
		return "SELECT value FROM #tableName WHERE name=?";
	}

	Boolean support(DbType dbType);

}
