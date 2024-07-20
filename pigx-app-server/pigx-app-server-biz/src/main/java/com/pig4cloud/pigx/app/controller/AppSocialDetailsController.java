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

package com.pig4cloud.pigx.app.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.app.api.entity.AppSocialDetails;
import com.pig4cloud.pigx.app.service.AppSocialDetailsService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.ValidGroup;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appsocial")
@AllArgsConstructor
@Tag(description = "social", name = "三方账号管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class AppSocialDetailsController {

	private final AppSocialDetailsService appSocialDetailsService;

	/**
	 * 社交登录账户简单分页查询
	 * @param page 分页对象
	 * @param appSocialDetails 社交登录
	 * @return
	 */
	@GetMapping("/page")
	public R getSocialDetailsPage(Page page, AppSocialDetails appSocialDetails) {
		return R.ok(appSocialDetailsService.page(page, Wrappers.query(appSocialDetails)));
	}

	/**
	 * 信息
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R getinfo(@PathVariable("id") Long id) {
		return R.ok(appSocialDetailsService.getById(id));
	}

	/**
	 * 保存
	 * @param appSocialDetails
	 * @return R
	 */
	@SysLog("保存三方信息")
	@PostMapping
	@HasPermission("app_social_details_add")
	public R save(@Valid @RequestBody AppSocialDetails appSocialDetails) {
		return R.ok(appSocialDetailsService.save(appSocialDetails));
	}

	/**
	 * 修改
	 * @param appSocialDetails
	 * @return R
	 */
	@SysLog("修改三方信息")
	@PutMapping
	@HasPermission("app_social_details_edit")
	public R updateById(@Validated({ ValidGroup.Update.class }) @RequestBody AppSocialDetails appSocialDetails) {
		appSocialDetailsService.updateById(appSocialDetails);
		return R.ok(Boolean.TRUE);
	}

	/**
	 * 删除
	 * @param ids
	 * @return R
	 */
	@SysLog("删除三方信息")
	@DeleteMapping
	@HasPermission("app_social_details_del")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(appSocialDetailsService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 通过社交账号、手机号查询用户、角色信息
	 * @param inStr appid@code
	 * @return
	 */
	@Inner
	@GetMapping("/info/{inStr}")
	public R getUserInfo(@PathVariable String inStr) {
		return R.ok(appSocialDetailsService.getUserInfo(inStr));
	}

	/**
	 * 绑定社交账号
	 * @param state 类型
	 * @param code code
	 * @return
	 */
	@PostMapping("/bind")
	public R bindSocial(String state, String code) {
		return R.ok(appSocialDetailsService.bindSocial(state, code));
	}

	/**
	 * 导出
	 */
	@GetMapping("/export")
	@ResponseExcel
	public List<AppSocialDetails> export(AppSocialDetails appSocialDetails) {
		return appSocialDetailsService.list(Wrappers.query(appSocialDetails));
	}

}
