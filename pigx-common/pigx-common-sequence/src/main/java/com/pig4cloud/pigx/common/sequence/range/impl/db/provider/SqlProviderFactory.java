package com.pig4cloud.pigx.common.sequence.range.impl.db.provider;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * sql语句提供工厂
 *
 * @author lishangbu
 * @date 2021/12/29
 */
@Lazy(value = false)
@Component
@RequiredArgsConstructor
public class SqlProviderFactory {

	private final DataSourceProperties dataSourceProperties;

	private final List<SqlProvider> sqlProviders;

	private final Environment environment;

	public SqlProvider getSqlProvider() {
		String url = dataSourceProperties.getUrl();
		// druid 形式 进行降级获取
		if (StrUtil.isBlank(url)) {
			url = environment.getProperty("spring.datasource.druid.url");
		}

		DbType dbType = JdbcUtils.getDbType(url);
		for (SqlProvider sqlProvider : sqlProviders) {
			if (sqlProvider.support(dbType)) {
				return sqlProvider;
			}
		}
		throw new UnsupportedOperationException("不支持的数据源");
	}

	/**
	 * 获取插入范围语句
	 * @return 插入范围语句
	 */
	public String getCreateTableSql() {
		SqlProvider sqlProvider = getSqlProvider();
		return sqlProvider.getCreateTableSql();
	}

	/**
	 * 获取插入范围语句
	 * @return 插入范围语句
	 */
	public String getInsertRangeSql() {
		SqlProvider sqlProvider = getSqlProvider();
		return sqlProvider.getInsertRangeSql();
	}

	/**
	 * 获取查询范围语句
	 * @return
	 */
	public String getSelectRangeSql() {
		SqlProvider sqlProvider = getSqlProvider();
		return sqlProvider.getSelectRangeSql();
	}

	/**
	 * 获取更新范围语句
	 * @return 更新范围语句
	 */
	public String getUpdateRangeSql() {
		SqlProvider sqlProvider = getSqlProvider();
		return sqlProvider.getUpdateRangeSql();
	}

	/**
	 * 获取是否创建表语句
	 * @return SQL语句
	 */
	public String getExistTableSql() {
		SqlProvider sqlProvider = getSqlProvider();
		return sqlProvider.getExistTableSql();
	}

}
