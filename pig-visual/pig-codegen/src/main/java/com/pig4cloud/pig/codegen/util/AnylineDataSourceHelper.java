package com.pig4cloud.pig.codegen.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.experimental.UtilityClass;
import org.anyline.data.datasource.DataSourceHolder;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 * Anyline 动态数据源辅助类。
 * <p>
 * 统一封装以下动作：
 * <ul>
 * <li>根据数据源名称获取动态数据源</li>
 * <li>按需向 Anyline 注册 {@link javax.sql.DataSource}</li>
 * <li>切换 {@link DynamicDataSourceContextHolder} 上下文并在执行结束后清理</li>
 * </ul>
 * 业务层只需要关注具体操作逻辑，不再重复处理数据源切换细节。
 */
@UtilityClass
public class AnylineDataSourceHelper {

	/**
	 * 在指定数据源上下文中执行无返回值逻辑。
	 * @param dsName 数据源名称
	 * @param action 要执行的业务逻辑
	 */
	public void run(String dsName, Runnable action) {
		execute(dsName, () -> {
			action.run();
			return null;
		});
	}

	/**
	 * 在指定数据源上下文中执行有返回值逻辑。
	 * <p>
	 * 进入执行前会确保 Anyline 已注册对应数据源，并在 finally 中清理动态数据源上下文。
	 * @param dsName 数据源名称
	 * @param action 要执行的业务逻辑
	 * @param <T> 返回值类型
	 * @return 业务逻辑返回值
	 */
	public <T> T execute(String dsName, Supplier<T> action) {
		if (StrUtil.isBlank(dsName)) {
			throw new IllegalArgumentException("dsName must not be blank");
		}

		DynamicRoutingDataSource routingDataSource = SpringUtil.getBean(DynamicRoutingDataSource.class);
		DataSource dataSource = routingDataSource.getDataSource(dsName);
		if (dataSource == null) {
			throw new IllegalArgumentException("DataSource not found: " + dsName);
		}

		registerIfAbsent(dsName, dataSource);
		DynamicDataSourceContextHolder.push(dsName);
		try {
			return action.get();
		}
		finally {
			DynamicDataSourceContextHolder.clear();
		}
	}

	/**
	 * 按需向 Anyline 注册数据源，避免重复注册。
	 * @param dsName 数据源名称
	 * @param dataSource Spring 动态数据源中解析出的数据源实例
	 */
	private void registerIfAbsent(String dsName, DataSource dataSource) {
		if (DataSourceHolder.exists(dsName)) {
			return;
		}
		try {
			DataSourceHolder.reg(dsName, dataSource);
		}
		catch (Exception e) {
			throw new IllegalStateException("Failed to register Anyline datasource: " + dsName, e);
		}
	}

}
