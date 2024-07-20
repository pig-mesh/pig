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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.mp.entity.WxAccountFans;
import com.pig4cloud.pigx.mp.service.WxAccountFansService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 微信公众号粉丝管理
 *
 * @author lengleng
 * @date 2019-03-26 22:08:08
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wx-account-fans")
public class WxAccountFansController {

	private final WxAccountFansService wxAccountFansService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param wxAccountFans 微信公众号粉丝
	 * @return
	 */
	@GetMapping("/page")
	public R getWxAccountFansPage(Page page, WxAccountFans wxAccountFans) {
		return R.ok(wxAccountFansService.getFansWithTagPage(page, wxAccountFans));
	}

	/**
	 * 通过id查询微信公众号粉丝
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(wxAccountFansService.getById(id));
	}

	/**
	 * 修改微信公众号粉丝
	 * @param wxAccountFans 微信公众号粉丝
	 * @return R
	 */
	@SysLog("修改微信公众号粉丝")
	@PutMapping
	@HasPermission("mp_wxaccountfans_edit")
	public R updateById(@RequestBody WxAccountFans wxAccountFans) {
		return R.ok(wxAccountFansService.updateFans(wxAccountFans));
	}

	/**
	 * 通过id删除微信公众号粉丝
	 * @param id id
	 * @return R
	 */
	@SysLog("删除微信公众号粉丝")
	@DeleteMapping("/{id}")
	@HasPermission("mp_wxaccountfans_del")
	public R removeById(@PathVariable Long id) {
		return R.ok(wxAccountFansService.removeById(id));
	}

	/**
	 * 同步粉丝
	 * @param appId
	 * @return
	 */
	@PostMapping("/sync/{appId}")
	@HasPermission("mp_wxaccountfans_sync")
	public R sync(@PathVariable String appId) {
		wxAccountFansService.syncAccountFans(appId);
		return R.ok();
	}

	/**
	 * 取消拉黑
	 * @param ids
	 * @param appId
	 * @return
	 */
	@PostMapping("/unblack/{appId}")
	public R unblack(@RequestBody Long[] ids, @PathVariable String appId) {
		return R.ok(wxAccountFansService.unblack(ids, appId));
	}

	@PostMapping("/black/{appId}")
	public R black(@RequestBody Long[] ids, @PathVariable String appId) {
		return R.ok(wxAccountFansService.black(ids, appId));
	}

}
