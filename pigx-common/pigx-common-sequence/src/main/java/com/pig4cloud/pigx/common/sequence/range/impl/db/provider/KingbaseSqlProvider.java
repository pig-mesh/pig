package com.pig4cloud.pigx.common.sequence.range.impl.db.provider;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2023/4/23 kingbase数据库支持
 */
@Component
public class KingbaseSqlProvider implements SqlProvider {

	/**
	 * 获取建表语句
	 * @return SQL
	 */
	@Override
	public String getCreateTableSql() {
		return "CREATE TABLE IF NOT EXISTS #tableName (ID INT8 PRIMARY KEY NOT NULL,VALUE INT8 NOT NULL,NAME VARCHAR (266) NOT NULL,gmt_create TIMESTAMP (6) NOT NULL,gmt_modified TIMESTAMP (6) NOT NULL)";
	}

	@Override
	public Boolean support(DbType dbType) {
		return DbType.KINGBASE_ES.equals(dbType);
	}

}
