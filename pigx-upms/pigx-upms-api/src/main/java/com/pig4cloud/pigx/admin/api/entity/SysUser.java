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

package com.pig4cloud.pigx.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "用户")
public class SysUser implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value = "user_id", type = IdType.AUTO)
	@ApiModelProperty(value = "主键id")
	private Long userId;

	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;

	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;

	/**
	 * 随机盐
	 */
	@JsonIgnore
	@ApiModelProperty(value = "随机盐")
	private String salt;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 0-正常，1-删除
	 */
	@TableLogic
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	private String delFlag;

	/**
	 * 锁定标记
	 */
	@ApiModelProperty(value = "锁定标记")
	private String lockFlag;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	private String phone;

	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像地址")
	private String avatar;

	/**
	 * 部门ID
	 */
	@ApiModelProperty(value = "用户所属部门id")
	private Long deptId;

	/**
	 * 租户ID
	 */
	@ApiModelProperty(value = "用户所属租户id")
	private Long tenantId;

	/**
	 * 微信openid
	 */
	@ApiModelProperty(value = "微信openid")
	private String wxOpenid;

	/**
	 * 微信小程序openId
	 */
	@ApiModelProperty(value = "微信小程序openid")
	private String miniOpenid;

	/**
	 * QQ openid
	 */
	@ApiModelProperty(value = "QQ openid")
	private String qqOpenid;

	/**
	 * 码云唯一标识
	 */
	@ApiModelProperty(value = "码云唯一标识")
	private String giteeLogin;

	/**
	 * 开源中国唯一标识
	 */
	@ApiModelProperty(value = "开源中国唯一标识")
	private String oscId;

	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "昵称")
	private String nickname;

	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "姓名")
	private String name;

	/**
	 * 邮箱
	 */
	@ApiModelProperty(value = "邮箱")
	private String email;

}
