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

package com.pig4cloud.pig.codegen.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 模板分组关联表
 *
 * @author PIG
 * @date 2023-02-22 09:25:15
 */
@Data
@TableName("gen_template_group")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Schema(description = "模板分组关联表")
public class GenTemplateGroupEntity extends Model<GenTemplateGroupEntity> {

	private static final long serialVersionUID = 1L;

	/**
	 * 分组id
	 */
	@Schema(description = "分组id")
	private Long groupId;

	/**
	 * 模板id
	 */
	@Schema(description = "模板id")
	private Long templateId;

}
