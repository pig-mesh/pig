package com.pigcloud.pig.common.datascope.test;

import com.pigcloud.pig.common.datascope.DataScope;
import com.pigcloud.pig.common.datascope.handler.DefaultDataPermissionHandler;
import com.pigcloud.pig.common.datascope.handler.DataPermissionHandler;
import com.pigcloud.pig.common.datascope.processor.DataScopeSqlProcessor;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author Hccake 2020/9/28
 * @version 1.0
 */
class SqlParseTest {

	@Test
	void test() {
		DataScope dataScope = new DataScope() {
			final String columnId = "order_id";

			@Override
			public String getResource() {
				return "order";
			}

			@Override
			public Collection<String> getTableNames() {
				Set<String> tableNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
				tableNames.addAll(Arrays.asList("t_order", "t_order_info"));
				return tableNames;
			}

			@Override
			public Expression getExpression(String tableName, Alias tableAlias) {
				Column column = new Column(tableAlias == null ? columnId : tableAlias.getName() + "." + columnId);
				ExpressionList expressionList = new ExpressionList();
				expressionList.setExpressions(Arrays.asList(new StringValue("1"), new StringValue("2")));
				return new InExpression(column, expressionList);
			}
		};

		List<DataScope> dataScopes = new ArrayList<>();
		dataScopes.add(dataScope);

		DataPermissionHandler dataPermissionHandler = new DefaultDataPermissionHandler(dataScopes) {
			@Override
			public boolean ignorePermissionControl(String mappedStatementId) {
				return false;
			}
		};

		DataScopeSqlProcessor dataScopeSqlProcessor = new DataScopeSqlProcessor();

		// DataScopeHolder.putDataScope("order", dataScope);

		String sql = "select o.order_id,o.order_name,oi.order_price "
				+ "from t_ORDER o left join t_order_info oi on o.order_id = oi.order_id "
				+ "where oi.order_price > 100";

		String parseSql = dataScopeSqlProcessor.parserSingle(sql, dataPermissionHandler.dataScopes());
		System.out.println(parseSql);

		String trueSql = "SELECT o.order_id, o.order_name, oi.order_price FROM t_ORDER o LEFT JOIN t_order_info oi ON o.order_id = oi.order_id AND oi.order_id IN ('1', '2') WHERE oi.order_price > 100 AND o.order_id IN ('1', '2')";
		Assert.isTrue(trueSql.equals(parseSql), "sql 数据权限解析异常");
	}

}
