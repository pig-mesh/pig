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

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.sensitive.Sensitive;
import com.pig4cloud.pigx.common.core.util.ValidGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 系统社交登录账号表
 *
 * @author lengleng
 * @date 2018-08-16 21:30:41
 */
@Data
@Schema(description = "第三方账号信息")
@EqualsAndHashCode(callSuper = true)
public class SysSocialDetails extends Model<SysSocialDetails> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主鍵
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	/**
	 * 类型
	 */
	@NotBlank(message = "类型不能为空")
	@Schema(description = "账号类型")
	private String type;

	/**
	 * 描述
	 */
	@Schema(description = "描述")
	private String remark;

	/**
	 * appid
	 */
	@Sensitive(prefixNoMaskLen = 4, suffixNoMaskLen = 4)
	@NotBlank(message = "账号不能为空")
	@Schema(description = "appId")
	private String appId;

	/**
	 * app_secret
	 */
	@Sensitive(prefixNoMaskLen = 9, suffixNoMaskLen = 9)
	@NotBlank(message = "密钥不能为空", groups = { ValidGroup.Insert.class })
	@Schema(description = "app secret")
	private String appSecret;

	/**
	 * 回调地址
	 */
	@Schema(description = "回调地址")
	private String redirectUrl;

	/**
	 * 拓展字段
	 */
	@Schema(description = "拓展字段")
	private String ext;

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
	@Schema(description = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

}
