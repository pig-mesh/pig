package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

/**
 * 条件类
 */
@Data
public class Condition {

	/**
	 * 条件键
	 */
	private String key;

	/**
	 * 表达式
	 */
	private String expression;

	/**
	 * 值
	 */
	private Object value;

	/**
	 * 键类型
	 */
	private String keyType;

}
