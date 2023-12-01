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

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * app用户表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@Data
@TenantTable
@TableName("app_user")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "app用户表")
public class AppUser extends Model<AppUser> {

	private static final long serialVersionUID = 1L;

	/**
	 * userId
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "userId")
	private Long userId;

	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String username;

	/**
	 * 密码
	 */
	@Schema(description = "密码")
	private String password;

	/**
	 * 随机盐
	 */
	@JsonIgnore
	@Schema(description = "随机盐")
	private String salt;

	/**
	 * 手机号
	 */
	@Schema(description = "手机号")
	private String phone;

	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar;

	/**
	 * 昵称
	 */
	@Schema(description = "拓展字段:昵称")
	private String nickname;

	/**
	 * 姓名
	 */
	@Schema(description = "拓展字段:姓名")
	private String name;

	/**
	 * 邮箱
	 */
	@Schema(description = "拓展字段:邮箱")
	private String email;

	/**
	 * 创建人
	 */
	@Schema(description = "创建人")
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 修改人
	 */
	@Schema(description = "修改人")
	@TableField(fill = FieldFill.UPDATE)
	private String updateBy;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@Schema(description = "修改时间")
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 删除标记
	 */
	@Schema(description = "删除标记,1:已删除,0:正常")
	@TableField(fill = FieldFill.INSERT)
	@TableLogic
	private String delFlag;

	/**
	 * 所属租户
	 */
	@Schema(description = "所属租户", hidden = true)
	private Long tenantId;

	/**
	 * 最后一次密码修改时间
	 */
	@Schema(description = "最后一次密码修改时间")
	private LocalDateTime lastModifiedTime;

	/**
	 * 锁定标记
	 */
	@Schema(description = "锁定标记")
	private String lockFlag;

	/**
	 * 微信小程序登录openId
	 */
	@Schema(description = "微信小程序登录openId")
	private String wxOpenid;

}
