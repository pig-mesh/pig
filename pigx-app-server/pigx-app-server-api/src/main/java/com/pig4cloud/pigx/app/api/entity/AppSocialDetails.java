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

package com.pig4cloud.pigx.app.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.sensitive.Sensitive;
import com.pig4cloud.pigx.common.core.util.ValidGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * app 社交账号登录
 */
@Data
@ApiModel(value = "第三方账号信息")
@EqualsAndHashCode(callSuper = true)
public class AppSocialDetails extends Model<AppSocialDetails> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主鍵
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "主键")
	private Long id;

	/**
	 * 类型
	 */
	@NotBlank(message = "类型不能为空")
	@ApiModelProperty(value = "账号类型")
	private String type;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String remark;

	/**
	 * appid
	 */
	@Sensitive(prefixNoMaskLen = 4, suffixNoMaskLen = 4)
	@NotBlank(message = "账号不能为空")
	@ApiModelProperty(value = "appId")
	private String appId;

	/**
	 * app_secret
	 */
	@Sensitive(prefixNoMaskLen = 9, suffixNoMaskLen = 9)
	@NotBlank(message = "密钥不能为空", groups = { ValidGroup.Insert.class })
	@ApiModelProperty(value = "app secret")
	private String appSecret;

	/**
	 * 回调地址
	 */
	@ApiModelProperty(value = "回调地址")
	private String redirectUrl;

	/**
	 * 拓展字段
	 */
	@ApiModelProperty(value = "拓展字段")
	private String ext;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建人")
	private String createBy;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "修改人")
	private String updateBy;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	private String delFlag;

}
