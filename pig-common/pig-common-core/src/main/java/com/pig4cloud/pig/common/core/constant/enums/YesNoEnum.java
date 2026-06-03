package com.pig4cloud.pig.common.core.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * yes and no enum
 *
 * @author lengleng
 * @date 2023/10/25
 */
@Getter
@RequiredArgsConstructor
public enum YesNoEnum {

	YES("1", "是"), NO("0", "否");

	/**
	 * 编码
	 */
	private final String code;

	/**
	 * 描述
	 */
	private final String desc;

	/**
	 * 通过布尔值获取代码
	 * @param bool 布尔
	 * @return {@link String }
	 */
	public static String getCode(Boolean bool) {
		return bool ? YES.getCode() : NO.getCode();
	}

}
