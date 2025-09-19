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

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.entity.SysFile;
import com.pig4cloud.pig.admin.service.SysFileService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.annotation.HasPermission;
import com.pig4cloud.pig.common.security.annotation.Inner;
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

import java.io.IOException;

/**
 * 文件管理控制器
 *
 * @author lengleng
 * @date 2025/05/30
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys-file")
@Tag(description = "sys-file", name = "文件管理模块")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysFileController {

	private final SysFileService sysFileService;

	/**
	 * 分页查询文件信息
	 * @param page 分页参数对象
	 * @param sysFile 文件查询条件对象
	 * @return 分页查询结果
	 */
	@GetMapping("/page")
	@Operation(summary = "分页查询", description = "分页查询")
	public R getFilePage(@ParameterObject Page page, @ParameterObject SysFile sysFile) {
		LambdaQueryWrapper<SysFile> wrapper = Wrappers.<SysFile>lambdaQuery()
			.like(StrUtil.isNotBlank(sysFile.getOriginal()), SysFile::getOriginal, sysFile.getOriginal());
		return R.ok(sysFileService.page(page, wrapper));
	}

	/**
	 * 通过id删除文件管理
	 * @param ids 要删除的文件id数组
	 * @return 操作结果
	 */
	@SysLog("删除文件管理")
	@DeleteMapping
	@HasPermission("sys_file_del")
	@Operation(summary = "通过id删除文件管理", description = "通过id删除文件管理")
	public R removeById(@RequestBody Long[] ids) {
		for (Long id : ids) {
			sysFileService.removeFile(id);
		}
		return R.ok();
	}

	/**
	 * 上传文件
	 * @param file 上传的文件资源
	 * @return 包含文件路径的R对象，格式为(/admin/bucketName/filename)
	 */
	@PostMapping(value = "/upload")
	@Operation(summary = "上传文件", description = "上传文件")
	public R upload(@RequestPart("file") MultipartFile file) {
		return sysFileService.uploadFile(file);
	}

	/**
	 * 获取文件并写入响应流
	 * @param bucket 桶名称
	 * @param fileName 文件路径/名称
	 * @param response HTTP响应对象
	 */
	@Inner(false)
	@GetMapping("/{bucket}/{fileName}")
	@Operation(summary = "获取文件并写入响应流", description = "获取文件并写入响应流")
	public void file(@PathVariable String bucket, @PathVariable String fileName, HttpServletResponse response) {
		sysFileService.getFile(bucket, fileName, response);
	}

	/**
	 * 获取本地resources目录下的文件并写入响应流
	 * @param fileName 文件名称
	 * @param response HTTP响应对象，用于输出文件内容
	 * @throws IOException 文件操作异常
	 */
	@SneakyThrows
	@GetMapping("/local/file/{fileName}")
	@Operation(summary = "获取本地resources目录下的文件并写入响应流", description = "获取本地resources目录下的文件并写入响应流")
	public void localFile(@PathVariable String fileName, HttpServletResponse response) {
		ClassPathResource resource = new ClassPathResource("file/" + fileName);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IoUtil.copy(resource.getInputStream(), response.getOutputStream());
	}

}
