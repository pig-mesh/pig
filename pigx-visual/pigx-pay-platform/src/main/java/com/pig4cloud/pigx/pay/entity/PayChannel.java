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
import com.pig4cloud.pigx.common.core.sensitive.Sensitive;
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
@TableName("pay_channel")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "渠道")
public class PayChannel extends Model<PayChannel> {

	private static final long serialVersionUID = 1L;

	/**
	 * 渠道主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 应用ID
	 */
	@Sensitive(prefixNoMaskLen = 4, suffixNoMaskLen = 4)
	private String appId;

	/**
	 * 渠道ID
	 */
	private String channelId;

	/**
	 * 渠道名称,如:alipay,wechat
	 */
	private String channelName;

	/**
	 * 渠道商户ID | 12****123
	 */
	@Sensitive(prefixNoMaskLen = 2, suffixNoMaskLen = 3)
	private String channelMchId;

	/**
	 * 前端回调地址
	 */
	private String returnUrl;

	/**
	 * 后端回调地址
	 */
	private String notifyUrl;

	/**
	 * 渠道状态
	 */
	private String state;

	/**
	 * 配置参数,json字符串
	 */
	private String param;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 是否删除 -1：已删除 0：正常
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 租户ID
	 */
	private Long tenantId;

}
