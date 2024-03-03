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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 异步通知记录
 *
 * @author lengleng
 * @date 2019-05-28 23:57:23
 */
@Data
@TenantTable
@TableName("pay_notify_record")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "异步通知记录")
public class PayNotifyRecord extends Model<PayNotifyRecord> {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "ID")
	private Long id;

	/**
	 * 响应ID
	 */
	@Schema(description = "响应ID")
	private String notifyId;

	/**
	 * 请求报文
	 */
	@Schema(description = "请求报文")
	private String request;

	/**
	 * 响应报文
	 */
	@Schema(description = "响应报文")
	private String response;

	/**
	 * 系统订单号
	 */
	@Schema(description = "系统订单号")
	private String orderNo;

	/**
	 * http状态
	 */
	@Schema(description = "http状态")
	private String httpStatus;

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
	@Schema(description = "更新时间")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 租户ID
	 */
	@Schema(description = "租户ID")
	private Long tenantId;

}
