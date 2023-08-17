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

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.entity.GenGroupEntity;
import com.pig4cloud.pig.codegen.entity.GenTable;
import com.pig4cloud.pig.codegen.entity.GenTableColumnEntity;
import com.pig4cloud.pig.codegen.mapper.GenTableMapper;
import com.pig4cloud.pig.codegen.mapper.GeneratorMapper;
import com.pig4cloud.pig.codegen.service.GenGroupService;
import com.pig4cloud.pig.codegen.service.GenTableColumnService;
import com.pig4cloud.pig.codegen.service.GenTableService;
import com.pig4cloud.pig.codegen.util.CommonColumnFiledEnum;
import com.pig4cloud.pig.codegen.util.GenKit;
import com.pig4cloud.pig.codegen.util.GeneratorFileTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 列属性
 *
 * @author pigx code generator
 * @date 2023-02-06 20:34:55
 */
@Service
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements GenTableService {

	/**
	 * 默认配置信息
	 */
	private static final String CONFIG_PATH = "template/config.json";

	private final GenTableColumnService columnService;

	private final GenGroupService genGroupService;

	/**
	 * 获取配置信息
	 * @return
	 */
	@Override
	public Map<String, Object> getGeneratorConfig() {
		ClassPathResource classPathResource = new ClassPathResource(CONFIG_PATH);
		JSONObject jsonObject = JSONUtil.parseObj(IoUtil.readUtf8(classPathResource.getStream()));
		return jsonObject.getRaw();
	}

	@Override
	public List<Map<String, Object>> queryDsAllTable(String dsName) {
		GeneratorMapper mapper = GenKit.getMapper(dsName);
		// 手动切换数据源
		DynamicDataSourceContextHolder.push(dsName);
		return mapper.queryTable();
	}

	@Override
	public List<Map<String, String>> queryColumn(String dsName, String tableName) {
		GeneratorMapper mapper = GenKit.getMapper(dsName);
		return mapper.selectMapTableColumn(tableName, dsName);
	}

	@Override
	public IPage list(Page<GenTable> page, GenTable table) {
		GeneratorMapper mapper = GenKit.getMapper(table.getDsName());
		// 手动切换数据源
		DynamicDataSourceContextHolder.push(table.getDsName());
		return mapper.queryTable(page, table.getTableName());
	}

	/**
	 * 获取表信息
	 * @param dsName
	 * @param tableName
	 * @return
	 */
	@Override
	public GenTable queryOrBuildTable(String dsName, String tableName) {
		GenTable genTable = baseMapper.selectOne(
				Wrappers.<GenTable>lambdaQuery().eq(GenTable::getTableName, tableName).eq(GenTable::getDsName, dsName));
		// 如果 genTable 为空， 执行导入
		if (Objects.isNull(genTable)) {
			genTable = this.tableImport(dsName, tableName);
		}

		List<GenTableColumnEntity> fieldList = columnService.list(Wrappers.<GenTableColumnEntity>lambdaQuery()
			.eq(GenTableColumnEntity::getDsName, dsName)
			.eq(GenTableColumnEntity::getTableName, tableName)
			.orderByAsc(GenTableColumnEntity::getSort));
		genTable.setFieldList(fieldList);

		// 查询模板分组信息
		List<GenGroupEntity> groupEntities = genGroupService.list();
		genTable.setGroupList(groupEntities);
		return genTable;
	}

	@Transactional(rollbackFor = Exception.class)
	public GenTable tableImport(String dsName, String tableName) {
		GeneratorMapper mapper = GenKit.getMapper(dsName);
		// 手动切换数据源
		DynamicDataSourceContextHolder.push(dsName);

		// 查询表是否存在
		GenTable table = new GenTable();

		// 从数据库获取表信息
		Map<String, String> queryTable = mapper.queryTable(tableName, dsName);

		// 获取默认表配置信息 （）
		Map<String, Object> generatorConfig = getGeneratorConfig();
		JSONObject project = (JSONObject) generatorConfig.get("project");
		JSONObject developer = (JSONObject) generatorConfig.get("developer");

		table.setPackageName(project.getStr("packageName"));
		table.setVersion(project.getStr("version"));
		table.setBackendPath(project.getStr("backendPath"));
		table.setFrontendPath(project.getStr("frontendPath"));
		table.setAuthor(developer.getStr("author"));
		table.setEmail(developer.getStr("email"));
		table.setTableName(tableName);
		table.setDsName(dsName);
		table.setTableComment(MapUtil.getStr(queryTable, "tableComment"));
		table.setDbType(MapUtil.getStr(queryTable, "dbType"));
		table.setFormLayout(2);
		table.setGeneratorType(GeneratorFileTypeEnum.ZIP.ordinal());
		table.setClassName(NamingCase.toPascalCase(tableName));
		table.setModuleName(GenKit.getModuleName(table.getPackageName()));
		table.setFunctionName(GenKit.getFunctionName(tableName));
		table.setCreateTime(LocalDateTime.now());
		this.save(table);

		// 获取原生字段数据
		List<Map<String, String>> queryColumnList = mapper.selectMapTableColumn(tableName, dsName);
		List<GenTableColumnEntity> tableFieldList = new ArrayList<>();

		for (Map<String, String> columnMap : queryColumnList) {
			String columnName = MapUtil.getStr(columnMap, "columnName");
			GenTableColumnEntity genTableColumnEntity = new GenTableColumnEntity();
			genTableColumnEntity.setTableName(tableName);
			genTableColumnEntity.setDsName(dsName);
			genTableColumnEntity.setFieldName(MapUtil.getStr(columnMap, "columnName"));
			genTableColumnEntity.setFieldComment(MapUtil.getStr(columnMap, "comments"));
			genTableColumnEntity.setFieldType(MapUtil.getStr(columnMap, "dataType"));
			String columnKey = MapUtil.getStr(columnMap, "columnKey");
			genTableColumnEntity.setPrimaryPk(StringUtils.isNotBlank(columnKey) && "PRI".equalsIgnoreCase(columnKey));
			genTableColumnEntity.setAutoFill("DEFAULT");
			genTableColumnEntity.setFormItem(true);
			genTableColumnEntity.setGridItem(true);

			// 审计字段处理
			if (EnumUtil.contains(CommonColumnFiledEnum.class, columnName)) {
				CommonColumnFiledEnum commonColumnFiledEnum = CommonColumnFiledEnum.valueOf(columnName);
				genTableColumnEntity.setFormItem(commonColumnFiledEnum.getFormItem());
				genTableColumnEntity.setGridItem(commonColumnFiledEnum.getGridItem());
				genTableColumnEntity.setAutoFill(commonColumnFiledEnum.getAutoFill());
				genTableColumnEntity.setSort(commonColumnFiledEnum.getSort());
			}
			tableFieldList.add(genTableColumnEntity);
		}
		// 初始化字段数据
		columnService.initFieldList(tableFieldList);
		// 保存列数据
		columnService.saveOrUpdateBatch(tableFieldList);
		table.setFieldList(tableFieldList);
		return table;
	}

}
