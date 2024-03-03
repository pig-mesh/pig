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

package com.pig4cloud.pigx.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author lengleng
 * @date 2020/2/10
 */
@Data
@Schema(description = "前端角色展示对象")
public class RoleVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	@Schema(description = "角色ID")
	private Long roleId;

	/**
	 * 角色名称
	 */
	@Schema(description = "角色名称")
	private String roleName;

	/**
	 * 角色标识
	 */
	@Schema(description = "角色标识")
	private String roleCode;

	/**
	 * 角色描述
	 */
	@Schema(description = "角色描述")
	private String roleDesc;

	/**
	 * 数据权限类型
	 */
	@Schema(description = "数据权限类型")
	private Integer dsType;

	/**
	 * 数据权限作用范围
	 */
	@Schema(description = "数据权限作用范围")
	private String dsScope;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 租户ID
	 */
	@Schema(description = "用户所属租户id")
	private Long tenantId;

}
