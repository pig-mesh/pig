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
package com.pig4cloud.pigx.mp.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.mp.config.WxMpInitConfigRunner;
import com.pig4cloud.pigx.mp.entity.WxAccount;
import com.pig4cloud.pigx.mp.service.WxAccountService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 公众号账户
 *
 * @author lengleng
 * @date 2019-03-26 22:07:53
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wx-account")
public class WxAccountController {

	private final WxAccountService wxAccountService;

	private final RedisTemplate redisTemplate;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param wxAccount 公众号账户
	 * @return
	 */
	@GetMapping("/page")
	public R getWxAccountPage(Page page, WxAccount wxAccount) {
		LambdaQueryWrapper<WxAccount> wrapper = Wrappers.<WxAccount>lambdaQuery()
			.like(StrUtil.isNotBlank(wxAccount.getName()), WxAccount::getName, wxAccount.getName())
			.like(StrUtil.isNotBlank(wxAccount.getAccount()), WxAccount::getAccount, wxAccount.getAccount());
		return R.ok(wxAccountService.page(page, wrapper));
	}

	/**
	 * 通过id查询公众号账户
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(wxAccountService.getById(id));
	}

	/**
	 * 新增公众号账户
	 * @param wxAccount 公众号账户
	 * @return R
	 */
	@SysLog("新增公众号账户")
	@PostMapping
	@HasPermission("mp_wxaccount_add")
	public R save(@RequestBody WxAccount wxAccount) {
		wxAccountService.save(wxAccount);
		redisTemplate.convertAndSend(CacheConstants.MP_REDIS_RELOAD_TOPIC, "重新加载公众号配置");
		return R.ok();
	}

	/**
	 * 修改公众号账户
	 * @param wxAccount 公众号账户
	 * @return R
	 */
	@SysLog("修改公众号账户")
	@PutMapping
	@HasPermission("mp_wxaccount_edit")
	public R updateById(@RequestBody WxAccount wxAccount) {
		wxAccountService.updateById(wxAccount);
		redisTemplate.convertAndSend(CacheConstants.MP_REDIS_RELOAD_TOPIC, "重新加载公众号配置");
		return R.ok();
	}

	/**
	 * 通过id删除公众号账户
	 * @param id id
	 * @return R
	 */
	@SysLog("删除公众号账户")
	@DeleteMapping("/{id}")
	@HasPermission("mp_wxaccount_del")
	public R removeById(@PathVariable Long id) {
		wxAccountService.removeById(id);
		redisTemplate.convertAndSend(CacheConstants.MP_REDIS_RELOAD_TOPIC, "重新加载公众号配置");
		return R.ok();
	}

	/**
	 * 生成公众号二维码
	 * @param appId
	 * @return
	 */
	@SysLog("生成公众号二维码")
	@PostMapping("/qr/{appId}")
	@HasPermission("mp_wxaccount_add")
	public R qr(@PathVariable String appId) {
		return wxAccountService.generateQr(appId);
	}

	/**
	 * 获取公众号列表
	 * @return
	 */
	@GetMapping("/list")
	public R list(String name) {
		LambdaQueryWrapper<WxAccount> wrapper = Wrappers.<WxAccount>lambdaQuery()
			.like(StrUtil.isNotBlank(name), WxAccount::getName, name);
		return R.ok(wxAccountService.list(wrapper));
	}

	/**
	 * 获取公众号接口数据
	 * @param appId 公众号
	 * @param interval 时间间隔
	 * @return
	 */
	@GetMapping("/statistics")
	public R statistics(String appId, String interval) {
		return wxAccountService.statistics(appId, interval);
	}

	@SneakyThrows
	@PostMapping("/clear-quota/{appId}")
	@HasPermission("mp_wxaccount_del")
	public R clearQuota(@PathVariable String appId) {
		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
		wxMpService.clearQuota(appId);
		return R.ok();
	}

}
