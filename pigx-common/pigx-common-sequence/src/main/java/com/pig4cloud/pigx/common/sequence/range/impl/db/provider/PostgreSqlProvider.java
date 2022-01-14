package com.pig4cloud.pigx.common.sequence.range.impl.db.provider;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.stereotype.Component;

/**
 * 数据源提供者
 *
 * @author lengleng
 * @date 2022-01-13
 */
@Component
public class PostgreSqlProvider implements SqlProvider {

	@Override
	public String getInsertRangeSql() {
		return "INSERT INTO #tableName(id,name,value,gmt_create,gmt_modified)" + " VALUE(?,?,?,?,?)";
	}

	@Override
	public Boolean support(DbType dbType) {
		return DbType.POSTGRE_SQL.equals(dbType);
	}

}
