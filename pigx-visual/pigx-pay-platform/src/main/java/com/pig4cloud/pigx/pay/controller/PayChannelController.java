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
import com.pig4cloud.pigx.pay.entity.PayChannel;
import com.pig4cloud.pigx.pay.service.PayChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 支付渠道表
 *
 * @author PIG
 * @date 2023-02-27 17:49:03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/channel")
@Tag(description = "channel", name = "支付渠道表管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PayChannelController {

	private final PayChannelService payChannelService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param payChannel 支付渠道表
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	@HasPermission("pay_channel_view")
	public R getpayChannelPage(Page page, PayChannel payChannel) {
		LambdaQueryWrapper<PayChannel> wrapper = Wrappers.lambdaQuery();
		wrapper.like(StrUtil.isNotBlank(payChannel.getChannelName()), PayChannel::getChannelName,
				payChannel.getChannelName());
		wrapper.eq(StrUtil.isNotBlank(payChannel.getState()), PayChannel::getState, payChannel.getState());
		return R.ok(payChannelService.page(page, wrapper));
	}

	/**
	 * 通过id查询支付渠道表
	 * @param id id
	 * @return R
	 */
	@Operation(summary = "通过id查询", description = "通过id查询")
	@GetMapping("/{id}")
	@HasPermission("pay_channel_view")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(payChannelService.getById(id));
	}

	/**
	 * 新增支付渠道表
	 * @param payChannel 支付渠道表
	 * @return R
	 */
	@Operation(summary = "新增支付渠道表", description = "新增支付渠道表")
	@SysLog("新增支付渠道表")
	@PostMapping
	@HasPermission("pay_channel_add")
	public R save(@RequestBody PayChannel payChannel) {
		return R.ok(payChannelService.save(payChannel));
	}

	/**
	 * 修改支付渠道表
	 * @param payChannel 支付渠道表
	 * @return R
	 */
	@Operation(summary = "修改支付渠道表", description = "修改支付渠道表")
	@SysLog("修改支付渠道表")
	@PutMapping
	@HasPermission("pay_channel_edit")
	public R updateById(@RequestBody PayChannel payChannel) {
		return R.ok(payChannelService.updateById(payChannel));
	}

	/**
	 * 通过id删除支付渠道表
	 * @param ids id列表
	 * @return R
	 */
	@Operation(summary = "通过id删除支付渠道表", description = "通过id删除支付渠道表")
	@SysLog("通过id删除支付渠道表")
	@DeleteMapping
	@HasPermission("pay_channel_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(payChannelService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出excel 表格
	 * @param payChannel 查询条件
	 * @return excel 文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("pay_channel_export")
	public List<PayChannel> export(PayChannel payChannel) {
		return payChannelService.list(Wrappers.query(payChannel));
	}

}
