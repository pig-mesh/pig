package com.pig4cloud.pigx.common.data.datascope;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.Setter;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2020/11/29
 */
public class DataScopeInnerInterceptor implements InnerInterceptor {

	@Setter
	private DataScopeHandle dataScopeHandle;

	@Override
	public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
			ResultHandler resultHandler, BoundSql boundSql) {
		PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);

		String originalSql = boundSql.getSql();
		Object parameterObject = boundSql.getParameterObject();

		// 查找参数中包含DataScope类型的参数
		DataScope dataScope = findDataScopeObject(parameterObject);
		if (dataScope == null) {
			return;
		}

		String scopeName = dataScope.getScopeName();
		List<Long> deptIds = dataScope.getDeptIds();
		// 优先获取赋值数据
		if (CollUtil.isEmpty(deptIds) && dataScopeHandle.calcScope(deptIds)) {
			originalSql = String.format("SELECT %s FROM (%s) temp_data_scope", dataScope.getFunc().getType(),
					originalSql);
			mpBs.sql(originalSql);
			return;
		}

		if (deptIds.isEmpty()) {
			originalSql = String.format("SELECT %s FROM (%s) temp_data_scope WHERE 1 = 2",
					dataScope.getFunc().getType(), originalSql);
		}
		else {
			String join = CollectionUtil.join(deptIds, ",");
			originalSql = String.format("SELECT %s FROM (%s) temp_data_scope WHERE temp_data_scope.%s IN (%s)",
					dataScope.getFunc().getType(), originalSql, scopeName, join);
		}

		mpBs.sql(originalSql);
	}

	/**
	 * 查找参数是否包括DataScope对象
	 * @param parameterObj 参数列表
	 * @return DataScope
	 */
	private DataScope findDataScopeObject(Object parameterObj) {
		if (parameterObj instanceof DataScope) {
			return (DataScope) parameterObj;
		}
		else if (parameterObj instanceof Map) {
			for (Object val : ((Map<?, ?>) parameterObj).values()) {
				if (val instanceof DataScope) {
					return (DataScope) val;
				}
			}
		}
		return null;
	}

}
