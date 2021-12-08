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

package com.pig4cloud.pigx.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 文件管理
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
@Data
@ApiModel(value = "文件")
@EqualsAndHashCode(callSuper = true)
public class SysFile extends Model<SysFile> {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "文件编号")
	private Long id;

	/**
	 * 文件名
	 */
	@ApiModelProperty(value = "文件名")
	private String fileName;

	/**
	 * 原文件名
	 */
	@ApiModelProperty(value = "原始文件名")
	private String original;

	/**
	 * 容器名称
	 */
	@ApiModelProperty(value = "存储桶名称")
	private String bucketName;

	/**
	 * 文件类型
	 */
	@ApiModelProperty(value = "文件类型")
	private String type;

	/**
	 * 文件大小
	 */
	@ApiModelProperty(value = "文件大小")
	private Long fileSize;

	/**
	 * 上传人
	 */
	@ApiModelProperty(value = "创建者")
	private String createUser;

	/**
	 * 上传时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新者")
	private String updateUser;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 删除标识：1-删除，0-正常
	 */
	@TableLogic
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	private Integer delFlag;

}
