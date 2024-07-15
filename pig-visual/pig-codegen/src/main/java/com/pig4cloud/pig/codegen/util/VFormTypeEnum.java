package com.pig4cloud.pig.codegen.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * vfrom 字段类型
 *
 * @author lengleng
 * @date 2023/6/5
 */
@Getter
@AllArgsConstructor
public enum VFormTypeEnum {

	GRID("grid"),

	GRID_COL("grid-col");

	private final String type;

}
