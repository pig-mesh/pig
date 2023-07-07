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

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 字典项
 *
 * @author lengleng
 * @date 2019/03/19
 */
@Data
@Schema(description = "字典项")
@EqualsAndHashCode(callSuper = true)
public class SysDictItem extends Model<SysDictItem> {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "字典项id")
	private Long id;

	/**
	 * 所属字典类id
	 */
	@Schema(description = "所属字典类id")
	private Long dictId;

	/**
	 * 数据值
	 */
	@Schema(description = "数据值")
	@JsonProperty(value = "value")
	private String itemValue;

	/**
	 * 标签名
	 */
	@Schema(description = "标签名")
	private String label;

	/**
	 * 类型
	 */
	@Schema(description = "类型")
	private String dictType;

	/**
	 * 描述
	 */
	@Schema(description = "描述")
	private String description;

	/**
	 * 排序（升序）
	 */
	@Schema(description = "排序值，默认升序")
	private Integer sortOrder;

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
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 备注信息
	 */
	@Schema(description = "备注信息")
	private String remarks;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

}
