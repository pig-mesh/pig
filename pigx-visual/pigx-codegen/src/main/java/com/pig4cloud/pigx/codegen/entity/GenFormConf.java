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

package com.pig4cloud.pigx.codegen.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 生成记录
 *
 * @author lengleng
 * @date 2019-08-12 15:55:35
 */
@Data
@TenantTable
@TableName("gen_form_conf")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "生成记录")
public class GenFormConf extends Model<GenFormConf> {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "ID")
	private Long id;

	/**
	 * 数据源名称
	 */
	@Schema(description = "数据源名称")
	private String dsName;

	/**
	 * 表名称
	 */
	@Schema(description = "表名称")
	private String tableName;

	/**
	 * 表单信息
	 */
	@Schema(description = "表单信息")
	private String formInfo;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 0-正常，1-删除
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 所属租户
	 */
	@Schema(description = "所属租户", hidden = true)
	private Long tenantId;

}
