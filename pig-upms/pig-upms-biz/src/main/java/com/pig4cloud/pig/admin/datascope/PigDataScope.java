package com.pig4cloud.pig.admin.datascope;

import cn.hutool.core.collection.CollectionUtil;
import com.pig4cloud.pig.common.security.datascope.UserDataScope;
import com.pig4cloud.pig.common.security.service.PigUser;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import com.pigcloud.pig.common.datascope.DataScope;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 数据权限控制，要求表至少有个 user_id 字段
 *
 * @author hccake
 */
@Component
public class PigDataScope implements DataScope {

	private static final String USER_ID = "user_id";

	private static final String DEPT_ID = "dept_id";

	/**
	 * 拥有 dept_id 字段的表名集合
	 */
	private static final Set<String> DEPT_ID_TABLE_NAMES = CollectionUtil.newHashSet("sys_user");

	/**
	 * 仅拥有 user_id 字段的表名集合
	 */
	private static final Set<String> USER_ID_TABLE_NAMES = CollectionUtil.newHashSet();

	private final Set<String> tableNames;

	public PigDataScope() {
		Set<String> set = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
		set.addAll(DEPT_ID_TABLE_NAMES);
		set.addAll(USER_ID_TABLE_NAMES);
		this.tableNames = Collections.unmodifiableSet(set);
	}

	@Override
	public String getResource() {
		return "userData";
	}

	@Override
	public Collection<String> getTableNames() {
		return this.tableNames;
	}

	@Override
	public Expression getExpression(String tableName, Alias tableAlias) {
		// 获取当前登录用户
		PigUser user = SecurityUtils.getUser();
		if (user == null) {
			return null;
		}

		UserDataScope userDataScope = user.getUserDataScope();

		// 如果数据权限是全部，直接放行
		if (userDataScope.isAllScope()) {
			return null;
		}

		// 如果数据权限是仅自己
		if (userDataScope.isOnlySelf()) {
			// 数据权限规则，where user_id = xx
			return userIdEqualsToExpression(tableAlias, user.getId());
		}

		// 如果当前表有部门字段，则优先使用部门字段控制范围
		if (DEPT_ID_TABLE_NAMES.contains(tableName)) {
			// 数据权限规则，where (user_id =xx or dept_id in ("x"，"y"))
			EqualsTo equalsTo = userIdEqualsToExpression(tableAlias, user.getId());
			Expression inExpression = getInExpression(tableAlias, DEPT_ID, userDataScope.getScopeDeptIds());
			// 这里一定要加括号，否则如果有其他查询条件，or 会出问题
			return new Parenthesis(new OrExpression(equalsTo, inExpression));
		}
		else {
			// 数据权限规则，where user_id in ("x"，"y")
			return getInExpression(tableAlias, USER_ID, userDataScope.getScopeUserIds());
		}
	}

	private EqualsTo userIdEqualsToExpression(Alias tableAlias, Integer userId) {
		Column column = new Column(tableAlias == null ? USER_ID : tableAlias.getName() + "." + USER_ID);
		return new EqualsTo(column, new LongValue(userId));
	}

	private Expression getInExpression(Alias tableAlias, String columnName, Set<Integer> scopeUserIds) {
		Column column = new Column(tableAlias == null ? columnName : tableAlias.getName() + "." + columnName);
		ExpressionList expressionList = new ExpressionList();
		List<Expression> list = scopeUserIds.stream().map(LongValue::new).collect(Collectors.toList());
		expressionList.setExpressions(list);
		return new InExpression(column, expressionList);
	}

}
