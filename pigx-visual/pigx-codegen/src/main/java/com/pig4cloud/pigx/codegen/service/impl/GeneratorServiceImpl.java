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
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.pig4cloud.pigx.codegen.entity.GenFormConf;
import com.pig4cloud.pigx.codegen.entity.GenTable;
import com.pig4cloud.pigx.codegen.entity.GenTableColumnEntity;
import com.pig4cloud.pigx.codegen.entity.GenTemplateEntity;
import com.pig4cloud.pigx.codegen.service.*;
import com.pig4cloud.pigx.codegen.util.GeneratorStyleEnum;
import com.pig4cloud.pigx.codegen.util.VelocityKit;
import com.pig4cloud.pigx.codegen.util.vo.GroupVo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author lengleng
 * @date 2018-07-30
 * <p>
 * 代码生成器
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

	private final GenTableColumnService columnService;

	private final GenFormConfService formConfService;

	private final GenFieldTypeService fieldTypeService;

	private final GenTableService tableService;

	private final GenGroupService genGroupService;

	private final GenTemplateService genTemplateService;

	/**
	 * 生成代码zip写出
	 * @param tableId 表
	 * @param zip 输出流
	 */
	@Override
	@SneakyThrows
	public void downloadCode(Long tableId, ZipOutputStream zip) {
		// 数据模型
		Map<String, Object> dataModel = getDataModel(tableId);

		Long style = (Long) dataModel.get("style");

		GroupVo groupVo = genGroupService.getGroupVoById(style);
		List<GenTemplateEntity> templateList = groupVo.getTemplateList();

		for (GenTemplateEntity template : templateList) {
			String templateCode = template.getTemplateCode();
			String generatorPath = template.getGeneratorPath();

			String content = VelocityKit.renderStr(templateCode, dataModel);
			String path = VelocityKit.renderStr(generatorPath, dataModel);

			// 添加到zip
			zip.putNextEntry(new ZipEntry(path));
			IoUtil.writeUtf8(zip, false, content);
			zip.flush();
			zip.closeEntry();
		}

	}

	/**
	 * 表达式优化的预览代码方法
	 * @param tableId 表
	 * @return [{模板名称:渲染结果}]
	 */
	@Override
	@SneakyThrows
	public List<Map<String, String>> preview(Long tableId) {
		// 数据模型
		Map<String, Object> dataModel = getDataModel(tableId);

		Long style = (Long) dataModel.get("style");

		// 获取模板列表，Lambda 表达式简化代码
		List<GenTemplateEntity> templateList = genGroupService.getGroupVoById(style).getTemplateList();

		return templateList.stream().map(template -> {
			String templateCode = template.getTemplateCode();
			String generatorPath = template.getGeneratorPath();

			String content = VelocityKit.renderStr(templateCode, dataModel);
			String path = VelocityKit.renderStr(generatorPath, dataModel);

			// 使用 map 简化代码
			return new HashMap<String, String>(4) {
				{
					put("code", content);
					put("codePath", path);
				}
			};
		}).collect(Collectors.toList());
	}

	/**
	 * 目标目录写入渲染结果方法
	 * @param tableId 表
	 */
	@Override
	public void generatorCode(Long tableId) {
		// 数据模型
		Map<String, Object> dataModel = getDataModel(tableId);
		Long style = (Long) dataModel.get("style");

		// 获取模板列表，Lambda 表达式简化代码
		List<GenTemplateEntity> templateList = genGroupService.getGroupVoById(style).getTemplateList();

		templateList.forEach(template -> {
			String templateCode = template.getTemplateCode();
			String generatorPath = template.getGeneratorPath();
			String content = VelocityKit.renderStr(templateCode, dataModel);
			String path = VelocityKit.renderStr(generatorPath, dataModel);
			FileUtil.writeUtf8String(content, path);
		});
	}

	/**
	 * 获取表单设计器需要的 JSON 方法
	 * @param dsName 数据源名称
	 * @param tableName 表名称
	 * @return JSON 字符串
	 */
	@SneakyThrows
	@Override
	public String vform(String dsName, String tableName) {
		// 查询表的元数据
		GenTable genTable = tableService.queryOrBuildTable(dsName, tableName);

		// 获取数据模型
		Map<String, Object> dataModel = getDataModel(genTable.getId());

		// 获取模板信息，Lambda 表达式简化代码
		GenTemplateEntity genTemplateEntity = Optional
				.ofNullable(genTemplateService.getById(GeneratorStyleEnum.VFORM_JSON.getTemplateId()))
				.orElseThrow(() -> new Exception("模板不存在"));

		// 渲染模板并返回结果
		return VelocityKit.renderStr(genTemplateEntity.getTemplateCode(), dataModel);
	}

	/**
	 * 获取表单设计器需要的 JSON 方法
	 * @param id 表单配置 ID
	 * @return JSON 字符串
	 */
	@SneakyThrows
	@Override
	public String vformSfc(Long id) {
		// 获取表单配置信息
		GenFormConf formConf = formConfService.getById(id);

		// 查询表的元数据
		GenTable genTable = tableService.queryOrBuildTable(formConf.getDsName(), formConf.getTableName());

		// 获取数据模型
		Map<String, Object> dataModel = getDataModel(genTable.getId());

		// 解析组件列表
		List<JSONObject> widgetList = formConfService.parse(formConf.getFormInfo());
		dataModel.put("widgetList", widgetList);

		// 获取模板信息
		GenTemplateEntity genTemplateEntity = Optional
				.ofNullable(genTemplateService.getById(GeneratorStyleEnum.VFORM_FORM.getTemplateId()))
				.orElseThrow(() -> new Exception("模板不存在"));

		// 渲染模板并返回结果
		return VelocityKit.renderStr(genTemplateEntity.getTemplateCode(), dataModel);
	}

	/**
	 * 通过 Lambda 表达式优化的获取数据模型方法
	 * @param tableId 表格 ID
	 * @return 数据模型 Map 对象
	 */
	private Map<String, Object> getDataModel(Long tableId) {
		// 获取表格信息
		GenTable table = tableService.getById(tableId);
		// 获取字段列表
		List<GenTableColumnEntity> fieldList = columnService.lambdaQuery()
				.eq(GenTableColumnEntity::getDsName, table.getDsName())
				.eq(GenTableColumnEntity::getTableName, table.getTableName()).list();
		table.setFieldList(fieldList);

		// 创建数据模型对象
		Map<String, Object> dataModel = new HashMap<>();

		// 填充数据模型
		dataModel.put("dbType", table.getDbType());
		dataModel.put("package", table.getPackageName());
		dataModel.put("packagePath", table.getPackageName().replace(".", File.separator));
		dataModel.put("version", table.getVersion());
		dataModel.put("moduleName", table.getModuleName());
		dataModel.put("ModuleName", StrUtil.upperFirst(table.getModuleName()));
		dataModel.put("functionName", table.getFunctionName());
		dataModel.put("FunctionName", StrUtil.upperFirst(table.getFunctionName()));
		dataModel.put("formLayout", table.getFormLayout());
		dataModel.put("style", table.getStyle());
		dataModel.put("author", table.getAuthor());
		dataModel.put("datetime", DateUtil.now());
		dataModel.put("date", DateUtil.today());
		setFieldTypeList(dataModel, table);

		// 获取导入的包列表
		Set<String> importList = fieldTypeService.getPackageByTableId(table.getDsName(), table.getTableName());
		dataModel.put("importList", importList);
		dataModel.put("tableName", table.getTableName());
		dataModel.put("tableComment", table.getTableComment());
		dataModel.put("className", StrUtil.lowerFirst(table.getClassName()));
		dataModel.put("ClassName", table.getClassName());
		dataModel.put("fieldList", table.getFieldList());
		dataModel.put("backendPath", table.getBackendPath());
		dataModel.put("frontendPath", table.getFrontendPath());

		return dataModel;
	}

	/**
	 * 将表字段按照类型分组并存储到数据模型中
	 * @param dataModel 存储数据的 Map 对象
	 * @param table 表信息对象
	 */
	private void setFieldTypeList(Map<String, Object> dataModel, GenTable table) {
		// 按字段类型分组，使用 Map 存储不同类型的字段列表
		Map<Boolean, List<GenTableColumnEntity>> typeMap = table.getFieldList().stream()
				.collect(Collectors.partitioningBy(GenTableColumnEntity::isPrimaryPk));

		// 从分组后的 Map 中获取不同类型的字段列表
		List<GenTableColumnEntity> primaryList = typeMap.get(true);
		List<GenTableColumnEntity> formList = typeMap.get(false).stream().filter(GenTableColumnEntity::isFormItem)
				.collect(Collectors.toList());
		List<GenTableColumnEntity> gridList = typeMap.get(false).stream().filter(GenTableColumnEntity::isGridItem)
				.collect(Collectors.toList());
		List<GenTableColumnEntity> queryList = typeMap.get(false).stream().filter(GenTableColumnEntity::isQueryItem)
				.collect(Collectors.toList());

		if (CollUtil.isNotEmpty(primaryList)) {
			dataModel.put("pk", primaryList.get(0));
		}
		dataModel.put("primaryList", primaryList);
		dataModel.put("formList", formList);
		dataModel.put("gridList", gridList);
		dataModel.put("queryList", queryList);
	}

}
