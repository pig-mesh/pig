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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户角色表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@Data
@TableName("app_user_role")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户角色表")
public class AppUserRole extends Model<AppUserRole> {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "用户ID")
	private Long userId;

	/**
	 * 角色ID
	 */
	@Schema(description = "角色ID")
	private Long roleId;

}
