package com.pig4cloud.pigx.flow.engine.expression.condition.impl;

import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.engine.expression.condition.NodeConditionStrategy;
import com.pig4cloud.pigx.flow.task.dto.Condition;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * 字符类型处理器
 */
@RequiredArgsConstructor
@Component("SelectUserNodeConditionStrategy")
public class SelectUserNodeConditionStrategy implements NodeConditionStrategy {

	private final ObjectMapper objectMapper;

	/**
	 * 抽象方法 处理表达式
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
