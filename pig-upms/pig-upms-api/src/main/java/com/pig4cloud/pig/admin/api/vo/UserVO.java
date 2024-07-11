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

package com.pig4cloud.pig.admin.api.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pig4cloud.pig.admin.api.entity.SysPost;
import com.pig4cloud.pig.admin.api.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lengleng
 * @date 2017/10/29
 */
@Data
@Schema(description = "前端用户展示对象")
public class UserVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键")
	private Long userId;

	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String username;

	/**
	 * 密码
	 */
	@JsonIgnore
	private String password;

	/**
	 * 随机盐
	 */
	@JsonIgnore
	private String salt;

	/**
	 * 微信openid
	 */
	@Schema(description = "微信open id")
	private String wxOpenid;

	/**
	 * QQ openid
	 */
	@Schema(description = "qq open id")
	private String qqOpenid;

	/**
	 * gitee openid
	 */
	@Schema(description = "gitee open id")
	private String giteeOpenId;

	/**
	 * 开源中国 openid
	 */
	@Schema(description = "开源中国 open id")
	private String oscOpenId;

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
	 * 0-正常，1-删除
	 */
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

	/**
	 * 锁定标记
	 */
	@Schema(description = "锁定标记,0:正常,9:已锁定")
	private String lockFlag;

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
	 * 部门ID
	 */
	@Schema(description = "所属部门")
	private Long deptId;

	/**
	 * 部门名称
	 */
	@Schema(description = "所属部门名称")
	private String deptName;

	/**
	 * 角色列表
	 */
	@Schema(description = "拥有的角色列表")
	private List<SysRole> roleList;

	/**
	 * 岗位列表
	 */
	private List<SysPost> postList;

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
