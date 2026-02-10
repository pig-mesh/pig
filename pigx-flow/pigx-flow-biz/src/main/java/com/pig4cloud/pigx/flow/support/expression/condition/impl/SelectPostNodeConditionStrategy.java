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
 * 岗位选择器类型条件处理器
 * <p>
 * 处理岗位选择器类型表单控件的条件表达式生成。
 * 支持判断发起人是否属于指定的岗位。
 * 支持的比较操作：
 * 1. 属于（in/==）：检查用户是否属于指定岗位中的任意一个
 * 2. 不属于（notin/!=）：检查用户是否不属于指定岗位中的任意一个
 *
 * @author pigx
 * @date 2026-02-10
 */
@RequiredArgsConstructor
@Component("SelectPostNodeConditionStrategy")
public class SelectPostNodeConditionStrategy implements NodeConditionStrategy {

	private final ObjectMapper objectMapper;

	/**
	 * 处理岗位选择器类型的条件表达式
	 * <p>
	 * 将岗位选择数据序列化为JSON格式，并生成调用岗位比较方法的SpEL表达式。
	 * 序列化后的JSON会进行转义处理，以确保在表达式中的正确性。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、岗位列表等信息
	 * @return SpEL表达式字符串
	 */
	@Override
	@SneakyThrows
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();

		return StrUtil.format("(expressionHandler.postCompare(\"{}\",\"{}\",\"{}\", execution))", id,
				EscapeUtil.escape(objectMapper.writeValueAsString(value)), compare);

	}

}
