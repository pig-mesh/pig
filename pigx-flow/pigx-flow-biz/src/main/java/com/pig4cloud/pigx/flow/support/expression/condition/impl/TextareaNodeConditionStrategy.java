package com.pig4cloud.pigx.flow.support.expression.condition.impl;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.flow.support.expression.condition.NodeConditionStrategy;
import com.pig4cloud.pigx.flow.dto.Condition;
import org.springframework.stereotype.Component;

/**
 * 多行文本框类型条件处理器
 * <p>
 * 处理多行文本框（textarea）类型表单控件的条件表达式生成。
 * 与输入框处理器相比，只支持相等和不相等的比较，不支持包含判断。
 * 支持的比较操作：
 * 1. 相等（==）：检查文本是否完全相等
 * 2. 不相等（!=）：检查文本是否不相等
 *
 * @author pigx
 */
@Component("TextareaNodeConditionStrategy")
public class TextareaNodeConditionStrategy implements NodeConditionStrategy {

	/**
	 * 处理多行文本框类型的条件表达式
	 * <p>
	 * 根据比较操作符生成对应的SpEL表达式，只支持相等和不相等的判断。
	 * 如果操作符不支持，则返回永真表达式"(2==2)"。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、比较值等信息
	 * @return SpEL表达式字符串，格式如："(expressionHandler.stringEqual("remark","备注内容",execution))"
	 */
	@Override
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();
		if (StrUtil.equals(compare, "==")) {
			return StrUtil.format("(expressionHandler.stringEqual(\"{}\",\"{}\",execution))", id, value);
		}
		if (StrUtil.equals(compare, "!=")) {
			return StrUtil.format("(!expressionHandler.stringEqual(\"{}\",\"{}\",execution))", id, value);
		}
		return "(2==2)";
	}

}
