package com.pig4cloud.pig.codegen.util.vo;
/*
 *      Copyright (c) 2018-2025, luolin All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: luolin (766488893@qq.com)
 */

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 自动创建表管理
 *
 * @author luolin
 * @date 2022-09-23 21:56:11
 */
@Data
@Schema(description = "自动创建表管理")
public class GenCreateTableVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Long id;

	/**
	 * 表名称
	 */
	@NotBlank(message = "表名称不能为空")
	@Schema(description = "表名称")
	private String tableName;

	/**
	 * 表注释
	 */
	@NotBlank(message = "表注释不能为空")
	@Schema(description = "表注释")
	private String comments;

	/**
	 * 数据源名称
	 */
	@NotBlank(message = "数据源名称不能为空")
	@Schema(description = "数据源名称")
	private String dsName;

	/**
	 * 主键策略
	 */
	@NotBlank(message = "主键策略不能为空")
	@Schema(description = "主键策略")
	private String pkPolicy;

	/**
	 * 创建人
	 */
	@Schema(description = "创建人")
	private Long createUser;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 表字段信息
	 */
	@Schema(description = "表字段信息")
	private String columnsInfo;

	/**
	 * 字段信息
	 */
	@Schema(description = "字段信息")
	private String columnInfo;

}
