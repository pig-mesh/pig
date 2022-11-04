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

package com.pig4cloud.pigx.common.data.datascope;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author lengleng
 * @date 2018/8/30 数据权限查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataScope extends HashMap {

	/**
	 * 限制范围的字段名称
	 */
	private String scopeDeptName = "dept_id";

	/**
	 * 本人权限范围字段
	 */
	private String scopeUserName = "create_by";

	/**
	 * 具体的数据范围
	 */
	private List<Long> deptList = new ArrayList<>();

	/**
	 * 具体查询的用户数据权限范围
	 */
	private String username;

	/**
	 * 是否只查询本部门
	 */
	private Boolean isOnly = false;

	/**
	 * 函数名称，默认 SELECT * ;
	 *
	 * <ul>
	 * <li>COUNT(1)</li>
	 * </ul>
	 */
	private DataScopeFuncEnum func = DataScopeFuncEnum.ALL;

	/**
	 * of 获取实例
	 */
	public static DataScope of() {
		return new DataScope();
	}

	public DataScope deptIds(List<Long> deptIds) {
		this.deptList = deptIds;
		return this;
	}

	public DataScope only(boolean isOnly) {
		this.isOnly = isOnly;
		return this;
	}

	public DataScope func(DataScopeFuncEnum func) {
		this.func = func;
		return this;
	}

}
