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

	@Override
	public String getInsertRangeSql() {
		return "INSERT INTO #tableName(id,name,value,gmt_create,gmt_modified)" + " VALUE(?,?,?,?,?)";
	}

	@Override
	public Boolean support(DbType dbType) {
		return DbType.ORACLE.equals(dbType);
	}

}
