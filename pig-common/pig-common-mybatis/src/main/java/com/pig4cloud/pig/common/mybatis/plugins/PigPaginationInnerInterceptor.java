package com.pig4cloud.pig.common.mybatis.plugins;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ParameterUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.SQLException;

/**
 * 分页拦截器
 * <p>
 * 重构分页插件, 当 size 小于 0 时, 直接设置为 0, 防止错误查询全表
 *
 * @author seven
 * @since 2021年10月11日
 */
@Data
@NoArgsConstructor
public class PigPaginationInnerInterceptor extends PaginationInnerInterceptor {

	/**
	 * 数据库类型
	 * <p>
	 * 查看 {@link #findIDialect(Executor)} 逻辑
	 */
	private DbType dbType;

	/**
	 * 方言实现类
	 * <p>
	 * 查看 {@link #findIDialect(Executor)} 逻辑
	 */
	private IDialect dialect;

	public PigPaginationInnerInterceptor(DbType dbType) {
		this.dbType = dbType;
	}

	public PigPaginationInnerInterceptor(IDialect dialect) {
		this.dialect = dialect;
	}

	@Override
	public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
			ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
		IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
		// size 小于 0 直接设置为 0 , 即不查询任何数据
		if (null != page && page.getSize() < 0) {
			page.setSize(0);
		}
		super.beforeQuery(executor, ms, page, rowBounds, resultHandler, boundSql);
	}

}
