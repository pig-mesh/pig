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
package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.entity.SysFile;
import com.pig4cloud.pig.admin.mapper.SysFileMapper;
import com.pig4cloud.pig.admin.service.SysFileService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.file.core.FileProperties;
import com.pig4cloud.pig.common.file.core.FileTemplate;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 文件管理
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

	private final FileTemplate fileTemplate;

	private final FileProperties properties;

	/**
	 * 上传文件
	 * @param file 要上传的文件
	 * @return 包含文件信息的响应结果，失败时返回错误信息
	 * @throws Exception 文件上传过程中可能出现的异常
	 */
	@Override
	public R uploadFile(MultipartFile file) {
		String fileName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
		Map<String, String> resultMap = new HashMap<>(4);
		resultMap.put(SysFile.Fields.bucketName, properties.getBucketName());
		resultMap.put(SysFile.Fields.fileName, fileName);
		resultMap.put("url", String.format("/admin/sys-file/%s/%s", properties.getBucketName(), fileName));

		try (InputStream inputStream = file.getInputStream()) {
			fileTemplate.putObject(properties.getBucketName(), fileName, inputStream, file.getContentType());
			// 文件管理数据记录,收集管理追踪文件
			fileLog(file, fileName);
		}
		catch (Exception e) {
			log.error("上传失败", e);
			return R.failed(e.getLocalizedMessage());
		}
		return R.ok(resultMap);
	}

	/**
	 * 从指定存储桶中获取文件并写入HTTP响应流
	 * @param bucket 存储桶名称
	 * @param fileName 文件名
	 * @param response HTTP响应对象
	 */
	@Override
	public void getFile(String bucket, String fileName, HttpServletResponse response) {
		try (InputStream inputStream = (InputStream) fileTemplate.getObject(bucket, fileName)) {
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLUtil.encode(fileName));
			IoUtil.copy(inputStream, response.getOutputStream());
		}
		catch (Exception e) {
			log.error("文件读取异常: {}", e.getLocalizedMessage());
		}
	}

	/**
	 * 根据ID删除文件
	 * @param id 文件ID
	 * @return 删除是否成功，文件不存在时返回false
	 * @throws Exception 删除过程中可能抛出的异常
	 */
	@Override
	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeFile(Long id) {
		SysFile file = this.getById(id);
		if (Objects.isNull(file)) {
			return Boolean.FALSE;
		}
		fileTemplate.removeObject(properties.getBucketName(), file.getFileName());
		return this.removeById(id);
	}

	/**
	 * 记录文件管理数据
	 * @param file 上传文件
	 * @param fileName 文件名
	 */
	private void fileLog(MultipartFile file, String fileName) {
		SysFile sysFile = new SysFile();
		sysFile.setFileName(fileName);
		sysFile.setOriginal(file.getOriginalFilename());
		sysFile.setFileSize(file.getSize());
		sysFile.setType(FileUtil.extName(file.getOriginalFilename()));
		sysFile.setBucketName(properties.getBucketName());
		this.save(sysFile);
	}

}
