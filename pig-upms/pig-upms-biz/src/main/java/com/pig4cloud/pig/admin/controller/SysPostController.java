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

package com.pig4cloud.pig.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysPost;
import com.pig4cloud.pig.admin.api.vo.PostExcelVO;
import com.pig4cloud.pig.admin.service.SysPostService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.plugin.excel.annotation.RequestExcel;
import com.pig4cloud.plugin.excel.annotation.ResponseExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息表管理控制器
 *
 * @author lengleng
 * @date 2025/05/30
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(description = "post", name = "岗位信息表管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysPostController {

	private final SysPostService sysPostService;

	/**
	 * 获取岗位列表
	 * @return 包含岗位列表的响应结果
	 */
	@GetMapping("/list")
	@Operation(summary = "获取岗位列表", description = "获取岗位列表")
	public R<List<SysPost>> listPosts() {
		return R.ok(sysPostService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 分页查询岗位信息
	 * @param page 分页参数对象
	 * @param sysPost 岗位查询条件对象
	 * @return 分页查询结果
	 */
	@GetMapping("/page")
	@HasPermission("sys_post_view")
	@Operation(description = "分页查询岗位信息", summary = "分页查询岗位信息")
	public R getPostPage(@ParameterObject Page page, @ParameterObject SysPost sysPost) {
		return R.ok(sysPostService.page(page, Wrappers.<SysPost>lambdaQuery()
			.like(StrUtil.isNotBlank(sysPost.getPostName()), SysPost::getPostName, sysPost.getPostName())));
	}

	/**
	 * 通过id查询岗位信息
	 * @param postId 岗位id
	 * @return 包含岗位信息的响应结果
	 */
	@HasPermission("sys_post_view")
	@GetMapping("/details/{postId}")
	@Operation(description = "通过id查询岗位信息", summary = "通过id查询岗位信息")
	public R getById(@PathVariable("postId") Long postId) {
		return R.ok(sysPostService.getById(postId));
	}

	/**
	 * 查询岗位详细信息
	 * @param query 查询条件
	 * @return 统一响应结果R，包含查询到的岗位信息
	 */
	@GetMapping("/details")
	@HasPermission("sys_post_view")
	@Operation(description = "查询角色信息", summary = "查询角色信息")
	public R getDetails(SysPost query) {
		return R.ok(sysPostService.getOne(Wrappers.query(query), false));
	}

	/**
	 * 新增岗位信息
	 * @param sysPost 岗位信息对象
	 * @return 操作结果
	 */
	@PostMapping
	@SysLog("新增岗位信息表")
	@HasPermission("sys_post_add")
	@Operation(description = "新增岗位信息表", summary = "新增岗位信息表")
	public R savePost(@RequestBody SysPost sysPost) {
		return R.ok(sysPostService.save(sysPost));
	}

	/**
	 * 修改岗位信息
	 * @param sysPost 岗位信息对象
	 * @return 操作结果
	 */
	@PutMapping
	@SysLog("修改岗位信息表")
	@HasPermission("sys_post_edit")
	@Operation(description = "修改岗位信息表", summary = "修改岗位信息表")
	public R updatePost(@RequestBody SysPost sysPost) {
		return R.ok(sysPostService.updateById(sysPost));
	}

	/**
	 * 通过id批量删除岗位信息
	 * @param ids 岗位id数组
	 * @return 统一返回结果
	 */
	@DeleteMapping
	@SysLog("通过id删除岗位信息表")
	@HasPermission("sys_post_del")
	@Operation(description = "通过id删除岗位信息表", summary = "通过id删除岗位信息表")
	public R removeById(@RequestBody Long[] ids) {
		return R.ok(sysPostService.removeBatchByIds(CollUtil.toList(ids)));
	}

	/**
	 * 导出岗位信息到Excel表格
	 * @return 岗位信息Excel文件流
	 */
	@ResponseExcel
	@GetMapping("/export")
	@HasPermission("sys_post_export")
	@Operation(description = "导出岗位信息到Excel表格", summary = "导出岗位信息到Excel表格")
	public List<PostExcelVO> exportPosts() {
		return sysPostService.listPosts();
	}

	/**
	 * 导入岗位信息
	 * @param excelVOList 岗位Excel数据列表
	 * @param bindingResult 数据校验结果
	 * @return 导入结果
	 */
	@PostMapping("/import")
	@HasPermission("sys_post_export")
	@Operation(description = "导入岗位信息", summary = "导入岗位信息")
	public R importRole(@RequestExcel List<PostExcelVO> excelVOList, BindingResult bindingResult) {
		return sysPostService.importPost(excelVOList, bindingResult);
	}

}
