package com.pig4cloud.pig.codegen.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lengleng
 * @date 2023/3/12
 * <p>
 * 通用字段的填充策略和显示策略
 */
@Getter
@RequiredArgsConstructor
public enum CommonColumnFiledEnum {

	/**
	 * create_by 字段
	 */
	create_by("0", "0", AutoFillEnum.INSERT.name(), 100),

	/**
	 * create_time 字段
	 */
	create_time("0", "0", AutoFillEnum.INSERT.name(), 101),
	/**
	 * update_by 字段
	 */
	update_by("0", "0", AutoFillEnum.INSERT_UPDATE.name(), 102),
	/**
	 * update_time 字段
	 */
	update_time("0", "0", AutoFillEnum.INSERT_UPDATE.name(), 103),
	/**
	 * del_flag 字段
	 */
	del_flag("0", "0", AutoFillEnum.DEFAULT.name(), 104);

	/**
	 * 表单是否默认显示 1/0
	 */
	private final String formItem;

	/**
	 * 表格是否默认显示 1/0
	 */
	private final String gridItem;

	/**
	 * 自动填充策略
	 */
	private final String autoFill;

	/**
	 * 排序值
	 */
	private final Integer sort;

}
