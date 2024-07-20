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

package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.dto.SysFileGroupDTO;
import com.pig4cloud.pigx.admin.api.entity.SysFile;
import com.pig4cloud.pigx.admin.api.entity.SysFileGroup;
import com.pig4cloud.pigx.admin.service.SysFileService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * 文件管理
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-file")
@Tag(description = "sys-file", name = "文件管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysFileController {

	private final SysFileService sysFileService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysFile 文件管理
	 * @return
	 */
	@Operation(summary = "分页查询", description = "分页查询")
	@GetMapping("/page")
	public R getSysFilePage(@ParameterObject Page page, @ParameterObject SysFile sysFile) {
		LambdaQueryWrapper<SysFile> wrapper = Wrappers.<SysFile>lambdaQuery()
			.eq(StrUtil.isNotBlank(sysFile.getType()), SysFile::getType, sysFile.getType())
			.eq(Objects.nonNull(sysFile.getGroupId()), SysFile::getGroupId, sysFile.getGroupId())
			.like(StrUtil.isNotBlank(sysFile.getOriginal()), SysFile::getOriginal, sysFile.getOriginal());
		return R.ok(sysFileService.page(page, wrapper));
	}

	/**
	 * 通过id删除文件管理
	 * @param ids id 列表
	 * @return R
	 */
	@Operation(summary = "通过id删除文件管理", description = "通过id删除文件管理")
	@SysLog("删除文件管理")
	@DeleteMapping
	@HasPermission("sys_file_del")
	public R removeById(@RequestBody Long[] ids) {
		for (Long id : ids) {
			sysFileService.deleteFile(id);
		}
		return R.ok();
	}

	@PutMapping("/rename")
	public R rename(@RequestBody SysFile sysFile) {
		return R.ok(sysFileService.updateById(sysFile));
	}

	/**
	 * 上传文件 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
	 * @param file 资源
	 * @param dir 文件夹
	 * @return R(/ admin / bucketName / filename)
	 */
	@PostMapping(value = "/upload")
	public R upload(@RequestPart("file") MultipartFile file, @RequestParam(value = "dir", required = false) String dir,
			@RequestParam(value = "groupId", required = false) Long groupId,
			@RequestParam(value = "type", required = false) String type) {
		return sysFileService.uploadFile(file, dir, groupId, type);
	}

	/**
	 * 获取文件
	 * @param fileName 文件空间/名称
	 * @param response
	 * @return
	 */
	@Inner(false)
	@GetMapping("/oss/file")
	public void file(String fileName, HttpServletResponse response) {
		sysFileService.getFile(fileName, response);
	}

	/**
	 * 获取本地（resources）文件
	 * @param fileName 文件名称
	 * @param response 本地文件
	 */
	@SneakyThrows
	@GetMapping("/local/file/{fileName}")
	public void localFile(@PathVariable String fileName, HttpServletResponse response) {
		ClassPathResource resource = new ClassPathResource("file/" + fileName);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IoUtil.copy(resource.getInputStream(), response.getOutputStream());
	}

	/**
	 * 查询文件组列表
	 * @param fileGroup SysFileGroup对象，用于筛选条件
	 * @return 包含文件组列表的R对象
	 */
	@GetMapping("/group/list")
	public R listGroup(SysFileGroup fileGroup) {
		return R.ok(sysFileService.listFileGroup(fileGroup));
	}

	/**
	 * 添加文件组
	 * @param fileGroup SysFileGroup对象，要添加的文件组信息
	 * @return 包含添加结果的R对象
	 */
	@PostMapping("/group/add")
	public R addGroup(@RequestBody SysFileGroup fileGroup) {
		return R.ok(sysFileService.saveOrUpdateGroup(fileGroup));
	}

	/**
	 * 更新文件组
	 * @param fileGroup SysFileGroup对象，要更新的文件组信息
	 * @return 包含更新结果的R对象
	 */
	@PutMapping("/group/update")
	public R updateGroup(@RequestBody SysFileGroup fileGroup) {
		return R.ok(sysFileService.saveOrUpdateGroup(fileGroup));
	}

	/**
	 * 删除文件组
	 * @param id 待删除文件组的ID
	 * @return 包含删除结果的R对象
	 */
	@DeleteMapping("/group/delete/{id}")
	public R updateGroup(@PathVariable Long id) {
		return R.ok(sysFileService.deleteGroup(id));
	}

	/**
	 * 移动文件组
	 * @param fileGroupDTO SysFileGroupDTO对象，要移动的文件组信息
	 * @return 包含移动结果的R对象
	 */
	@PutMapping("/group/move")
	public R moveFileGroup(@RequestBody SysFileGroupDTO fileGroupDTO) {
		return R.ok(sysFileService.moveFileGroup(fileGroupDTO));
	}

}
