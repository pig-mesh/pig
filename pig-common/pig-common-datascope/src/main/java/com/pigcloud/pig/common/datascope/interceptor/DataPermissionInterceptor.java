package com.pigcloud.pig.common.datascope.interceptor;

import com.pigcloud.pig.common.datascope.DataScope;
import com.pigcloud.pig.common.datascope.handler.DataPermissionHandler;
import com.pigcloud.pig.common.datascope.processor.DataScopeSqlProcessor;
import com.pigcloud.pig.common.datascope.util.PluginUtils;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;

import java.sql.Connection;
import java.util.List;

/**
 * 数据权限拦截器
 *
 * @author Hccake 2020/9/28
 * @version 1.0
 */
@RequiredArgsConstructor
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class DataPermissionInterceptor implements Interceptor {

	private final DataScopeSqlProcessor dataScopeSqlProcessor;

	private final DataPermissionHandler dataPermissionHandler;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 第一版，测试用
		Object target = invocation.getTarget();
		StatementHandler sh = (StatementHandler) target;
		PluginUtils.MPStatementHandler mpSh = PluginUtils.mpStatementHandler(sh);
		MappedStatement ms = mpSh.mappedStatement();
		SqlCommandType sct = ms.getSqlCommandType();
		PluginUtils.MPBoundSql mpBs = mpSh.mPBoundSql();
		String mappedStatementId = ms.getId();

		// 根据用户权限判断是否需要拦截，例如管理员可以查看所有，则直接放行
		if (dataPermissionHandler.ignorePermissionControl(mappedStatementId)) {
			return invocation.proceed();
		}

		List<DataScope> dataScopes = dataPermissionHandler.filterDataScopes(mappedStatementId);
		if (dataScopes == null || dataScopes.isEmpty()) {
			return invocation.proceed();
		}

		// 根据 DataScopes 进行数据权限的 sql 处理
		if (sct == SqlCommandType.SELECT) {
			mpBs.sql(dataScopeSqlProcessor.parserSingle(mpBs.sql(), dataScopes));
		}
		else if (sct == SqlCommandType.INSERT || sct == SqlCommandType.UPDATE || sct == SqlCommandType.DELETE) {
			mpBs.sql(dataScopeSqlProcessor.parserMulti(mpBs.sql(), dataScopes));
		}

		// 执行 sql
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

}
