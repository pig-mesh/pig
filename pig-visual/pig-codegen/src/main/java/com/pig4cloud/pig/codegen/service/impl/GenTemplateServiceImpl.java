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
package com.pig4cloud.pig.codegen.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.smallbun.screw.core.constant.DefaultConstants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.config.PigCodeGenDefaultProperties;
import com.pig4cloud.pig.codegen.entity.GenGroupEntity;
import com.pig4cloud.pig.codegen.entity.GenTemplateEntity;
import com.pig4cloud.pig.codegen.entity.GenTemplateGroupEntity;
import com.pig4cloud.pig.codegen.mapper.GenGroupMapper;
import com.pig4cloud.pig.codegen.mapper.GenTemplateGroupMapper;
import com.pig4cloud.pig.codegen.mapper.GenTemplateMapper;
import com.pig4cloud.pig.codegen.service.GenTemplateService;
import com.pig4cloud.pig.codegen.util.vo.GenTemplateFileVO;
import com.pig4cloud.pig.common.core.exception.CheckedException;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 模板
 *
 * @author PIG
 * @date 2023-02-21 11:08:43
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenTemplateServiceImpl extends ServiceImpl<GenTemplateMapper, GenTemplateEntity>
		implements GenTemplateService {

	private static final String CONFIG_JSON_FILE = "config.json";

	private static final String VERSION_FILE = "VERSION";

	private final PigCodeGenDefaultProperties defaultProperties;

	private final GenTemplateGroupMapper genTemplateGroupMapper;

	private final GenGroupMapper genGroupMapper;

	/**
	 * 在线更新
	 * @return {@link R }
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R onlineUpdate() {
		// 获取 config.json 和 version 文件
		ConfigAndVersion configAndVersion = getConfigAndVersion();
		JSONObject configJsonObj = configAndVersion.getConfigJsonObj();
		String versionFile = configAndVersion.getVersionFile();

		// 查询出全部的模板组名称
		Set<String> cgtmConfigGroupNames = configJsonObj.keySet();

		// 根据模板组名称+version 查询是否存在，不存在则新增，存在跳过
		for (String cgtmConfigGroupName : cgtmConfigGroupNames) {
			boolean exists = groupExists(withVersion(cgtmConfigGroupName, versionFile));

			if (exists) {
				continue;
			}

			// 插入新的模板组（名称 + VERSION）, 再解析 config.json group 里面的所有模板
			insertTemplateFiles(versionFile, configJsonObj, cgtmConfigGroupName);
		}
		return R.ok("更新成功，版本号:" + versionFile);
	}

	/**
	 * 检查版本
	 * @return {@link R }
	 */
	@Override
	public R checkVersion() {
		// 关闭在线更新提示
		if (!defaultProperties.isAutoCheckVersion()) {
			return R.ok(true);
		}

		// 获取 config.json 和 version 文件
		ConfigAndVersion configAndVersion = getConfigAndVersion();
		JSONObject configJsonObj = configAndVersion.getConfigJsonObj();
		String versionFile = configAndVersion.getVersionFile();

		// 查询出全部的模板组名称
		boolean exists = false;
		Set<String> cgtmConfigGroupNames = configJsonObj.keySet();
		for (String cgtmConfigGroupName : cgtmConfigGroupNames) {
			exists = groupExists(withVersion(cgtmConfigGroupName, versionFile));
		}

		return R.ok(exists);
	}

	/**
	 * 获取配置和版本
	 * @return {@link ConfigAndVersion }
	 */
	private ConfigAndVersion getConfigAndVersion() {
		// 获取 config.json 和 version 文件
		String configFile = getCGTMFile(CONFIG_JSON_FILE);
		String versionFile = getCGTMFile(VERSION_FILE);

		// 解析 config.json
		JSONObject configJsonObj = JSONUtil.parseObj(configFile);

		return new ConfigAndVersion(configJsonObj, versionFile);
	}

	/**
	 * 插入模板文件
	 * @param version 版本
	 * @param configJsonObj config.json
	 * @param groupName 组名称
	 */
	private void insertTemplateFiles(String version, JSONObject configJsonObj, String groupName) {
		// 创建新的 group
		GenGroupEntity genGroupEntity = createGroup(groupName, version);

		// 解析json配置文件
		List<GenTemplateFileVO> templateFileVOList = configJsonObj.getBeanList(groupName, GenTemplateFileVO.class);
		for (GenTemplateFileVO genTemplateFileVO : templateFileVOList) {
			GenTemplateEntity genTemplateEntity = getOrCreateTemplate(genTemplateFileVO, version);
			createTemplateGroupRelationIfAbsent(genTemplateEntity.getId(), genGroupEntity.getId());
		}
	}

	/**
	 * 获取 cgtmfile
	 * @param fileName 文件名
	 * @return {@link String }
	 */
	private String getCGTMFile(String fileName) {
		String requestUrl = String.format("%s/CGTM/raw/%s/%s", DefaultConstants.CGTM_URL, defaultProperties.getBranch(),
				fileName);
		HttpResponse response = HttpRequest.get(requestUrl).execute();
		String responseBody = response.body();
		int httpStatus = response.getStatus();

		if (httpStatus == HttpStatus.HTTP_OK || StrUtil.isNotBlank(responseBody)) {
			return responseBody;
		}

		log.warn("在线更新模板失败:{} ，Http Code:{}", fileName, httpStatus);
		throw new CheckedException("在线更新模板失败，任务终止！");
	}

	private boolean groupExists(String groupName) {
		return genGroupMapper
			.exists(Wrappers.<GenGroupEntity>lambdaQuery().eq(GenGroupEntity::getGroupName, groupName));
	}

	private GenGroupEntity createGroup(String groupName, String version) {
		GenGroupEntity genGroupEntity = new GenGroupEntity();
		genGroupEntity.setGroupName(withVersion(groupName, version));
		genGroupMapper.insert(genGroupEntity);
		return genGroupEntity;
	}

	private GenTemplateEntity getOrCreateTemplate(GenTemplateFileVO templateFileVO, String version) {
		String templateName = withVersion(templateFileVO.getTemplateName(), version);
		GenTemplateEntity genTemplateEntity = findTemplateByName(templateName);
		if (genTemplateEntity != null) {
			log.info("模板文件已存在，复用: {}", templateName);
			return genTemplateEntity;
		}

		GenTemplateEntity templateEntity = new GenTemplateEntity();
		templateEntity.setTemplateName(templateName);
		templateEntity.setTemplateDesc(templateName);
		templateEntity.setTemplateCode(getCGTMFile(templateFileVO.getTemplateFile()));
		templateEntity.setGeneratorPath(templateFileVO.getGeneratorPath());
		baseMapper.insert(templateEntity);
		log.info("模板文件已插入: {}", templateName);
		return templateEntity;
	}

	private GenTemplateEntity findTemplateByName(String templateName) {
		return baseMapper
			.selectOne(Wrappers.<GenTemplateEntity>lambdaQuery().eq(GenTemplateEntity::getTemplateName, templateName));
	}

	private void createTemplateGroupRelationIfAbsent(Long templateId, Long groupId) {
		boolean relationExists = genTemplateGroupMapper.exists(Wrappers.<GenTemplateGroupEntity>lambdaQuery()
			.eq(GenTemplateGroupEntity::getTemplateId, templateId)
			.eq(GenTemplateGroupEntity::getGroupId, groupId));
		if (relationExists) {
			return;
		}

		GenTemplateGroupEntity genTemplateGroupEntity = new GenTemplateGroupEntity();
		genTemplateGroupEntity.setTemplateId(templateId);
		genTemplateGroupEntity.setGroupId(groupId);
		genTemplateGroupMapper.insert(genTemplateGroupEntity);
	}

	private String withVersion(String name, String version) {
		return name + version;
	}

	@RequiredArgsConstructor
	private static final class ConfigAndVersion {

		private final JSONObject configJsonObj;

		private final String versionFile;

		private JSONObject getConfigJsonObj() {
			return configJsonObj;
		}

		private String getVersionFile() {
			return versionFile;
		}

	}

}
