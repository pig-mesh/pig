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
 * 退款
 *
 * @author lengleng
 * @date 2019-05-28 23:58:11
 */
@Data
@TenantTable
@TableName("pay_refund_order")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "退款订单表")
public class PayRefundOrder extends Model<PayRefundOrder> {

	private static final long serialVersionUID = 1L;

	/**
	 * 退款订单号
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "退款订单号")
	private Long refundOrderId;

	/**
	 * 支付订单号
	 */
	@Schema(description = "支付订单号")
	private Long payOrderId;

	/**
	 * 渠道支付单号
	 */
	@Schema(description = "渠道支付单号")
	private String channelPayOrderNo;

	/**
	 * 商户ID
	 */
	@Schema(description = "商户ID")
	private String mchId;

	/**
	 * 商户退款单号
	 */
	@Schema(description = "商户退款单号")
	private String mchRefundNo;

	/**
	 * 渠道ID
	 */
	@Schema(description = "渠道ID")
	private String channelId;

	/**
	 * 支付金额
	 */
	@Schema(description = "支付金额")
	private String payAmount;

	/**
	 * 退款金额,单位分
	 */
	@Schema(description = "退款金额,单位分")
	private Long refundAmount;

	/**
	 * 三位货币代码
	 */
	@Schema(description = "三位货币代码")
	private String currency;

	/**
	 * 退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败,4-业务处理完成
	 */
	@Schema(description = "退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败,4-业务处理完成")
	private Integer status;

	/**
	 * 退款结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败
	 */
	@Schema(description = "退款结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败")
	private Integer result;

	/**
	 * 客户端IP
	 */
	@Schema(description = "客户端IP")
	private String clientIp;

	/**
	 * 设备
	 */
	@Schema(description = "设备")
	private String device;

	/**
	 * 备注
	 */
	@Schema(description = "备注")
	private String remark;

	/**
	 * 渠道用户标识
	 */
	@Schema(description = "渠道用户标识")
	private String channelUser;

	/**
	 * 用户姓名
	 */
	@Schema(description = "用户姓名")
	private String username;

	/**
	 * 渠道商户ID
	 */
	@Schema(description = "渠道商户ID")
	private String channelMchId;

	/**
	 * 渠道订单号
	 */
	@Schema(description = "渠道订单号")
	private String channelOrderNo;

	/**
	 * 渠道错误码
	 */
	@Schema(description = "渠道错误码")
	private String channelErrCode;

	/**
	 * 渠道错误描述
	 */
	@Schema(description = "渠道错误描述")
	private String channelErrMsg;

	/**
	 * 特定渠道发起时额外参数
	 */
	@Schema(description = "特定渠道发起时额外参数")
	private String extra;

	/**
	 * 通知地址
	 */
	@Schema(description = "通知地址")
	private String notifyUrl;

	/**
	 * 扩展参数1
	 */
	@Schema(description = "扩展参数1")
	private String param1;

	/**
	 * 扩展参数2
	 */
	@Schema(description = "扩展参数2")
	private String param2;

	/**
	 * 订单失效时间
	 */
	@Schema(description = "订单失效时间")
	private LocalDateTime expireTime;

	/**
	 * 订单退款成功时间
	 */
	@Schema(description = "订单退款成功时间")
	private LocalDateTime refundSuccTime;

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
