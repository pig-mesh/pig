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

package com.pig4cloud.pig.codegen.entity;

import lombok.Data;

import java.util.List;

/**
 * @author lengleng
 * @date 2018/07/29 表属性： https://blog.csdn.net/lkforce/article/details/79557482
 */
@Data
public class TableEntity {

	/**
	 * 名称
	 */
	private String tableName;

	/**
	 * 备注
	 */
	private String comments;

	/**
	 * 主键
	 */
	private ColumnEntity pk;

	/**
	 * 列名
	 */
	private List<ColumnEntity> columns;

	/**
	 * 驼峰类型
	 */
	private String caseClassName;

	/**
	 * 普通类型
	 */
	private String lowerClassName;

	/**
	 * 数据库类型 （用于根据数据库个性化）
	 */
	private String dbType;

}
