package com.pig4cloud.pigx.common.sequence.range.impl.db.provider;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.stereotype.Component;

/**
 * 瀚高数据源
 *
 * @author lengleng
 * @date 2023-04-26
 */
@Component
public class HighGoSqlProvider implements SqlProvider {

	/**
	 * 获取建表语句
	 * @return
	 */
	@Override
	public String getCreateTableSql() {
		return "CREATE TABLE IF NOT EXISTS #tableName (ID int8 PRIMARY KEY NOT NULL"
				+ ",VALUE int8 NOT NULL,NAME VARCHAR (266) NOT NULL,gmt_create TIMESTAMP (6) NOT NULL"
				+ ",gmt_modified TIMESTAMP (6) NOT NULL)";
	}

	@Override
	public Boolean support(DbType dbType) {
		return DbType.HIGH_GO.equals(dbType);
	}

}
