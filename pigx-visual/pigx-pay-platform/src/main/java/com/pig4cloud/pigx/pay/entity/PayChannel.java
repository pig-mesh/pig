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

package com.pig4cloud.pigx.pay.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import com.pig4cloud.pigx.common.sensitive.annotation.Sensitive;
import com.pig4cloud.pigx.common.sensitive.core.SensitiveTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 渠道
 *
 * @author lengleng
 * @date 2019-05-28 23:57:58
 */
@Data
@TenantTable
@TableName("pay_channel")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "渠道")
public class PayChannel extends Model<PayChannel> {

	private static final long serialVersionUID = 1L;

	/**
	 * 渠道主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "渠道主键ID")
	private Long id;

	/**
	 * mchId
	 */
	@Schema(description = "mchId")
	private String mchId;

	/**
	 * 渠道ID
	 */
	@Schema(description = "渠道ID")
	private String channelId;

	/**
	 * 渠道名称,如:alipay,wechat
	 */
	@Schema(description = "渠道名称,如:alipay,wechat")
	private String channelName;

	/**
	 * 渠道商户ID | 123333***
	 */
	@Sensitive(type = SensitiveTypeEnum.KEY)
	@Schema(description = "渠道商户ID")
	private String channelMchId;

	/**
	 * 前端回调地址
	 */
	@Schema(description = "前端回调地址")
	private String returnUrl;

	/**
	 * 后端回调地址
	 */
	@Schema(description = "后端回调地址")
	private String notifyUrl;

	/**
	 * 渠道状态
	 */
	@Schema(description = "渠道状态")
	private String state;

	/**
	 * 配置参数,json字符串
	 */
	@Schema(description = "配置参数,json字符串")
	private String param;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;

	/**
	 * delFlag
	 */
	@TableLogic
	@Schema(description = "delFlag")
	private String delFlag;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 租户ID
	 */
	@Schema(description = "租户ID")
	private Long tenantId;

	/**
	 * 应用ID
	 */
	@Sensitive(type = SensitiveTypeEnum.KEY)
	@Schema(description = "应用ID")
	private String appId;

}
