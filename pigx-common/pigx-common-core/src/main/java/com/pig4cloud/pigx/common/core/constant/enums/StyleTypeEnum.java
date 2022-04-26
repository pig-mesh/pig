package com.pig4cloud.pigx.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lengleng
 * @date 2020-01-19
 * <p>
 * 前端类型类型
 */
@Getter
@AllArgsConstructor
public enum StyleTypeEnum {

	/**
	 * 前端类型-avue 风格
	 */
	AVUE("0", "avue 风格"),

	/**
	 * 前端类型-element 风格
	 */
	ELEMENT("1", "element 风格"),

	/**
	 * 前端风格-uview 风格
	 */
	UVIEW("2", "uview 风格");

	/**
	 * 类型
	 */
	private String style;

	/**
	 * 描述
	 */
	private String description;

}
