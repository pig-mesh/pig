package com.github.pig.common.bean.interceptor;

import com.baomidou.mybatisplus.plugins.SqlParserHandler;
import com.baomidou.mybatisplus.plugins.pagination.PageHelper;
import com.baomidou.mybatisplus.toolkit.PluginUtils;
import com.github.pig.common.util.UserUtils;
import com.xiaoleilu.hutool.util.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author lengleng
 * @date 2018/1/19
 * 数据权限插件，参考PaginationInterceptor
 */
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor extends SqlParserHandler implements Interceptor {
    private static final String DATA_SCOPE = "DataScope";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        // 先判断是不是SELECT操作
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        // 如果mapper方法是DataScope结尾，则执行数据权限校验
        if (mappedStatement.getId().endsWith(DATA_SCOPE)) {
            BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
            String sql = boundSql.getSql();
            //查找参数中包含DataScope类型的参数
            Object parameterObject = boundSql.getParameterObject();
            DataScope dataScope = findDataScopeObject(parameterObject);

            List<Integer> deptIds;
            String scopeName = "dept_id";
            //如果入参没有数据权限参数则按照默认的（本级和下级）
            if (dataScope == null) {
                deptIds = CollectionUtil.newArrayList(1, 2, 3, 4);
            } else {
                scopeName = dataScope.getScopeName();
                deptIds = dataScope.getDeptIds();

                String username = UserUtils.getUser();
                //只查询本部门
                if (dataScope.getIsOnly()) {
                    deptIds = CollectionUtil.newArrayList(1);
                } else if (CollectionUtil.isEmpty(deptIds)) {
                    //deptIds为空是查询本级和下级
                    deptIds = CollectionUtil.newArrayList(1, 2, 3, 4);
                }
            }
            String join = CollectionUtil.join(deptIds, ",");
            sql = "select * from (" + sql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + join + ")";
            metaObject.setValue("delegate.boundSql.sql", sql);
            return invocation.proceed();
        } else {
            return invocation.proceed();
        }
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 查找参数是否包括DataScope对象
     *
     * @param parameterObj 参数列表
     * @return DataScope
     */
    private DataScope findDataScopeObject(Object parameterObj) {
        if (parameterObj instanceof DataScope) {
            return (DataScope) parameterObj;
        } else if (parameterObj instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObj).values()) {
                if (val instanceof DataScope) {
                    return (DataScope) val;
                }
            }
        }
        return null;
    }

}
