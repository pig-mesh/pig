package com.pig4cloud.pig.codegen.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * boolean 类型枚举
 *
 */

@Getter
@RequiredArgsConstructor
public enum BoolFillEnum {

	/**
	 * true
	 */
	TRUE("1"),
	/**
	 * false
	 */
	FALSE("0");

	private final String value;

}
