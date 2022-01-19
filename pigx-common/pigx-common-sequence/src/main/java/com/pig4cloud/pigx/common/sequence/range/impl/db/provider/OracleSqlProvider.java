package com.pig4cloud.pigx.common.sequence.range.impl.db.provider;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.stereotype.Component;

/**
 * oracle数据源
 *
 * @author lishangbu
 * @date 2021/12/29
 */
@Component
public class OracleSqlProvider implements SqlProvider {

	/**
	 * 获取表是否存在
	 * @return
	 */
	@Override
	public String getExistTableSql() {
		return "select count(*) from user_tables where table_name =upper('#tableName')";
	}

	/**
	 * 获取建表语句
	 * @return SQL
	 */
	@Override
	public String getCreateTableSql() {
		return "CREATE TABLE #tableName " + "(id NUMBER (20,0) VISIBLE NOT NULL,value NUMBER (20,0) VISIBLE NOT NULL"
				+ ",name VARCHAR2 (96 BYTE) VISIBLE,gmt_create DATE VISIBLE NOT NULL"
				+ ",gmt_modified DATE VISIBLE NOT NULL)";
	}

	/**
	 * 获取更新范围语句
	 * @return
	 */
	public String getUpdateRangeSql() {
		return "UPDATE #tableName SET value=?,gmt_modified=? WHERE name=? AND value=?";
	}

	/**
	 * 获取查询范围语句
	 * @return
	 */
	public String getSelectRangeSql() {
		return "SELECT value FROM #tableName WHERE name=?";
	}

	@Override
	public Boolean support(DbType dbType) {
		return DbType.ORACLE.equals(dbType);
	}

}
