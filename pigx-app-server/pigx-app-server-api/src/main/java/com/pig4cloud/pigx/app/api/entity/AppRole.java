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

package com.pig4cloud.pigx.app.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * app角色表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@Data
@TenantTable
@TableName("app_role")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "app角色表")
public class AppRole extends Model<AppRole> {

	private static final long serialVersionUID = 1L;

	/**
	 * roleId
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "roleId")
	private Long roleId;

	/**
	 * roleName
	 */
	@Schema(description = "roleName")
	private String roleName;

	/**
	 * roleCode
	 */
	@Schema(description = "roleCode")
	private String roleCode;

	/**
	 * roleDesc
	 */
	@Schema(description = "roleDesc")
	private String roleDesc;

	/**
	 * 创建人
	 */
	@Schema(description = "创建人")
	private String createBy;

	/**
	 * 修改人
	 */
	@Schema(description = "修改人")
	private String updateBy;

	/**
	 * createTime
	 */
	@Schema(description = "createTime")
	private LocalDateTime createTime;

	/**
	 * updateTime
	 */
	@Schema(description = "updateTime")
	private LocalDateTime updateTime;

	/**
	 * delFlag
	 */
	@Schema(description = "delFlag")
	private String delFlag;

	/**
	 * tenantId
	 */
	@Schema(description = "tenantId", hidden = true)
	private Long tenantId;

}
