package com.pigcloud.pig.common.datascope.processor;

import com.pigcloud.pig.common.datascope.DataScope;
import com.pigcloud.pig.common.datascope.holder.DataScopeHolder;
import com.pigcloud.pig.common.datascope.parser.JsqlParserSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.update.Update;

import java.util.List;
import java.util.Objects;

/**
 * 数据权限 sql 处理器 参考 mybatis-plus 租户拦截器，解析 sql where 部分，进行查询表达式注入
 *
 * @author Hccake 2020/9/26
 * @version 1.0
 */
@RequiredArgsConstructor
@Slf4j
public class DataScopeSqlProcessor extends JsqlParserSupport {

	private static final String MYSQL_ESCAPE_CHARACTER = "`";

	/**
	 * select 类型SQL处理
	 * @param select jsqlparser Statement Select
	 */
	@Override
	protected void processSelect(Select select, int index, String sql, Object obj) {
		List<DataScope> dataScopes = (List<DataScope>) obj;
		try {
			// dataScopes 放入 ThreadLocal 方便透传
			DataScopeHolder.set(dataScopes);
			processSelectBody(select.getSelectBody());
			List<WithItem> withItemsList = select.getWithItemsList();
			if (withItemsList != null && !withItemsList.isEmpty()) {
				withItemsList.forEach(this::processSelectBody);
			}
		}
		finally {
			// 必须清空 ThreadLocal
			DataScopeHolder.remove();
		}
	}

	protected void processSelectBody(SelectBody selectBody) {
		if (selectBody == null) {
			return;
		}
		if (selectBody instanceof PlainSelect) {
			processPlainSelect((PlainSelect) selectBody);
		}
		else if (selectBody instanceof WithItem) {
			WithItem withItem = (WithItem) selectBody;
			processSelectBody(withItem.getSubSelect().getSelectBody());
		}
		else {
			SetOperationList operationList = (SetOperationList) selectBody;
			List<SelectBody> selectBodys = operationList.getSelects();
			if (selectBodys != null && !selectBodys.isEmpty()) {
				selectBodys.forEach(this::processSelectBody);
			}
		}
	}

	/**
	 * insert 类型SQL处理
	 * @param insert jsqlparser Statement Insert
	 */
	@Override
	protected void processInsert(Insert insert, int index, String sql, Object obj) {
		// insert 暂时不处理
	}

	/**
	 * update 类型SQL处理
	 * @param update jsqlparser Statement Update
	 */
	@Override
	protected void processUpdate(Update update, int index, String sql, Object obj) {
		List<DataScope> dataScopes = (List<DataScope>) obj;
		try {
			// dataScopes 放入 ThreadLocal 方便透传
			DataScopeHolder.set(dataScopes);
			update.setWhere(this.injectExpression(update.getWhere(), update.getTable()));
		}
		finally {
			// 必须清空 ThreadLocal
			DataScopeHolder.remove();
		}
	}

	/**
	 * delete 类型SQL处理
	 * @param delete jsqlparser Statement Delete
	 */
	@Override
	protected void processDelete(Delete delete, int index, String sql, Object obj) {
		List<DataScope> dataScopes = (List<DataScope>) obj;
		try {
			// dataScopes 放入 ThreadLocal 方便透传
			DataScopeHolder.set(dataScopes);
			delete.setWhere(this.injectExpression(delete.getWhere(), delete.getTable()));
		}
		finally {
			// 必须清空 ThreadLocal
			DataScopeHolder.remove();
		}
	}

	/**
	 * 处理 PlainSelect
	 */
	protected void processPlainSelect(PlainSelect plainSelect) {
		FromItem fromItem = plainSelect.getFromItem();
		Expression where = plainSelect.getWhere();
		processWhereSubSelect(where);
		if (fromItem instanceof Table) {
			Table fromTable = (Table) fromItem;
			// #1186 github
			plainSelect.setWhere(injectExpression(where, fromTable));
		}
		else {
			processFromItem(fromItem);
		}
		// #3087 github
		List<SelectItem> selectItems = plainSelect.getSelectItems();
		if (selectItems != null && !selectItems.isEmpty()) {
			selectItems.forEach(this::processSelectItem);
		}
		List<Join> joins = plainSelect.getJoins();
		if (joins != null && !joins.isEmpty()) {
			joins.forEach(j -> {
				processJoin(j);
				processFromItem(j.getRightItem());
			});
		}
	}

	/**
	 * 处理where条件内的子查询
	 * <p>
	 * 支持如下: 1. in 2. = 3. > 4. < 5. >= 6. <= 7. <> 8. EXISTS 9. NOT EXISTS
	 * <p>
	 * 前提条件: 1. 子查询必须放在小括号中 2. 子查询一般放在比较操作符的右边
	 * @param where where 条件
	 */
	protected void processWhereSubSelect(Expression where) {
		if (where == null) {
			return;
		}
		if (where instanceof FromItem) {
			processFromItem((FromItem) where);
			return;
		}
		if (where.toString().indexOf("SELECT") > 0) {
			// 有子查询
			if (where instanceof BinaryExpression) {
				// 比较符号 , and , or , 等等
				BinaryExpression expression = (BinaryExpression) where;
				processWhereSubSelect(expression.getLeftExpression());
				processWhereSubSelect(expression.getRightExpression());
			}
			else if (where instanceof InExpression) {
				// in
				InExpression expression = (InExpression) where;
				ItemsList itemsList = expression.getRightItemsList();
				if (itemsList instanceof SubSelect) {
					processSelectBody(((SubSelect) itemsList).getSelectBody());
				}
			}
			else if (where instanceof ExistsExpression) {
				// exists
				ExistsExpression expression = (ExistsExpression) where;
				processWhereSubSelect(expression.getRightExpression());
			}
			else if (where instanceof NotExpression) {
				// not exists
				NotExpression expression = (NotExpression) where;
				processWhereSubSelect(expression.getExpression());
			}
			else if (where instanceof Parenthesis) {
				Parenthesis expression = (Parenthesis) where;
				processWhereSubSelect(expression.getExpression());
			}
		}
	}

