package com.pig4cloud.pigx.codegen.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lengleng
 * @date 2023/3/12
 * <p>
 * 通用字段的填充策略和显示策略
 */
@Getter
@AllArgsConstructor
public enum CommonColumnFiledEnum {

	/**
	 * create_by 字段
	 */
	create_by(false, false, AutoFillEnum.INSERT.name(), 100),

	/**
	 * create_time 字段
	 */
	create_time(false, false, AutoFillEnum.INSERT.name(), 101),
	/**
	 * update_by 字段
	 */
	update_by(false, false, AutoFillEnum.INSERT_UPDATE.name(), 102),
	/**
	 * update_time 字段
	 */
	update_time(false, false, AutoFillEnum.INSERT_UPDATE.name(), 103),
	/**
	 * del_flag 字段
	 */
	del_flag(false, false, AutoFillEnum.DEFAULT.name(), 104),
	/**
	 * tenant_id 字段
	 */
	tenant_id(false, false, AutoFillEnum.DEFAULT.name(), 105);

	/**
	 * 表单是否默认显示
	 */
	private Boolean formItem;

	/**
	 * 表格是否默认显示
	 */
	private Boolean gridItem;

	/**
	 * 自动填充策略
	 */
	private String autoFill;

	/**
	 * 排序值
	 */
	private Integer sort;

}
