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
	 * 获取表是否存在
	 * @return
	 */
	default String getExistTableSql() {
		return null;
	}

	/**
	 * 获取建表语句
	 * @return
	 */
	String getCreateTableSql();

	/**
	 * 获取插入范围语句
	 * @return
	 */
	default String getInsertRangeSql() {
		return "INSERT INTO #tableName (id,name,value,gmt_create,gmt_modified)" + " VALUES(?,?,?,?,?)";
	}

	/**
	 * 获取更新范围语句
	 * @return
	 */
	default String getUpdateRangeSql() {
		return "UPDATE #tableName SET value=?,gmt_modified=? WHERE name=? AND value=?";
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
