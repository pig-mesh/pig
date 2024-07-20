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

package com.pig4cloud.pigx.pay.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.pay.entity.PayRefundOrder;
import com.pig4cloud.pigx.pay.service.PayRefundOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 退款
 *
 * @author lengleng
 * @date 2019-05-28 23:58:11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/refund")
@Tag(description = "refund", name = "退款订单表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PayRefundOrderController {

	private final PayRefundOrderService payRefundOrderService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param payRefundOrder 退款订单表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("pay_refund_view")
	public R getpayRefundOrderPage(Page page, PayRefundOrder payRefundOrder) {
		LambdaQueryWrapper<PayRefundOrder> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(payRefundOrder.getRefundOrderId() != null, PayRefundOrder::getRefundOrderId,
				payRefundOrder.getRefundOrderId());
		wrapper.eq(payRefundOrder.getPayOrderId() != null, PayRefundOrder::getPayOrderId,
				payRefundOrder.getPayOrderId());
		wrapper.eq(StrUtil.isNotBlank(payRefundOrder.getMchId()), PayRefundOrder::getMchId, payRefundOrder.getMchId());
		return R.ok(payRefundOrderService.page(page, wrapper));
	}

	/**
	 * 通过id查询退款订单表
	 * @param refundOrderId id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{refundOrderId}")
	@HasPermission("pay_refund_view")
	public R getById(@PathVariable("refundOrderId") Long refundOrderId) {
		return R.ok(payRefundOrderService.getById(refundOrderId));
	}

	/**
	 * 新增退款订单表
	 * @param payRefundOrder 退款订单表
	 * @return R
	 */
	@Operation(summary = "新增退款订单表", description = "新增退款订单表")
	@SysLog("新增退款订单表")
	@PostMapping
	@HasPermission("pay_refund_add")
	public R save(@RequestBody PayRefundOrder payRefundOrder) {
		return R.ok(payRefundOrderService.refund(payRefundOrder));
	}

	/**
	 * 修改退款订单表
	 * @param payRefundOrder 退款订单表
	 * @return R
	 */
	@Operation(summary = "修改退款订单表", description = "修改退款订单表")
	@SysLog("修改退款订单表")
	@PutMapping
	@HasPermission("pay_refund_edit")
	public R updateById(@RequestBody PayRefundOrder payRefundOrder) {
		return R.ok(payRefundOrderService.updateById(payRefundOrder));
	}

	/**
	 * 通过id删除退款订单表
	 * @param ids refundOrderId列表
	 * @return R
	 */
	@Operation(summary = "通过id删除退款订单表", description = "通过id删除退款订单表")
	@SysLog("通过id删除退款订单表")
	@DeleteMapping
	@HasPermission("pay_refund_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(payRefundOrderService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 * @param payRefundOrder 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("pay_refund_export")
	public List<PayRefundOrder> export(PayRefundOrder payRefundOrder) {
		return payRefundOrderService.list(Wrappers.query(payRefundOrder));
	}

}
