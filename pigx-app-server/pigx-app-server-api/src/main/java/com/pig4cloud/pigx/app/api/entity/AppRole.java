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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("app_role")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "app角色表")
public class AppRole extends Model<AppRole> {

	private static final long serialVersionUID = 1L;

	/**
	 * roleId
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "roleId")
	private Long roleId;

	/**
	 * roleName
	 */
	@ApiModelProperty(value = "roleName")
	private String roleName;

	/**
	 * roleCode
	 */
	@ApiModelProperty(value = "roleCode")
	private String roleCode;

	/**
	 * roleDesc
	 */
	@ApiModelProperty(value = "roleDesc")
	private String roleDesc;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createBy;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updateBy;

	/**
	 * createTime
	 */
	@ApiModelProperty(value = "createTime")
	private LocalDateTime createTime;

	/**
	 * updateTime
	 */
	@ApiModelProperty(value = "updateTime")
	private LocalDateTime updateTime;

	/**
	 * delFlag
	 */
	@ApiModelProperty(value = "delFlag")
	private String delFlag;

	/**
	 * tenantId
	 */
	@ApiModelProperty(value = "tenantId", hidden = true)
	private Long tenantId;

}
