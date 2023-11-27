package com.pig4cloud.pigx.common.core.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

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
	AVUE("0", "avue"),

	/**
	 * 前端类型-element 风格
	 */
	ELEMENT("1", "element"),

	/**
	 * 前端风格-uview 风格
	 */
	UVIEW("2", "uview"),

	/**
	 * magic
	 */
	MAGIC("3", "magic"),
	/**
	 * element-plus
	 */
	PLUS("4", "plus");

	/**
	 * 类型
	 */
	private String style;

	/**
	 * 描述
	 */
	private String description;

	public static String getDesc(String style) {
		return Arrays.stream(StyleTypeEnum.values())
			.filter(styleTypeEnum -> styleTypeEnum.getStyle().equals(style))
			.findFirst()
			.get()
			.getDescription();
	}

}
