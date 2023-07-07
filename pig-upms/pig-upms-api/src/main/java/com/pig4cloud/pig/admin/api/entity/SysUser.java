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

package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
@Data
@Schema(description = "用户")
public class SysUser implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value = "user_id", type = IdType.ASSIGN_ID)
	@Schema(description = "主键id")
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
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建人")
	private String createBy;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "修改人")
	private String updateBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 0-正常，1-删除
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

	/**
	 * 锁定标记
	 */
	@Schema(description = "锁定标记")
	private String lockFlag;

	/**
	 * 手机号
	 */
	@Schema(description = "手机号")
	private String phone;

	/**
	 * 头像
	 */
	@Schema(description = "头像地址")
	private String avatar;

	/**
	 * 部门ID
	 */
	@Schema(description = "用户所属部门id")
	private Long deptId;

	/**
	 * 微信openid
	 */
	@Schema(description = "微信openid")
	private String wxOpenid;

	/**
	 * 微信小程序openId
	 */
	@Schema(description = "微信小程序openid")
	private String miniOpenid;

	/**
	 * QQ openid
	 */
	@Schema(description = "QQ openid")
	private String qqOpenid;

	/**
	 * 码云唯一标识
	 */
	@Schema(description = "码云唯一标识")
	private String giteeLogin;

	/**
	 * 开源中国唯一标识
	 */
	@Schema(description = "开源中国唯一标识")
	private String oscId;

	/**
	 * 昵称
	 */
	@Schema(description = "昵称")
	private String nickname;

	/**
	 * 姓名
	 */
	@Schema(description = "姓名")
	private String name;

	/**
	 * 邮箱
	 */
	@Schema(description = "邮箱")
	private String email;

}
