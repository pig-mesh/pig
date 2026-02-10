package com.pig4cloud.pigx.flow.support.expression.condition.impl;

import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.dto.Condition;
import com.pig4cloud.pigx.flow.support.expression.condition.NodeConditionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * 角色选择器类型条件处理器
 * <p>
 * 处理角色选择器类型表单控件的条件表达式生成。
 * 支持判断发起人是否拥有指定的角色。
 * 支持的比较操作：
 * 1. 属于（in/==）：检查用户是否拥有指定角色中的任意一个
 * 2. 不属于（notin/!=）：检查用户是否不拥有指定角色中的任意一个
 *
 * @author pigx
 * @date 2026-02-10
 */
@RequiredArgsConstructor
@Component("SelectRoleNodeConditionStrategy")
public class SelectRoleNodeConditionStrategy implements NodeConditionStrategy {

	private final ObjectMapper objectMapper;

	/**
	 * 处理角色选择器类型的条件表达式
	 * <p>
	 * 将角色选择数据序列化为JSON格式，并生成调用角色比较方法的SpEL表达式。
	 * 序列化后的JSON会进行转义处理，以确保在表达式中的正确性。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、角色列表等信息
	 * @return SpEL表达式字符串
	 */
	@Override
	@SneakyThrows
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();

		return StrUtil.format("(expressionHandler.roleCompare(\"{}\",\"{}\",\"{}\", execution))", id,
				EscapeUtil.escape(objectMapper.writeValueAsString(value)), compare);

	}

}
