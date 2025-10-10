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
 * 部门选择器类型条件处理器
 * <p>
 * 处理部门选择器类型表单控件的条件表达式生成。
 * 支持判断选中的部门是否在指定的部门列表中。
 * 支持的比较操作：
 * 1. 属于（in/==）：检查部门是否在指定的部门列表中
 * 2. 不属于（notin/!=）：检查部门是否不在指定的部门列表中
 * <p>
 * 处理时会将部门数据序列化为JSON字符串并进行转义处理。
 *
 * @author pigx
 */
@RequiredArgsConstructor
@Component("SelectDeptNodeConditionStrategy")
public class SelectDeptNodeConditionStrategy implements NodeConditionStrategy {

	private final ObjectMapper objectMapper;

	/**
	 * 处理部门选择器类型的条件表达式
	 * <p>
	 * 将部门选择数据序列化为JSON格式，并生成调用部门比较方法的SpEL表达式。
	 * 序列化后的JSON会进行转义处理，以确保在表达式中的正确性。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、部门列表等信息
	 * @return SpEL表达式字符串，格式如："(expressionHandler.deptCompare("dept","[{\"id\":1,\"name\":\"研发部\"}]","in", execution))"
	 * @throws Exception 当JSON序列化失败时抛出异常
	 */
	@Override
	@SneakyThrows
	public String handle(Condition condition) {

		String compare = condition.getExpression();
		String id = condition.getKey();
		Object value = condition.getValue();

		return StrUtil.format("(expressionHandler.deptCompare(\"{}\",\"{}\",\"{}\", execution))", id,
				EscapeUtil.escape(objectMapper.writeValueAsString(value)), compare);

	}

}
