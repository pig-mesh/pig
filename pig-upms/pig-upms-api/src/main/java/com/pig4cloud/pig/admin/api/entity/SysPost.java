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
package com.pig4cloud.pig.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pig4cloud.pig.common.mybatis.base.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位管理
 *
 * @author fxz
 * @date 2022-03-15 17:18:40
 */
@Data
@TableName("sys_post")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "岗位信息表")
public class SysPost extends BaseEntity {

	private static final long serialVersionUID = -8744622014102311894L;

	/**
	 * 岗位ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "岗位ID")
	private Long postId;

	/**
	 * 岗位编码
	 */
	@Schema(description = "岗位编码")
	private String postCode;

	/**
	 * 岗位名称
	 */
	@Schema(description = "岗位名称")
	private String postName;

	/**
	 * 岗位排序
	 */
	@Schema(description = "岗位排序")
	private Integer postSort;

	/**
	 * 是否删除 -1：已删除 0：正常
	 */
	@Schema(description = "是否删除  -1：已删除  0：正常")
	private String delFlag;

	/**
	 * 备注信息
	 */
	@Schema(description = "备注信息")
	private String remark;

}
