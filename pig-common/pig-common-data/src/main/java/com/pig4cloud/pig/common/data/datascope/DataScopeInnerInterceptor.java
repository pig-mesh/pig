package com.pig4cloud.pig.common.data.datascope;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlInjectionUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
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

import java.util.List;
import java.util.Map;

/**
 * 数据权限内部拦截器
 * <p>
 * 参照 MyBatis Plus {@code DataPermissionInterceptor} 的实现思路，
 * 通过 JSqlParser 解析 SQL 为 AST，使用 {@code PlainSelect.setWhere()} 直接在
 * WHERE 子句上追加数据权限条件，避免子查询包装导致索引失效。
 * 对于 UNION 等复杂 SQL 或解析失败的情况，回退到子查询包装方式保证兼容性。
 *
 * @author lengleng
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

        try {
            Statement statement = CCJSqlParserUtil.parse(originalSql);
            if (!(statement instanceof Select selectStatement)) {
                return;
            }

            // UNION 等复杂语句无法直接操作 WHERE，回退到子查询包装
            if (!(selectStatement.getSelectBody() instanceof PlainSelect plainSelect)) {
                mpBs.sql(buildSubQuerySql(originalSql, dataScope));
                return;
            }

            // 构建数据权限条件表达式
            Expression scopeExpression = buildScopeExpression(plainSelect, dataScope);
            if (scopeExpression == null) {
                return;
            }

            // 与原有 WHERE 条件合并（数据权限条件 AND 原条件）
            Expression existingWhere = plainSelect.getWhere();
            if (existingWhere != null) {
                plainSelect.setWhere(new AndExpression(scopeExpression, existingWhere));
            } else {
                plainSelect.setWhere(scopeExpression);
            }

            mpBs.sql(selectStatement.toString());
        } catch (JSQLParserException e) {
            log.warn("数据权限 SQL 解析失败，回退到子查询包装: {}", originalSql, e);
            mpBs.sql(buildSubQuerySql(originalSql, dataScope));
        }
    }

    /**
     * 根据 DataScope 配置构建数据权限条件表达式
     * <p>
     * 使用 {@code CCJSqlParserUtil.parseCondExpression()} 解析条件字符串为 Expression 对象，
     * 与 MyBatis Plus DataPermissionInterceptor 保持一致的实现方式。
     *
     * @param plainSelect 解析后的 PlainSelect 对象
     * @param dataScope   数据权限参数
     * @return 数据权限条件表达式，无需处理时返回 null
     */
    private Expression buildScopeExpression(PlainSelect plainSelect, DataScope dataScope)
            throws JSQLParserException {
        List<Long> deptIds = dataScope.getDeptList();
        String username = dataScope.getUsername();

        // 获取表引用（优先别名，其次表名），用于限定列名
        String tableRef = getTableReference(plainSelect.getFromItem());
        String prefix = StrUtil.isNotBlank(tableRef) ? tableRef + "." : "";

        // 1. 无数据权限限制，返回 0 条数据
        if (CollUtil.isEmpty(deptIds) && StrUtil.isBlank(username)) {
            return CCJSqlParserUtil.parseCondExpression("1 = 2");
        }

        // SQL 注入检查
        if (StrUtil.isNotBlank(username) && SqlInjectionUtils.check(username)) {
            return CCJSqlParserUtil.parseCondExpression("1 = 2");
        }

        // 2. 本人权限 + 部门权限：加括号保证与原有条件组合时的优先级
        if (StrUtil.isNotBlank(username) && CollUtil.isNotEmpty(deptIds)) {
            String join = CollectionUtil.join(deptIds, ",");
            String logicKeyword = dataScope.getLogicMode() != null ? dataScope.getLogicMode().getKeyword()
                    : DataScopeLogicEnum.OR.getKeyword();
            String condSql = String.format("(%s%s = '%s' %s %s%s IN (%s))", prefix, dataScope.getScopeUserName(),
                    username, logicKeyword, prefix, dataScope.getScopeDeptName(), join);
            return CCJSqlParserUtil.parseCondExpression(condSql);
        }

        // 3. 仅本人权限
        if (StrUtil.isNotBlank(username)) {
            String condSql = String.format("%s%s = '%s'", prefix, dataScope.getScopeUserName(), username);
            return CCJSqlParserUtil.parseCondExpression(condSql);
        }

        // 4. 仅部门权限
        String join = CollectionUtil.join(deptIds, ",");
        String condSql = String.format("%s%s IN (%s)", prefix, dataScope.getScopeDeptName(), join);
        return CCJSqlParserUtil.parseCondExpression(condSql);
    }

    /**
     * 获取表引用名称（优先别名，其次表名）
     *
     * @param fromItem FROM 子句项
     * @return 表引用名称，无法获取时返回 null
     */
    private String getTableReference(FromItem fromItem) {
        if (fromItem == null) {
            return null;
        }
        if (fromItem.getAlias() != null) {
            return fromItem.getAlias().getName();
        }
        if (fromItem instanceof Table table) {
            return table.getName();
        }
        return null;
    }

    /**
     * 回退方案：子查询包装（用于 UNION 等复杂 SQL 或解析失败时）
     */
    private String buildSubQuerySql(String originalSql, DataScope dataScope) {
        List<Long> deptIds = dataScope.getDeptList();
        String username = dataScope.getUsername();
        String selectType = dataScope.getFunc().getType();

        // 无权限
        if (CollUtil.isEmpty(deptIds) && StrUtil.isBlank(username)) {
            return String.format("SELECT %s FROM (%s) temp_data_scope WHERE 1 = 2", selectType, originalSql);
        }

        // SQL 注入检查
        if (StrUtil.isNotBlank(username) && SqlInjectionUtils.check(username)) {
            return String.format("SELECT %s FROM (%s) temp_data_scope WHERE 1 = 2", selectType, originalSql);
        }

        // 本人 + 部门
        if (StrUtil.isNotBlank(username) && CollUtil.isNotEmpty(deptIds)) {
            String join = CollectionUtil.join(deptIds, ",");
            String logicKeyword = dataScope.getLogicMode() != null ? dataScope.getLogicMode().getKeyword()
                    : DataScopeLogicEnum.OR.getKeyword();
            return String.format(
                    "SELECT %s FROM (%s) temp_data_scope WHERE (temp_data_scope.%s = '%s' %s temp_data_scope.%s IN (%s))",
                    selectType, originalSql, dataScope.getScopeUserName(), username, logicKeyword,
                    dataScope.getScopeDeptName(), join);
        }

        // 仅本人
        if (StrUtil.isNotBlank(username)) {
            return String.format("SELECT %s FROM (%s) temp_data_scope WHERE temp_data_scope.%s = '%s'", selectType,
                    originalSql, dataScope.getScopeUserName(), username);
        }

        // 仅部门
        String join = CollectionUtil.join(deptIds, ",");
        return String.format("SELECT %s FROM (%s) temp_data_scope WHERE temp_data_scope.%s IN (%s)", selectType,
                originalSql, dataScope.getScopeDeptName(), join);
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
