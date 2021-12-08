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

package com.pig4cloud.pigx.codegen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 生成记录
 *
 * @author lengleng
 * @date 2019-08-12 15:55:35
 */
@Data
@TableName("gen_form_conf")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "生成记录")
public class GenFormConf extends Model<GenFormConf> {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "ID")
	private Long id;

	/**
	 * 表名称
	 */
	@ApiModelProperty(value = "表名称")
	private String tableName;

	/**
	 * 表单信息
	 */
	@ApiModelProperty(value = "表单信息")
	private String formInfo;

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
	 * 删除标记
	 */
	@ApiModelProperty(value = "删除标记")
	private String delFlag;

	/**
	 * 所属租户
	 */
	@ApiModelProperty(value = "所属租户", hidden = true)
	private Long tenantId;

}
