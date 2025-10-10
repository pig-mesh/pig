package com.pig4cloud.pigx.flow.support.expression.condition.impl;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.flow.support.expression.condition.NodeConditionStrategy;
import com.pig4cloud.pigx.flow.dto.Condition;
import org.springframework.stereotype.Component;

/**
 * 输入框（字符串）类型条件处理器
 * <p>
 * 处理输入框类型表单控件的条件表达式生成。
 * 支持的比较操作：
 * 1. 相等（==）：检查字符串是否完全相等
 * 2. 不相等（!=）：检查字符串是否不相等
 * 3. 包含（contain）：检查字符串是否包含指定子串
 * 4. 不包含（notcontain）：检查字符串是否不包含指定子串
 *
 * @author pigx
 */
@Component("inputNodeConditionStrategy")
public class InputNodeConditionStrategy implements NodeConditionStrategy {

	/**
	 * 处理输入框类型的条件表达式
	 * <p>
	 * 根据不同的比较操作符生成对应的SpEL表达式，
	 * 表达式会调用ExpressionHandler中的相应方法进行字符串比较。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、比较值等信息
	 * @return SpEL表达式字符串，如果操作符不支持则返回"(2==2)"
	 */
	@Override
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();
		if (StrUtil.equals(compare, "==")) {
			return StrUtil.format("(expressionHandler.stringEqual(\"{}\",\"{}\",execution))", id, value);
		}
		if (StrUtil.equals(compare, "contain")) {
			return StrUtil.format("(expressionHandler.stringContain(\"{}\",\"{}\",execution))", id, value);
		}
		if (StrUtil.equals(compare, "notcontain")) {
			return StrUtil.format("(!expressionHandler.stringContain(\"{}\",\"{}\",execution))", id, value);
		}
		if (StrUtil.equals(compare, "!=")) {
			return StrUtil.format("(!expressionHandler.stringEqual(\"{}\",\"{}\",execution))", id, value);
		}
		return "(2==2)";
	}

}
