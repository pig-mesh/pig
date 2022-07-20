/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.codegen.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.codegen.entity.GenConfig;
import com.pig4cloud.pig.codegen.entity.GenFormConf;
import com.pig4cloud.pig.codegen.mapper.GenFormConfMapper;
import com.pig4cloud.pig.codegen.mapper.GeneratorMapper;
import com.pig4cloud.pig.codegen.service.GenCodeService;
import com.pig4cloud.pig.codegen.service.GeneratorService;
import com.pig4cloud.pig.codegen.support.StyleTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author lengleng
 * @date 2018-07-30
 * <p>
 * 代码生成器
 */
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

	private final GeneratorMapper generatorMapper;

	private final GenFormConfMapper genFormConfMapper;

	private final Map<String, GenCodeService> genCodeServiceMap;

	/**
	 * 分页查询表
	 * @param tableName 查询条件
	 * @param dsName
	 * @return
	 */
	@Override
	@DS("#last")
	public IPage<List<Map<String, Object>>> getPage(Page page, String tableName, String dsName) {
		page.setOptimizeCountSql(false);
		return generatorMapper.queryList(page, tableName);
	}

	/**
	 * 预览代码
	 * @param genConfig 查询条件
	 * @return
	 */
	@Override
	public Map<String, String> previewCode(GenConfig genConfig) {
		// 根据tableName 查询最新的表单配置
		List<GenFormConf> formConfList = genFormConfMapper.selectList(Wrappers.<GenFormConf>lambdaQuery()
				.eq(GenFormConf::getTableName, genConfig.getTableName()).orderByDesc(GenFormConf::getCreateTime));

		String tableNames = genConfig.getTableName();
		String dsName = genConfig.getDsName();

		// 获取实现
		GenCodeService genCodeService = genCodeServiceMap.get(StyleTypeEnum.getDecs(genConfig.getStyle()));

		for (String tableName : StrUtil.split(tableNames, StrUtil.DASHED)) {
			// 查询表信息
			Map<String, String> table = generatorMapper.queryTable(tableName, dsName);
			// 查询列信息
			List<Map<String, String>> columns = generatorMapper.queryColumns(tableName, dsName);
			// 生成代码
			if (CollUtil.isNotEmpty(formConfList)) {
				return genCodeService.gen(genConfig, table, columns, null, formConfList.get(0));
			}
			else {
				return genCodeService.gen(genConfig, table, columns, null, null);
			}
		}

		return MapUtil.empty();
	}

	/**
	 * 生成代码
	 * @param genConfig 生成配置
	 * @return
	 */
	@Override
	public byte[] generatorCode(GenConfig genConfig) {
		// 根据tableName 查询最新的表单配置
		List<GenFormConf> formConfList = genFormConfMapper.selectList(Wrappers.<GenFormConf>lambdaQuery()
				.eq(GenFormConf::getTableName, genConfig.getTableName()).orderByDesc(GenFormConf::getCreateTime));

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		String tableNames = genConfig.getTableName();
		String dsName = genConfig.getDsName();

		GenCodeService genCodeService = genCodeServiceMap.get(StyleTypeEnum.getDecs(genConfig.getStyle()));

		for (String tableName : StrUtil.split(tableNames, StrUtil.DASHED)) {
			// 查询表信息
			Map<String, String> table = generatorMapper.queryTable(tableName, dsName);
			// 查询列信息
			List<Map<String, String>> columns = generatorMapper.queryColumns(tableName, dsName);
			// 生成代码
			if (CollUtil.isNotEmpty(formConfList)) {
				genCodeService.gen(genConfig, table, columns, zip, formConfList.get(0));
			}
			else {
				genCodeService.gen(genConfig, table, columns, zip, null);
			}
		}
		IoUtil.close(zip);
		return outputStream.toByteArray();
	}

}
