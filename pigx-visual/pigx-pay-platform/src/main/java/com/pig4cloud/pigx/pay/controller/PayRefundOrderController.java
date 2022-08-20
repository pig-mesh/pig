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

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.pay.entity.PayRefundOrder;
import com.pig4cloud.pigx.pay.service.PayRefundOrderService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 退款
 *
 * @author lengleng
 * @date 2019-05-28 23:58:11
 */
@RestController
@AllArgsConstructor
@RequestMapping("/payrefundorder")
@Api(value = "payrefundorder", tags = "payrefundorder管理")
public class PayRefundOrderController {

	private final PayRefundOrderService payRefundOrderService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param payRefundOrder 退款
	 * @return
	 */
	@GetMapping("/page")
	public R getPayRefundOrderPage(Page page, PayRefundOrder payRefundOrder) {
		return R.ok(payRefundOrderService.page(page, Wrappers.query(payRefundOrder)));
	}

	/**
	 * 通过id查询退款
	 * @param orderId id
	 * @return R
	 */
	@GetMapping("/{orderId}")
	public R getById(@PathVariable String orderId) {
		return R.ok(payRefundOrderService.getById(orderId));
	}

	/**
	 * 新增退款
	 * @param payRefundOrder 退款
	 * @return R
	 */
	@SysLog("新增退款")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('pay_payrefundorder_add')")
	public R save(@RequestBody PayRefundOrder payRefundOrder) {
		return R.ok(payRefundOrderService.save(payRefundOrder));
	}

	/**
	 * 修改退款
	 * @param payRefundOrder 退款
	 * @return R
	 */
	@SysLog("修改退款")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('pay_payrefundorder_edit')")
	public R updateById(@RequestBody PayRefundOrder payRefundOrder) {
		return R.ok(payRefundOrderService.updateById(payRefundOrder));
	}

	/**
	 * 通过id删除退款
	 * @param refundOrderId refundOrderId
	 * @return R
	 */
	@SysLog("删除退款")
	@DeleteMapping("/{refundOrderId}")
	@PreAuthorize("@pms.hasPermission('pay_payrefundorder_del')")
	public R removeById(@PathVariable String refundOrderId) {
		return R.ok(payRefundOrderService.removeById(refundOrderId));
	}

}
