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
 * 商品
 *
 * @author lengleng
 * @date 2019-05-28 23:58:27
 */
@Data
@TenantTable
@TableName("pay_goods_order")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品订单表")
public class PayGoodsOrder extends Model<PayGoodsOrder> {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品订单ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "商品订单ID")
	private Long goodsOrderId;

	/**
	 * 商品ID
	 */
	@Schema(description = "商品ID")
	private String goodsId;

	/**
	 * 商品名称
	 */
	@Schema(description = "商品名称")
	private String goodsName;

	/**
	 * 金额,单位分
	 */
	@Schema(description = "金额,单位分")
	private String amount;

	/**
	 * 用户ID
	 */
	@Schema(description = "用户ID")
	private String userId;

	/**
	 * 订单状态,订单生成(0),支付成功(1),处理完成(2),处理失败(-1)
	 */
	@Schema(description = "订单状态,订单生成(0),支付成功(1),处理完成(2),处理失败(-1)")
	private String status;

	/**
	 * 支付订单号
	 */
	@Schema(description = "支付订单号")
	private Long payOrderId;

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
