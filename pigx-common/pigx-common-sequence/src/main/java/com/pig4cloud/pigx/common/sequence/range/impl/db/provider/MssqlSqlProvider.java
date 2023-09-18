package com.pig4cloud.pigx.common.sequence.range.impl.db.provider;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.stereotype.Component;

/**
 * 数据源提供者
 *
 * @author hanyichao
 * @date 2022-04-27
 *
 * sql server
 */
@Component
public class MssqlSqlProvider implements SqlProvider {

	/**
	 * 获取建表语句
	 * @return
	 */
	@Override
	public String getCreateTableSql() {
		return "IF NOT EXISTS (\n"
				+ "SELECT*FROM sys.all_objects WHERE object_id=OBJECT_ID(N'#tableName') AND type IN ('U')) \n"
				+ "CREATE TABLE #tableName (id bigint NOT NULL,VALUE bigint NOT NULL,name nvarchar (64) COLLATE "
				+ "Chinese_PRC_CI_AS NOT NULL,gmt_create datetime2 (7) NOT NULL,gmt_modified datetime2 (7) NOT NULL) GO";
	}

	@Override
	public Boolean support(DbType dbType) {
		return DbType.SQL_SERVER2005.equals(dbType);
	}

}
