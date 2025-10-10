package com.pig4cloud.pigx.flow.support.expression.condition.impl;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.flow.support.expression.condition.NodeConditionStrategy;
import com.pig4cloud.pigx.flow.dto.Condition;
import org.springframework.stereotype.Component;

/**
 * 日期选择器类型条件处理器
 * <p>
 * 处理日期选择器类型表单控件的条件表达式生成。
 * 支持日期比较操作：>, <, >=, <=, ==, !=
 * 默认使用"yyyy-MM-dd"格式进行日期解析和比较。
 *
 * @author pigx
 */
@Component("datePickerNodeConditionStrategy")
public class DateNodeConditionStrategy implements NodeConditionStrategy {

	/**
	 * 处理日期类型的条件表达式
	 * <p>
	 * 生成日期比较的SpEL表达式，将日期字符串转换为时间戳进行比较。
	 * 固定使用"yyyy-MM-dd"格式解析日期。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、比较值等信息
	 * @return SpEL表达式字符串，格式如："(expressionHandler.dateTimeCompare("birthday",">","2023-01-01",execution,"yyyy-MM-dd"))"
	 */
	@Override
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();

		return StrUtil.format("(expressionHandler.dateTimeCompare(\"{}\",\"{}\",\"{}\",execution,\"yyyy-MM-dd\"))", id,
				compare, value);

	}

}
