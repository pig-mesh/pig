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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 公共参数配置
 *
 * @author Lucky
 * @date 2019-04-29
 */
@Data
@Schema(description = "公共参数")
@EqualsAndHashCode(callSuper = true)
public class SysPublicParam extends Model<SysPublicParam> {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "公共参数编号")
	private Long publicId;

	/**
	 * 公共参数名称
	 */
	@Schema(description = "公共参数名称", required = true, example = "公共参数名称")
	private String publicName;

	/**
	 * 公共参数地址值,英文大写+下划线
	 */
	@Schema(description = "键[英文大写+下划线]", required = true, example = "PIGX_PUBLIC_KEY")
	private String publicKey;

	/**
	 * 值
	 */
	@Schema(description = "值", required = true, example = "999")
	private String publicValue;

	/**
	 * 状态（1有效；2无效；）
	 */
	@Schema(description = "标识[1有效；2无效]", example = "1")
	private String status;

	/**
	 * 公共参数编码
	 */
	@Schema(description = "编码", example = "^(PIG|PIGX)$")
	private String validateCode;

	/**
	 * 是否是系统内置
	 */
	@Schema(description = "是否是系统内置")
	private String systemFlag;

	/**
	 * 配置类型：0-默认；1-检索；2-原文；3-报表；4-安全；5-文档；6-消息；9-其他
	 */
	@Schema(description = "类型[1-检索；2-原文...]", example = "1")
	private String publicType;

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
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

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

}
