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
import com.ijpay.alipay.AliPayApi;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import com.pig4cloud.pigx.common.xss.core.XssCleanIgnore;
import com.pig4cloud.pigx.pay.entity.PayNotifyRecord;
import com.pig4cloud.pigx.pay.handler.PayNotifyCallbakHandler;
import com.pig4cloud.pigx.pay.service.PayNotifyRecordService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 异步通知记录
 *
 * @author lengleng
 * @date 2019-05-28 23:57:23
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/notify")
@Tag(description  = "notify", name =  "notify管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PayNotifyRecordController {

	private final PayNotifyRecordService payNotifyRecordService;

	private final PayNotifyCallbakHandler alipayCallback;

	private final PayNotifyCallbakHandler weChatCallback;

	private final PayNotifyCallbakHandler mergePayCallback;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param payNotifyRecord 异步通知记录
	 * @return
	 */
	@GetMapping("/page")
	public R getPayNotifyRecordPage(Page page, PayNotifyRecord payNotifyRecord) {
		return R.ok(payNotifyRecordService.page(page, Wrappers.query(payNotifyRecord)));
	}

	/**
	 * 通过id查询异步通知记录
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(payNotifyRecordService.getById(id));
	}

	/**
	 * 新增异步通知记录
	 * @param payNotifyRecord 异步通知记录
	 * @return R
	 */
	@SysLog("新增异步通知记录")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('pay_paynotifyrecord_add')")
	public R save(@RequestBody PayNotifyRecord payNotifyRecord) {
		return R.ok(payNotifyRecordService.save(payNotifyRecord));
	}

	/**
	 * 修改异步通知记录
	 * @param payNotifyRecord 异步通知记录
	 * @return R
	 */
	@SysLog("修改异步通知记录")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('pay_paynotifyrecord_edit')")
	public R updateById(@RequestBody PayNotifyRecord payNotifyRecord) {
		return R.ok(payNotifyRecordService.updateById(payNotifyRecord));
	}

	/**
	 * 通过id删除异步通知记录
	 * @param id id
	 * @return R
	 */
	@SysLog("删除异步通知记录")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('pay_paynotifyrecord_del')")
	public R removeById(@PathVariable Long id) {
		return R.ok(payNotifyRecordService.removeById(id));
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
	@ResponseBody
	@XssCleanIgnore
	@PostMapping("/wx/callbak")
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
	public void mergeCallbak(HttpServletRequest request, HttpServletResponse response) {
		// 解析回调信息
		Map<String, String> params = AliPayApi.toMap(request);
		response.getWriter().print(mergePayCallback.handle(params));
	}

}
