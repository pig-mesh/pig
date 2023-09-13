package com.pig4cloud.pigx.common.data.datascope;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
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
public class DataScopeInnerInterceptor implements DataScopeInterceptor {

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

		// 返回true 不拦截直接返回原始 SQL （只针对 * 查询）
		if (dataScopeHandle.calcScope(dataScope) && DataScopeFuncEnum.ALL.equals(dataScope.getFunc())) {
			return;
		}

		// 返回true 不拦截直接返回原始 SQL （只针对 COUNT 查询）
		if (dataScopeHandle.calcScope(dataScope) && DataScopeFuncEnum.COUNT.equals(dataScope.getFunc())) {
			mpBs.sql(String.format("SELECT %s FROM (%s) temp_data_scope", dataScope.getFunc().getType(), originalSql));
			return;
		}

		List<Long> deptIds = dataScope.getDeptList();

		// 1.无数据权限限制，则直接返回 0 条数据
		if (CollUtil.isEmpty(deptIds) && StrUtil.isBlank(dataScope.getUsername())) {
			originalSql = String.format("SELECT %s FROM (%s) temp_data_scope WHERE 1 = 2",
					dataScope.getFunc().getType(), originalSql);
		}
		// 2.如果为本人权限则走下面
		else if (StrUtil.isNotBlank(dataScope.getUsername())) {
			originalSql = String.format("SELECT %s FROM (%s) temp_data_scope WHERE temp_data_scope.%s = '%s'",
					dataScope.getFunc().getType(), originalSql, dataScope.getScopeUserName(), dataScope.getUsername());
		}
		// 3.都没有，则是其他权限，走下面
		else {
			String join = CollectionUtil.join(deptIds, ",");
			originalSql = String.format("SELECT %s FROM (%s) temp_data_scope WHERE temp_data_scope.%s IN (%s)",
					dataScope.getFunc().getType(), originalSql, dataScope.getScopeDeptName(), join);
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
