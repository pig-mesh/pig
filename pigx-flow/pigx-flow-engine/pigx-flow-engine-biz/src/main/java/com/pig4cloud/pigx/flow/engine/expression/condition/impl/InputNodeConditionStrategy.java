package com.pig4cloud.pigx.flow.engine.expression.condition.impl;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.flow.engine.expression.condition.NodeConditionStrategy;
import com.pig4cloud.pigx.flow.task.dto.Condition;
import org.springframework.stereotype.Component;

/**
 * 字符类型处理器
 */
@Component("InputNodeConditionStrategy")
public class InputNodeConditionStrategy implements NodeConditionStrategy {

	/**
	 * 抽象方法 处理表达式
	 *
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
