/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.admin.api.dto.SysFileGroupDTO;
import com.pig4cloud.pig.admin.api.entity.SysFile;
import com.pig4cloud.pig.admin.api.entity.SysFileGroup;
import com.pig4cloud.pig.admin.mapper.SysFileGroupMapper;
import com.pig4cloud.pig.admin.mapper.SysFileMapper;
import com.pig4cloud.pig.admin.service.SysFileService;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.util.R;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.util.*;

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

	private final FileStorageService fileStorageService;

	private final SysFileGroupMapper fileGroupMapper;

	/**
	 * 上传文件
	 * @param file 文件流
	 * @param fileName
	 * @param dir 文件夹
	 * @param groupId 分组ID
	 * @param type 类型
	 * @return
	 */
	@Override
	public R uploadFile(MultipartFile file, String fileName, String dir, Long groupId, String type) {
		if (StrUtil.isBlank(fileName)) {
			fileName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
		}
		Map<String, String> resultMap = new HashMap<>(4);
		resultMap.put("fileName", fileName);
		resultMap.put("url", String.format("/admin/sys-file/oss/file?fileName=%s", fileName));

		try {
			FileInfo fileInfo = fileStorageService.of(file)
				.setPath(StrUtil.isNotBlank(dir) ? dir + StrUtil.SLASH : "")
				.setSaveFilename(fileName)
				.upload();
			if (fileInfo == null) {
				return R.failed("上传失败");
			}
			resultMap.put("bucketName", fileInfo.getPlatform());
			fileLog(file, dir, fileName, groupId, type, fileInfo.getPlatform());
		}
		catch (Exception e) {
			log.error("上传失败", e);
			return R.failed(e.getLocalizedMessage());
		}
		return R.ok(resultMap);
	}

	/**
	 * 获取文件内容并响应给客户端。
	 * @param fileName 要获取的文件名称
	 * @param response 响应对象，用于将文件内容发送给客户端
	 */
	@Override
	public void getFile(String fileName, HttpServletResponse response) {
		Map<String, String> stringMap = HttpUtil.decodeParamMap(fileName, Charset.defaultCharset());
		String urlFileName = MapUtil.getStr(stringMap, SysFile.Fields.fileName);
		if (StrUtil.isNotBlank(urlFileName)) {
			fileName = urlFileName;
		}

		SysFile sysFile = baseMapper.selectOne(Wrappers.<SysFile>lambdaQuery().eq(SysFile::getFileName, fileName),
				false);
		if (Objects.isNull(sysFile)) {
			log.warn("文件不存在: {}", fileName);
			return;
		}

		try {
			FileInfo fileInfo = new FileInfo();
			fileInfo.setPlatform(sysFile.getBucketName());
			fileInfo.setPath(StrUtil.isNotBlank(sysFile.getDir()) ? sysFile.getDir() + StrUtil.SLASH : "");
			fileInfo.setFilename(fileName);
			response.setContentType("application/octet-stream; charset=UTF-8");
			response.addHeader(SysFile.Fields.hash, sysFile.getHash());
			response.addHeader(HttpHeaders.CONTENT_DISPOSITION,
					"attachment; filename=" + URLUtil.encode(sysFile.getOriginal()));
			fileStorageService.download(fileInfo).inputStream(is -> {
				try {
					IoUtil.copy(is, response.getOutputStream());
				}
				catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			});
		}
		catch (Exception e) {
			log.error("文件读取异常: {}", e.getLocalizedMessage());
		}
	}

	/**
	 * 根据ID删除文件
	 * @param id 文件ID
	 * @return 删除是否成功，文件不存在时返回false
	 */
	@Override
	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public Boolean deleteFile(Long id) {
		SysFile file = this.getById(id);
		if (Objects.isNull(file)) {
			return Boolean.FALSE;
		}
		FileInfo fileInfo = new FileInfo();
		fileInfo.setPlatform(file.getBucketName());
		fileInfo.setPath(StrUtil.isNotBlank(file.getDir()) ? file.getDir() + StrUtil.SLASH : "");
		fileInfo.setFilename(file.getFileName());
		fileStorageService.delete(fileInfo);
		return this.removeById(id);
	}

	/**
	 * 查询文件组列表
	 * @param fileGroup SysFileGroup对象，用于筛选条件
	 * @return 包含文件组树形结构列表的List对象
	 */
	@Override
	public List<Tree<Long>> listFileGroup(SysFileGroup fileGroup) {
		// 从数据库查询文件组列表
		List<TreeNode<Long>> treeNodeList = fileGroupMapper.selectList(Wrappers.query(fileGroup))
			.stream()
			.map(group -> {
				TreeNode<Long> treeNode = new TreeNode<>();
				treeNode.setName(group.getName());
				treeNode.setId(group.getId());
				treeNode.setParentId(group.getPid());
				return treeNode;
			})
			.toList();

		// 构建树形结构
		List<Tree<Long>> treeList = TreeUtil.build(treeNodeList, CommonConstants.MENU_TREE_ROOT_ID);
		return CollUtil.isEmpty(treeList) ? new ArrayList<>() : treeList;
	}

	/**
	 * 添加或更新文件组
	 * @param fileGroup SysFileGroup对象，要添加或更新的文件组信息
	 * @return 添加或更新成功返回true，否则返回false
	 */
	@Override
	public Boolean saveOrUpdateGroup(SysFileGroup fileGroup) {
		if (Objects.isNull(fileGroup.getId())) {
			// 插入文件组
			fileGroupMapper.insert(fileGroup);
		}
		else {
			// 更新文件组
			fileGroupMapper.updateById(fileGroup);
		}
		return Boolean.TRUE;
	}

	/**
	 * 删除文件组
	 * @param id 待删除文件组的ID
	 * @return 删除成功返回true，否则返回false
	 */
	@Override
	public Boolean deleteGroup(Long id) {
		// 根据ID删除文件组
		fileGroupMapper.deleteById(id);
		return Boolean.TRUE;
	}

	/**
	 * 移动文件组
	 * @param fileGroupDTO SysFileGroupDTO对象，要移动的文件组信息
	 * @return 移动成功返回true，否则返回false
	 */
	@Override
	public Boolean moveFileGroup(SysFileGroupDTO fileGroupDTO) {
		// 创建SysFile对象并设置groupId属性
		SysFile file = new SysFile();
		file.setGroupId(fileGroupDTO.getGroupId());

		// 根据IDS更新对应的SysFile记录
		baseMapper.update(file,
				Wrappers.<SysFile>lambdaQuery().in(SysFile::getId, CollUtil.toList(fileGroupDTO.getIds())));
		return Boolean.TRUE;
	}

	/**
	 * 文件管理数据记录，收集管理追踪文件
	 * @param file 上传的文件格式
	 * @param dir 文件夹
	 * @param fileName 文件名
	 * @param groupId 文件组ID
	 * @param type 文件类型
	 */
	@SneakyThrows
	private void fileLog(MultipartFile file, String dir, String fileName, Long groupId, String type, String platform) {
		// 创建SysFile对象并设置相关属性
		SysFile sysFile = new SysFile();
		sysFile.setFileName(fileName);
		sysFile.setDir(dir);
		sysFile.setOriginal(file.getOriginalFilename());
		sysFile.setFileSize(file.getSize());
		sysFile.setBucketName(platform);
		sysFile.setType(type);
		sysFile.setGroupId(groupId);

		sysFile.setHash(SecureUtil.md5(file.getInputStream()));
		// 调用save方法保存SysFile对象
		this.save(sysFile);
	}

}
