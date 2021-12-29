package com.pig4cloud.pigx.common.sequence.range.impl.db.provider;

import com.baomidou.mybatisplus.annotation.DbType;
import org.springframework.stereotype.Component;

/**
 * 数据源提供者
 *
 * @author lishangbu
 * @date 2021/12/29
 */
@Component
public class MysqlSqlProvider implements SqlProvider {

	/**
	 * 获取插入范围语句
	 * @return
	 */
	@Override
	public String getInsertRangeSql() {
		return "INSERT IGNORE INTO #tableName(id,name,value,gmt_create,gmt_modified)" + " VALUE(?,?,?,?,?)";
	}

	@Override
	public Boolean support(DbType dbType) {
		return DbType.MYSQL.equals(dbType);
	}

}
