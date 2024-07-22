package com.pig4cloud.pig.codegen.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 代码生成主题
 *
 * @author 冷冷
 */
@Getter
@AllArgsConstructor
public enum GeneratorStyleEnum {

	VFORM_JSON(1L, "element-plus 风格"),

	VFORM_FORM(2L, "uview 风格");

	/**
	 * 对应模板ID
	 */
	private Long templateId;

	/**
	 * 描述
	 */
	private String desc;

}
