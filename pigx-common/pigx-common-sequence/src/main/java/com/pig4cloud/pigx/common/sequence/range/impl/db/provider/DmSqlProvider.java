package com.pig4cloud.pigx.common.sequence.range.impl.db.provider;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.stereotype.Component;

/**
 * 数据源提供者
 *
 * @author 达梦数据库支持
 * @date 2022-04-27
 *
 * sql server
 */
@Component
public class DmSqlProvider implements SqlProvider {

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
	 * @return
	 */
	@Override
	public String getCreateTableSql() {
		return "CREATE TABLE #tableName (ID BIGINT NOT NULL,VALUE BIGINT NOT NULL"
				+ ",NAME VARCHAR (64) NOT NULL,GMT_CREATE TIMESTAMP (0) NOT NULL,GMT_MODIFIED TIMESTAMP (0) NOT NULL"
				+ ",NOT CLUSTER PRIMARY KEY (ID),CONSTRAINT UK_NAME UNIQUE (NAME)) STORAGE (ON MAIN,CLUSTERBTR);";
	}

	@Override
	public Boolean support(DbType dbType) {
		return DbType.DM.equals(dbType);
	}

}
