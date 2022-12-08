package com.pig4cloud.pigx.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTypeEnum {

	TOB("0", "面向后台应用"), TOC("1", "面向小程序");

	/**
	 * 类型
	 */
	private final String status;

	/**
	 * 描述
	 */
	private final String description;

}
