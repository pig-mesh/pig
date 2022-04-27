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
	 * 获取表是否存在
	 * @return 执行语句
	 */
	@Override
	public String getExistTableSql() {
		return "SELECT TOP 1 * FROM sysObjects WHERE Id=OBJECT_ID(N'#tableName') AND xtype='U'";
	}

	/**
	 * 获取建表语句
	 * @return
	 */
	@Override
	public String getCreateTableSql() {
		return "CREATE TABLE #tableName (id bigint NOT NULL,value bigint NOT NULL,name nvarchar (32) COLLATE "
				+ "Chinese_PRC_CI_AS NOT NULL,gmt_create datetime2 (7) NOT NULL,gmt_modified datetime2 (7) NOT NULL)";
	}

	@Override
	public Boolean support(DbType dbType) {
		return DbType.SQL_SERVER.equals(dbType);
	}

}
