package com.pig4cloud.pigx.flow.support.expression.condition.impl;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.flow.support.expression.condition.NodeConditionStrategy;
import com.pig4cloud.pigx.flow.dto.Condition;
import org.springframework.stereotype.Component;

/**
 * 时间选择器类型条件处理器
 * <p>
 * 处理时间选择器类型表单控件的条件表达式生成。
 * 只比较时分秒部分，不包含日期。
 * 支持时间比较操作：>, <, >=, <=, ==, !=
 * 默认使用"HH:mm:ss"格式进行时间解析和比较。
 *
 * @author pigx
 */
@Component("timePickerNodeConditionStrategy")
public class TimeNodeConditionStrategy implements NodeConditionStrategy {

	/**
	 * 处理时间类型的条件表达式
	 * <p>
	 * 生成时间比较的SpEL表达式，将时间字符串转换为时间戳进行比较。
	 * 固定使用"HH:mm:ss"格式解析时间。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、比较值等信息
	 * @return SpEL表达式字符串，格式如："(expressionHandler.dateTimeCompare("startTime",">","09:00:00",execution,"HH:mm:ss"))"
	 */
	@Override
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();

		return StrUtil.format("(expressionHandler.dateTimeCompare(\"{}\",\"{}\",\"{}\",execution,\"HH:mm:ss\"))", id,
				compare, value);

	}

}
