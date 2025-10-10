package com.pig4cloud.pigx.flow.support.expression.condition.impl;

import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.support.expression.condition.NodeConditionStrategy;
import com.pig4cloud.pigx.flow.dto.Condition;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * 用户选择器类型条件处理器
 * <p>
 * 处理用户选择器类型表单控件的条件表达式生成。
 * 支持判断选中的用户是否在指定的用户/部门列表中。
 * 支持的比较操作：
 * 1. 属于（in/==）：检查用户是否在指定的用户列表中，或属于指定的部门
 * 2. 不属于（notin/!=）：检查用户是否不在指定的用户列表中，且不属于指定的部门
 * <p>
 * 特别说明：当条件中包含部门时，会自动查询该部门下的所有用户进行比较。
 *
 * @author pigx
 */
@RequiredArgsConstructor
@Component("SelectUserNodeConditionStrategy")
public class SelectUserNodeConditionStrategy implements NodeConditionStrategy {

	private final ObjectMapper objectMapper;

	/**
	 * 处理用户选择器类型的条件表达式
	 * <p>
	 * 将用户/部门选择数据序列化为JSON格式，并生成调用用户比较方法的SpEL表达式。
	 * 序列化后的JSON会进行转义处理，以确保在表达式中的正确性。
	 * 支持混合选择用户和部门，系统会自动展开部门为用户列表。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、用户/部门列表等信息
	 * @return SpEL表达式字符串，格式如："(expressionHandler.userCompare("assignee","[{\"id\":1,\"type\":\"USER\"}]","in", execution))"
	 * @throws Exception 当JSON序列化失败时抛出异常
	 */
	@Override
	@SneakyThrows
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();

		return StrUtil.format("(expressionHandler.userCompare(\"{}\",\"{}\",\"{}\", execution))", id,
				EscapeUtil.escape(objectMapper.writeValueAsString(value)), compare);

	}

}
