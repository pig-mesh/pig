/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pig.admin.api.dto;

import com.pig4cloud.pig.admin.api.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author lengleng
 * @date 2017/11/5
 */
@Data
@Schema(description = "系统用户传输对象")
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends SysUser {

	/**
	 * 角色ID
	 */
	@Schema(description = "角色id集合")
	private List<Long> role;

	/**
	 * 部门id
	 */
	@Schema(description = "部门id")
	private Long deptId;

	/**
	 * 岗位ID
	 */
	private List<Long> post;

	/**
	 * 新密码
	 */
	@Schema(description = "新密码")
	private String newpassword1;

}
