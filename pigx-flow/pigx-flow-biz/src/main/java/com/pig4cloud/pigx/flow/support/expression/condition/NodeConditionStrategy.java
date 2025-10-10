package com.pig4cloud.pigx.flow.support.expression.condition;

import com.pig4cloud.pigx.flow.dto.Condition;

/**
 * 节点条件策略接口
 * <p>
 * 定义了流程节点条件处理的策略模式接口。
 * 该接口是策略模式的核心，不同的表单控件类型（如文本、数字、日期、选择器等）
 * 都需要实现该接口，以提供相应的条件表达式生成逻辑。
 * <p>
 * 实现类将根据不同的表单控件类型和条件配置，生成对应的SpEL表达式，
 * 这些表达式将在流程执行时被Flowable引擎解析和执行，用于判断流程的走向。
 *
 * @author pigx
 * @see com.pig4cloud.pigx.flow.dto.Condition 条件配置对象
 */
public interface NodeConditionStrategy {

	/**
	 * 处理条件表达式
	 * <p>
	 * 根据传入的条件配置对象，生成对应的SpEL（Spring Expression Language）表达式字符串。
	 * 生成的表达式会在流程运行时被评估，用于决定流程的条件分支走向。
	 *
	 * @param condition 条件配置对象，包含字段名、操作符、比较值等信息
	 * @return 生成的SpEL表达式字符串，例如："${expressionHandler.numberCompare('age', '>', 18, execution)}"
	 */
	String handle(Condition condition);

}
