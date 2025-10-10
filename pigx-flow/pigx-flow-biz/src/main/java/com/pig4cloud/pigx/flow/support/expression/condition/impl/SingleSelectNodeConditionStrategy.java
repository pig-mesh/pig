package com.pig4cloud.pigx.flow.support.expression.condition.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.flow.support.expression.condition.NodeConditionStrategy;
import com.pig4cloud.pigx.flow.dto.Condition;
import com.pig4cloud.pigx.flow.dto.SelectValue;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 单选框类型条件处理器
 * <p>
 * 处理单选框（radio、下拉单选等）类型表单控件的条件表达式生成。
 * 支持的比较操作：
 * 1. 包含（in）：检查选中值是否在指定的选项列表中
 * 2. 不包含（not in）：检查选中值是否不在指定的选项列表中
 * <p>
 * 处理时会将选项值转换为字符串数组格式传递给表达式处理器。
 *
 * @author pigx
 */
@Component("SingleSelectNodeConditionStrategy")
public class SingleSelectNodeConditionStrategy implements NodeConditionStrategy {

	/**
	 * 处理单选类型的条件表达式
	 * <p>
	 * 将条件中的选项列表转换为字符串数组格式，并生成相应的SpEL表达式。
	 * 支持判断单选值是否在指定的选项集合中。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、选项值列表等信息
	 * @return SpEL表达式字符串，格式如："(expressionHandler.singleSelectHandler("status", execution,"1","2","3"))"
	 */
	@Override
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();

		List<SelectValue> list = Convert.toList(SelectValue.class, value);

		StringBuilder sb = new StringBuilder();

		for (SelectValue o : list) {
			sb.append(",\"").append(o.getKey()).append("\"");
		}
		String string = sb.toString();
		if (CollUtil.isNotEmpty(list)) {
			string = string.substring(1);
		}
		if (compare.equals("in")) {
			return StrUtil.format("(expressionHandler.singleSelectHandler(\"{}\", execution,{}))", id, string);
		}

		return StrUtil.format("(!expressionHandler.singleSelectHandler(\"{}\", execution,{}))", id, string);

	}

}
