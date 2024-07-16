package com.pig4cloud.pigx.codegen.util;

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

	VFORM_JSON(1L, "vform.json"),

	VFORM_VUE(2L, "vform.vue");

	/**
	 * 对应模板ID
	 */
	private Long templateId;

	/**
	 * 描述
	 */
	private String desc;

}