	protected void processSelectItem(SelectItem selectItem) {
		if (selectItem instanceof SelectExpressionItem) {
			SelectExpressionItem selectExpressionItem = (SelectExpressionItem) selectItem;
			if (selectExpressionItem.getExpression() instanceof SubSelect) {
				processSelectBody(((SubSelect) selectExpressionItem.getExpression()).getSelectBody());
			}
			else if (selectExpressionItem.getExpression() instanceof Function) {
				processFunction((Function) selectExpressionItem.getExpression());
			}
		}
	}

	/**
	 * 处理函数
	 * <p>
	 * 支持: 1. select fun(args..) 2. select fun1(fun2(args..),args..)
	 * <p>
	 * <p>
	 * fixed gitee pulls/141
	 * </p>
	 * @param function
	 */
	protected void processFunction(Function function) {
		ExpressionList parameters = function.getParameters();
		if (parameters != null) {
			parameters.getExpressions().forEach(expression -> {
				if (expression instanceof SubSelect) {
					processSelectBody(((SubSelect) expression).getSelectBody());
				}
				else if (expression instanceof Function) {
					processFunction((Function) expression);
				}
			});
		}
	}

	/**
	 * 处理子查询等
	 */
	protected void processFromItem(FromItem fromItem) {
		if (fromItem instanceof SubJoin) {
			SubJoin subJoin = (SubJoin) fromItem;
			if (subJoin.getJoinList() != null) {
				subJoin.getJoinList().forEach(this::processJoin);
			}
			if (subJoin.getLeft() != null) {
				processFromItem(subJoin.getLeft());
			}
		}
		else if (fromItem instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) fromItem;
			if (subSelect.getSelectBody() != null) {
				processSelectBody(subSelect.getSelectBody());
			}
		}
		else if (fromItem instanceof ValuesList) {
			log.debug("Perform a subquery, if you do not give us feedback");
		}
		else if (fromItem instanceof LateralSubSelect) {
			LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
			if (lateralSubSelect.getSubSelect() != null) {
				SubSelect subSelect = lateralSubSelect.getSubSelect();
				if (subSelect.getSelectBody() != null) {
					processSelectBody(subSelect.getSelectBody());
				}
			}
		}
	}

	/**
	 * 处理联接语句
	 */
	protected void processJoin(Join join) {
		if (join.getRightItem() instanceof Table) {
			Table fromTable = (Table) join.getRightItem();
			join.setOnExpression(injectExpression(join.getOnExpression(), fromTable));
		}
	}

	/**
	 * 根据 DataScope ，将数据过滤的表达式注入原本的 where/or 条件
	 * @param currentExpression Expression where/or
	 * @param table 表信息
	 * @return 修改后的 where/or 条件
	 */
	private Expression injectExpression(Expression currentExpression, Table table) {
		// 获取表名
		String tableName = getTableName(table.getName());

		List<DataScope> dataScopes = DataScopeHolder.get();
		Expression dataFilterExpression = dataScopes.stream().filter(x -> x.getTableNames().contains(tableName))
				.map(x -> x.getExpression(tableName, table.getAlias())).filter(Objects::nonNull)
				.reduce(AndExpression::new).orElse(null);

		if (currentExpression == null) {
			return dataFilterExpression;
		}
		if (dataFilterExpression == null) {
			return currentExpression;
		}
		if (currentExpression instanceof OrExpression) {
			return new AndExpression(new Parenthesis(currentExpression), dataFilterExpression);
		}
		else {
			return new AndExpression(currentExpression, dataFilterExpression);
		}
	}

	/**
	 * 兼容 mysql 转义表名 `t_xxx`
	 * @param tableName 表名
	 * @return 去除转移字符后的表名
	 */
	protected static String getTableName(String tableName) {
		if (tableName.startsWith(MYSQL_ESCAPE_CHARACTER) && tableName.endsWith(MYSQL_ESCAPE_CHARACTER)) {
			tableName = tableName.substring(1, tableName.length() - 1);
		}
		return tableName;
	}


	/**
	 * 根据当前表是否有别名，动态对字段名前添加表别名 eg. 表名： table_1 as t 原始字段：column1 返回： t.column1
	 * @param table 表信息
	 * @param column 字段名
	 * @return 原始字段名，或者添加了表别名的字段名
	 */
	protected Column getAliasColumn(Table table, String column) {
		StringBuilder columnBuilder = new StringBuilder();
		if (table.getAlias() != null) {
			columnBuilder.append(table.getAlias().getName()).append(".");
		}
		columnBuilder.append(column);
		return new Column(columnBuilder.toString());
	}

}
