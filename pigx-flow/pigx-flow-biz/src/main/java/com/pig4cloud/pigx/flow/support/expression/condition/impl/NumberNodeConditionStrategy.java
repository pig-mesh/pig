package com.pig4cloud.pigx.flow.support.expression.condition.impl;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.flow.support.expression.condition.NodeConditionStrategy;
import com.pig4cloud.pigx.flow.dto.Condition;
import org.springframework.stereotype.Component;

/**
 * 数字输入框类型条件处理器
 * <p>
 * 处理数字输入框类型表单控件的条件表达式生成。
 * 支持所有数字比较操作符：>, <, >=, <=, ==, !=
 * 生成的表达式会调用ExpressionHandler的numberCompare方法进行数值比较。
 *
 * @author pigx
 */
@Component("inputNumberNodeConditionStrategy")
public class NumberNodeConditionStrategy implements NodeConditionStrategy {

	/**
	 * 处理数字类型的条件表达式
	 * <p>
	 * 生成数字比较的SpEL表达式，支持各种数值比较操作。
	 * 表达式中会将字段值转换为数字类型后进行比较。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、比较值等信息
	 * @return SpEL表达式字符串，格式如："(expressionHandler.numberCompare("age",">",18,execution))"
	 */
	@Override
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();

		return StrUtil.format("(expressionHandler.numberCompare(\"{}\",\"{}\",{},execution))", id, compare, value);

	}

}
