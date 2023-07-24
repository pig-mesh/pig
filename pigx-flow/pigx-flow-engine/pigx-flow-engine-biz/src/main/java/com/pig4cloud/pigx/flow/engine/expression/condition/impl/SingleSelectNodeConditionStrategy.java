package com.pig4cloud.pigx.flow.engine.expression.condition.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.flow.engine.expression.condition.NodeConditionStrategy;
import com.pig4cloud.pigx.flow.task.dto.Condition;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字符类型处理器
 */
@Component("SingleSelectNodeConditionStrategy")
public class SingleSelectNodeConditionStrategy implements NodeConditionStrategy {

	/**
	 * 抽象方法 处理表达式
	 */
	@Override
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();

		List<?> list = Convert.toList(value);

		StringBuilder sb = new StringBuilder();

		for (Object o : list) {
			sb.append(",\"").append(o.toString()).append("\"");
		}
		String string = sb.toString();
		if (CollUtil.isNotEmpty(list)) {
			string = string.substring(1);
		}
		if (compare.equals("in")) {
			return StrUtil.format("(expressionHandler.stringContain(\"{}\", execution,{}))", id, string);
		}

		return StrUtil.format("(!expressionHandler.stringContain(\"{}\", execution,{}))", id, string);

	}

}
