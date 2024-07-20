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
import com.ijpay.alipay.AliPayApi;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import com.pig4cloud.pigx.common.xss.core.XssCleanIgnore;
import com.pig4cloud.pigx.pay.entity.PayNotifyRecord;
import com.pig4cloud.pigx.pay.handler.PayNotifyCallbakHandler;
import com.pig4cloud.pigx.pay.service.PayNotifyRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 异步通知记录
 *
 * @author lengleng
 * @date 2019-05-28 23:57:23
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notify")
@Tag(description = "notify", name = "通知记录日志表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PayNotifyRecordController {

	private final PayNotifyRecordService payNotifyRecordService;

	private final PayNotifyCallbakHandler alipayCallback;

	private final PayNotifyCallbakHandler weChatCallback;

	private final PayNotifyCallbakHandler mergePayCallback;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param payNotifyRecord 通知记录日志表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("pay_record_view")
	public R getpayNotifyRecordPage(Page page, PayNotifyRecord payNotifyRecord) {
		LambdaQueryWrapper<PayNotifyRecord> wrapper = Wrappers.lambdaQuery();
		wrapper.eq(StrUtil.isNotBlank(payNotifyRecord.getNotifyId()), PayNotifyRecord::getNotifyId,
				payNotifyRecord.getNotifyId());
		wrapper.eq(StrUtil.isNotBlank(payNotifyRecord.getOrderNo()), PayNotifyRecord::getOrderNo,
				payNotifyRecord.getOrderNo());
		return R.ok(payNotifyRecordService.page(page, wrapper));
	}

	/**
	 * 通过id查询通知记录日志表
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	@HasPermission("pay_record_view")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(payNotifyRecordService.getById(id));
	}

	/**
	 * 新增通知记录日志表
	 * @param payNotifyRecord 通知记录日志表
	 * @return R
	 */
	@Operation(summary = "新增通知记录日志表", description = "新增通知记录日志表")
	@SysLog("新增通知记录日志表")
	@PostMapping
	public R save(@RequestBody PayNotifyRecord payNotifyRecord) {
		return R.ok(payNotifyRecordService.save(payNotifyRecord));
	}

	/**
	 * 修改通知记录日志表
	 * @param payNotifyRecord 通知记录日志表
	 * @return R
	 */
	@Operation(summary = "修改通知记录日志表", description = "修改通知记录日志表")
	@SysLog("修改通知记录日志表")
	@PutMapping
	@HasPermission("pay_record_edit")
	public R updateById(@RequestBody PayNotifyRecord payNotifyRecord) {
		return R.ok(payNotifyRecordService.updateById(payNotifyRecord));
	}

	/**
	 * 通过id删除通知记录日志表
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除通知记录日志表", description = "通过id删除通知记录日志表")
	@SysLog("通过id删除通知记录日志表")
	@DeleteMapping
	@HasPermission("pay_record_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(payNotifyRecordService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 支付宝渠道异步回调
	 * @param request 渠道请求
	 * @return
	 */
	@Inner(false)
	@SneakyThrows
	@XssCleanIgnore
	@PostMapping("/ali/callbak")
	@Operation(summary = "支付宝渠道异步回调", description = "支付宝渠道异步回调")
	public void aliCallbak(HttpServletRequest request, HttpServletResponse response) {
		// 解析回调信息
		Map<String, String> params = AliPayApi.toMap(request);
		response.getWriter().print(alipayCallback.handle(params));
	}

	/**
	 * 微信渠道支付回调
	 * @param request
	 * @return
	 */
	@Inner(false)
	@SneakyThrows
	@XssCleanIgnore
	@PostMapping("/wx/callbak")
	@Operation(summary = "微信渠道支付回调", description = "微信渠道支付回调")
	public String wxCallbak(HttpServletRequest request) {
		String xmlMsg = HttpKit.readData(request);
		log.info("微信订单回调信息:{}", xmlMsg);
		Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
		return weChatCallback.handle(params);
	}

	/**
	 * 聚合渠道异步回调
	 * @param request 渠道请求
	 * @return
	 */
	@Inner(false)
	@SneakyThrows
	@XssCleanIgnore
	@PostMapping("/merge/callbak")
	@Operation(summary = "聚合渠道异步回调", description = "聚合渠道异步回调")
	public void mergeCallbak(HttpServletRequest request, HttpServletResponse response) {
		// 解析回调信息
		Map<String, String> params = AliPayApi.toMap(request);
		response.getWriter().print(mergePayCallback.handle(params));
	}

	/**
	 * 导出excel 表格
	 * @param payNotifyRecord 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("pay_record_export")
	public List<PayNotifyRecord> export(PayNotifyRecord payNotifyRecord) {
		return payNotifyRecordService.list(Wrappers.query(payNotifyRecord));
	}

}
