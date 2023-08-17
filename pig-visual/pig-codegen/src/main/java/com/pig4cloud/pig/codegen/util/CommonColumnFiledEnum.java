package com.pig4cloud.pig.codegen.util;

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
	create_by(false, false, "INSERT", 100),

	/**
	 * create_time 字段
	 */
	create_time(false, false, "INSERT", 101),
	/**
	 * update_by 字段
	 */
	update_by(false, false, "INSERT_UPDATE", 102),
	/**
	 * update_time 字段
	 */
	update_time(false, false, "INSERT_UPDATE", 103),
	/**
	 * del_flag 字段
	 */
	del_flag(false, false, "DEFAULT", 104),
	/**
	 * tenant_id 字段
	 */
	tenant_id(false, false, "DEFAULT", 105);

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
