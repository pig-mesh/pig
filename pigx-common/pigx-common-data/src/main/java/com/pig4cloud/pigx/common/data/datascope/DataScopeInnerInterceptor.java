package com.pig4cloud.pigx.common.data.datascope;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author legal
 * @date 2020/11/29
 */
@Slf4j
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

        // 是否强制跳过数据权限
        if (dataScope.isSkip()) {
            return;
        }

        // 返回true 不拦截直接返回原始 SQL （只针对 * 查询）
        if (DataScopeFuncEnum.ALL.equals(dataScope.getFunc()) && dataScopeHandle.calcScope(dataScope)) {
            return;
        }

        // 返回true 不拦截直接返回原始 SQL （只针对 COUNT 查询）
        if (DataScopeFuncEnum.COUNT.equals(dataScope.getFunc()) && dataScopeHandle.calcScope(dataScope)) {
            mpBs.sql(String.format("SELECT %s FROM (%s) temp_data_scope", dataScope.getFunc().getType(), originalSql));
            return;
        }
        // 分割sql语句
        List<String> where = splitSqlByWhere(originalSql);
        // 获取表名
        String tableName = getTableName(originalSql);

        List<Long> deptIds = dataScope.getDeptList();

        // 1.无数据权限限制，则直接返回 0 条数据
        if (CollUtil.isEmpty(deptIds) && StrUtil.isBlank(dataScope.getUsername())) {
            if (CollUtil.isNotEmpty(where) && where.size() > 1) {
                originalSql = String.format("%s WHERE 1 = 2 AND %s", where.get(0), where.get(1));
            } else {
                originalSql = String.format("%s WHERE 1 = 2", where.get(0));
            }
            /*originalSql = String.format("SELECT %s FROM (%s) temp_data_scope WHERE 1 = 2",
                    dataScope.getFunc().getType(), originalSql);*/
        }
        // 2.如果为本人权限 + 部门权限控制
        else if (StrUtil.isNotBlank(dataScope.getUsername()) && CollUtil.isNotEmpty(deptIds)) {
            String join = CollectionUtil.join(deptIds, ",");
            // 使用配置的组合模式(AND/OR),默认OR保持向后兼容
            String logicKeyword = dataScope.getLogicMode() != null ? dataScope.getLogicMode().getKeyword()
                    : DataScopeLogicEnum.OR.getKeyword();
            originalSql = String.format("%s WHERE %s.%s = '%s' %s %s.%s IN (%s)",
                    where.get(0),
                    tableName,
                    dataScope.getScopeUserName(),
                    dataScope.getUsername(),
                    logicKeyword,
                    tableName,
                    dataScope.getScopeDeptName(),
                    join
            );
            if (CollUtil.isNotEmpty(where) && where.size() > 1) {
                originalSql = originalSql + " AND " + where.get(1);
            }
            /*originalSql = String.format(
                    "SELECT %s FROM (%s) temp_data_scope WHERE temp_data_scope.%s = '%s' %s temp_data_scope.%s IN (%s)",
                    dataScope.getFunc().getType(), originalSql, dataScope.getScopeUserName(), dataScope.getUsername(),
                    logicKeyword, dataScope.getScopeDeptName(), join);*/
        }
        // 3. 如果为本人
        else if (StrUtil.isNotBlank(dataScope.getUsername())) {
            if (CollUtil.isNotEmpty(where) && where.size() > 1) {
                originalSql = String.format("%s WHERE %s.%s = '%s' AND %s",
                        where.get(0),
                        tableName,
                        dataScope.getScopeUserName(),
                        dataScope.getUsername(),
                        where.get(1)
                );
            } else {
                originalSql = String.format("%s WHERE %s.%s = '%s'",
                        where.get(0),
                        tableName,
                        dataScope.getScopeUserName(),
                        dataScope.getUsername()
                );
            }
            /*originalSql = String.format("SELECT %s FROM (%s) temp_data_scope WHERE temp_data_scope.%s = '%s'",
                    dataScope.getFunc().getType(), originalSql, dataScope.getScopeUserName(), dataScope.getUsername());*/
        }
        // 4.部门权限控制
        else {
            String join = CollectionUtil.join(deptIds, ",");
            if (CollUtil.isNotEmpty(where) && where.size() > 1) {
                originalSql = String.format("%s WHERE %s.%s IN (%s) AND %s",
                        where.get(0),
                        tableName,
                        dataScope.getScopeDeptName(),
                        join,
                        where.get(1)
                );
            } else {
                originalSql = String.format("%s WHERE %s.%s IN (%s)",
                        where.get(0),
                        tableName,
                        dataScope.getScopeDeptName(),
                        join
                );
            }
            /*originalSql = String.format("SELECT %s FROM (%s) temp_data_scope WHERE temp_data_scope.%s IN (%s)",
                    dataScope.getFunc().getType(), originalSql, dataScope.getScopeDeptName(), join);*/
        }

        mpBs.sql(originalSql);
    }

    /**
     * 获取表名（优先取别名，其次取原表名）
     */
    private static String getTableName(String originalSql) {
        if (StrUtil.isBlank(originalSql)) {
            return "";
        }
        try {
            Statement statement = CCJSqlParserUtil.parse(originalSql);
            if (!(statement instanceof Select selectStatement)) {
                return "";
            }

            // 增加 PlainSelect 类型判断，避免 UNION 等复杂语句强转异常
            if (!(selectStatement.getSelectBody() instanceof PlainSelect plainSelect)) {
                return "";
            }

            FromItem fromItem = plainSelect.getFromItem();
            if (fromItem == null) {
                return "";
            }

            // 优先使用别名
            if (fromItem.getAlias() != null) {
                return fromItem.getAlias().getName();
            }

            // 其次使用原表名
            if (fromItem instanceof Table table) {
                return table.getName();
            }

        } catch (JSQLParserException e) {
            log.error("JSQLParserException: {} -- sql: {}", e.getMessage(), originalSql);
        }
        return "";
    }

    /**
     * 分割SQL by WHERE
     * @return 分割结果，失败返回空集合
     */
    private static List<String> splitSqlByWhere(String sql) {
        if (StrUtil.isBlank(sql)) {
            return Collections.emptyList();
        }
        try {
            // 统一转大写匹配，避免遗漏大小写混合情况（如 Where / wHere）
            String upperSql = sql.toUpperCase();
            int index = upperSql.indexOf(" WHERE ");
            if (index != -1) {
                return List.of(
                        sql.substring(0, index),
                        sql.substring(index + " WHERE ".length())
                );
            }
        } catch (Exception e) {
            log.error("分割SQL异常：{}--：{}", e, sql);
        }
        return Collections.emptyList();
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