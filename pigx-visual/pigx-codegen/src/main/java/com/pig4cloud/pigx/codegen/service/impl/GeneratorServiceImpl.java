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

package com.pig4cloud.pigx.codegen.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.codegen.entity.GenConfig;
import com.pig4cloud.pigx.codegen.entity.GenFormConf;
import com.pig4cloud.pigx.codegen.mapper.GenFormConfMapper;
import com.pig4cloud.pigx.codegen.mapper.GeneratorMapper;
import com.pig4cloud.pigx.codegen.service.GenCodeService;
import com.pig4cloud.pigx.codegen.service.GeneratorService;
import com.pig4cloud.pigx.common.core.constant.enums.StyleTypeEnum;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

	private final GenFormConfMapper genFormConfMapper;

	private final GenCodeService element;

	private final Map<String, GenCodeService> genCodeServiceMap;

	/**
	 * 分页查询表
	 * @param tableName 查询条件
	 * @param dsName
	 * @return
	 */
	@Override
	public IPage<Map<String, Object>> getPage(Page page, String tableName, String dsName) {
		GeneratorMapper mapper = element.getMapper(dsName);
		// 手动切换数据源
		DynamicDataSourceContextHolder.push(dsName);

		// 关闭SQL 优化
		page.setOptimizeCountSql(false);
		return mapper.queryTable(page, tableName);
	}

	@Override
	public Map<String, String> previewCode(GenConfig genConfig) {
		// 根据tableName 查询最新的表单配置
		List<GenFormConf> formConfList = genFormConfMapper.selectList(Wrappers.<GenFormConf>lambdaQuery()
				.eq(GenFormConf::getTableName, genConfig.getTableName()).orderByDesc(GenFormConf::getCreateTime));

		String tableNames = genConfig.getTableName();
		String dsName = genConfig.getDsName();

		GenCodeService genCodeService = genCodeServiceMap.get(StyleTypeEnum.getDecs(genConfig.getStyle()));
		GeneratorMapper mapper = genCodeService.getMapper(genConfig.getDsName());

		for (String tableName : StrUtil.split(tableNames, StrUtil.DASHED)) {
			// 查询表信息
			Map<String, String> table = mapper.queryTable(tableName, dsName);
			// 查询列信息
			List<Map<String, String>> columns = mapper.selectMapTableColumn(tableName, dsName);
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
		GeneratorMapper mapper = genCodeService.getMapper(dsName);

		for (String tableName : StrUtil.split(tableNames, StrUtil.DASHED)) {
			// 查询表信息
			Map<String, String> table = mapper.queryTable(tableName, dsName);
			// 查询列信息
			List<Map<String, String>> columns = mapper.selectMapTableColumn(tableName, dsName);
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
