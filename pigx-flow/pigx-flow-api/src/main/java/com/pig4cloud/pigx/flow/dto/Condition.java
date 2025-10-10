package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

/**
 * 流程条件数据传输对象
 * <p>
 * 该DTO用于定义流程节点的单个执行条件。条件由键、表达式和值组成，
 * 用于判断流程是否满足某个分支或节点的执行要求。
 * 常用于条件分支节点，根据表单数据判断流程走向。
 * </p>
 * 
 * @author pigx
 */
@Data
public class Condition {

	/**
	 * 条件键（字段标识）
	 * <p>
	 * 对应表单中的字段ID或变量名，如 "amount"、"deptId" 等。
	 * 用于标识需要判断的数据字段。
	 * </p>
	 */
	private String key;

	/**
	 * 条件表达式
	 * <p>
	 * 定义判断逻辑的表达式，支持的表达式类型包括：
	 * - EQ: 等于
	 * - NE: 不等于
	 * - GT: 大于
	 * - GE: 大于等于
	 * - LT: 小于
	 * - LE: 小于等于
	 * - IN: 包含在
	 * - NOT_IN: 不包含在
	 * - CONTAINS: 包含（字符串）
	 * - NOT_CONTAINS: 不包含（字符串）
	 * - EMPTY: 为空
	 * - NOT_EMPTY: 不为空
	 * </p>
	 */
	private String expression;

	/**
	 * 条件值
	 * <p>
	 * 与表达式配合使用的比较值。
	 * 类型可以是字符串、数字、日期、数组等，具体取决于字段类型和表达式。
	 * 例如：金额大于1000时，value=1000；部门包含在[1,2,3]时，value=[1,2,3]
	 * </p>
	 */
	private Object value;

	/**
	 * 字段类型
	 * <p>
	 * 标识条件键对应的数据类型，用于正确解析和比较数据。
	 * 常见类型：INPUT（输入框）、NUMBER（数字）、DATE（日期）、
	 * SELECT_USER（选择用户）、SELECT_DEPT（选择部门）等
	 * </p>
	 */
	private String keyType;

}
