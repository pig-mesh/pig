package com.pig4cloud.pigx.flow.engine.expression.condition;

import com.pig4cloud.pigx.flow.task.dto.Condition;

/**
 * 节点单个条件处理器
 */
public interface NodeConditionStrategy {

	/**
	 * 抽象方法 处理表达式
	 */
	String handle(Condition condition);

}
