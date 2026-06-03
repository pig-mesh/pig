package com.pig4cloud.pig.codegen.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * vfrom 字段类型
 *
 * @author lengleng
 * @date 2023/6/5
 */
@Getter
@RequiredArgsConstructor
public enum VFormTypeEnum {

	GRID("grid"),

	GRID_COL("grid-col");

	private final String type;

}
