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

package com.github.pig.admin.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
@Data
@TableName("sys_user_role")
public class SysUserRole extends Model<SysUserRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(type = IdType.INPUT)
	private Integer userId;
    /**
     * 角色ID
     */
	@TableId(type = IdType.INPUT)
	private Integer roleId;


	@Override
	protected Serializable pkVal() {
		return this.userId;
	}

	@Override
	public String toString() {
		return "SysUserRole{" +
			", userId=" + userId +
			", roleId=" + roleId +
			"}";
	}
}
