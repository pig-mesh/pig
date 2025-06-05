package com.pig4cloud.pig.common.mybatis.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ParameterUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 分页拦截器实现类，用于处理分页查询逻辑
 * <p>
 * 当分页大小小于0时自动设置为0，防止全表查询
 *
 * @author lengleng
 * @date 2025/05/31
 * @since 2021年10月11日
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
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

	/**
	 * 在执行查询前处理分页参数
	 * @param executor 执行器
	 * @param ms 映射语句
	 * @param parameter 参数对象
	 * @param rowBounds 行边界
	 * @param resultHandler 结果处理器
	 * @param boundSql 绑定SQL
	 */
	@Override
	public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
			ResultHandler resultHandler, BoundSql boundSql) {
		IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
		// size 小于 0 直接设置为 0 , 即不查询任何数据
		if (null != page && page.getSize() < 0) {
			page.setSize(0);
		}
		super.beforeQuery(executor, ms, page, rowBounds, resultHandler, boundSql);
	}

}
