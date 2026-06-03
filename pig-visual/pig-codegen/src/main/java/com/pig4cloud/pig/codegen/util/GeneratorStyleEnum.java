package com.pig4cloud.pig.codegen.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 代码生成主题
 *
 * @author 冷冷
 */
@Getter
@RequiredArgsConstructor
public enum GeneratorStyleEnum {

	VFORM_JSON(1L, "vform.json"),

	VFORM_VUE(2L, "vform.vue");

	/**
	 * 对应模板ID
	 */
	private final Long templateId;

	/**
	 * 描述
	 */
	private final String desc;

}
