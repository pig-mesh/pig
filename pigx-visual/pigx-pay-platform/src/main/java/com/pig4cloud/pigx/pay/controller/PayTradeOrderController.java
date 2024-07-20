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
import com.pig4cloud.pigx.pay.entity.PayTradeOrder;
import com.pig4cloud.pigx.pay.service.PayTradeOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付订单表
 *
 * @author PIG
 * @date 2023-02-28 14:18:17
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/trade")
@Tag(description = "trade", name = "支付订单表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PayTradeOrderController {

	private final PayTradeOrderService payTradeOrderService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param payTradeOrder 支付订单表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("pay_trade_view")
	public R getpayTradeOrderPage(Page page, PayTradeOrder payTradeOrder) {
		LambdaQueryWrapper<PayTradeOrder> wrapper = Wrappers.lambdaQuery();
		wrapper.like(payTradeOrder.getOrderId() != null, PayTradeOrder::getOrderId, payTradeOrder.getOrderId());
		wrapper.eq(StrUtil.isNotBlank(payTradeOrder.getStatus()), PayTradeOrder::getStatus, payTradeOrder.getStatus());
		return R.ok(payTradeOrderService.page(page, wrapper));
	}

	/**
	 * 通过id查询支付订单表
	 * @param orderId id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{orderId}")
	@HasPermission("pay_trade_view")
	public R getById(@PathVariable("orderId") Long orderId) {
		return R.ok(payTradeOrderService.getById(orderId));
	}

	/**
	 * 新增支付订单表
	 * @param payTradeOrder 支付订单表
	 * @return R
	 */
	@Operation(summary = "新增支付订单表", description = "新增支付订单表")
	@SysLog("新增支付订单表")
	@PostMapping
	@HasPermission("pay_trade_add")
	public R save(@RequestBody PayTradeOrder payTradeOrder) {
		return R.ok(payTradeOrderService.save(payTradeOrder));
	}

	/**
	 * 修改支付订单表
	 * @param payTradeOrder 支付订单表
	 * @return R
	 */
	@Operation(summary = "修改支付订单表", description = "修改支付订单表")
	@SysLog("修改支付订单表")
	@PutMapping
	@HasPermission("pay_trade_edit")
	public R updateById(@RequestBody PayTradeOrder payTradeOrder) {
		return R.ok(payTradeOrderService.updateById(payTradeOrder));
	}

	/**
	 * 通过id删除支付订单表
	 * @param ids orderId列表
	 * @return R
	 */
	@Operation(summary = "通过id删除支付订单表", description = "通过id删除支付订单表")
	@SysLog("通过id删除支付订单表")
	@DeleteMapping
	@HasPermission("pay_trade_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(payTradeOrderService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 * @param payTradeOrder 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("pay_trade_export")
	public List<PayTradeOrder> export(PayTradeOrder payTradeOrder) {
		return payTradeOrderService.list(Wrappers.query(payTradeOrder));
	}

}
