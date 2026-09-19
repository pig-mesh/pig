/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
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

package com.pig4cloud.pig.common.data.util;

import java.util.regex.Pattern;

/**
 * 前端排序字段校验。仅接受字段名或表别名限定的字段名，不接受 SQL 表达式。
 * <p>
 * 本工具只校验字段格式，业务层仍需限制允许排序的字段。
 */
public final class SqlSortUtils {

	private static final Pattern SORT_COLUMN = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*(\\.[A-Za-z_][A-Za-z0-9_]*)?");

	private SqlSortUtils() {
	}

	/**
	 * 校验单个排序字段。多个字段应由调用方拆分后分别校验。
	 * @param column 前端传入的排序字段
	 * @return 是否为合法的字段名或别名限定字段名
	 */
	public static boolean isValidColumn(String column) {
		return column != null && SORT_COLUMN.matcher(column).matches();
	}

}
