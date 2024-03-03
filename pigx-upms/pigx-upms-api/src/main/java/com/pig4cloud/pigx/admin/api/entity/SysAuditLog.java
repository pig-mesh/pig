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

package com.pig4cloud.pigx.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 审计记录表
 *
 * @author PIG
 * @date 2023-02-28 20:12:23
 */
@Data
@TenantTable
@TableName("sys_audit_log")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "审计记录表")
public class SysAuditLog extends Model<SysAuditLog> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	/**
	 * 审计名称
	 */
	@Schema(description = "审计名称")
	private String auditName;

	/**
	 * 字段名称
	 */
	@Schema(description = "字段名称")
	private String auditField;

	/**
	 * 变更前值
	 */
	@Schema(description = "变更前值")
	private String beforeVal;

	/**
	 * 变更后值
	 */
	@Schema(description = "变更后值")
	private String afterVal;

	/**
	 * 操作人
	 */
	@Schema(description = "操作人")
	private String createBy;

	/**
	 * 操作时间
	 */
	@Schema(description = "操作时间")
	private LocalDateTime createTime;

	/**
	 * 删除标记
	 */
	@Schema(description = "删除标记")
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 租户ID
	 */
	@Schema(description = "租户ID")
	private Long tenantId;

}
