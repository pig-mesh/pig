/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pig.common.data.datascope;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限逻辑组合类型
 *
 * @author lengleng
 * @date 2025-12-10
 */
@Getter
@AllArgsConstructor
public enum DataScopeLogicEnum {

	/**
	 * OR逻辑 - 满足任一条件即可（默认） 示例: WHERE create_by='user' OR dept_id IN (1,2,3)
	 */
	OR("OR", "或"),

	/**
	 * AND逻辑 - 需同时满足所有条件 示例: WHERE create_by='user' AND dept_id IN (1,2,3)
	 */
	AND("AND", "且");

	/**
	 * SQL关键字
	 */
	private final String keyword;

	/**
	 * 描述
	 */
	private final String description;

}
