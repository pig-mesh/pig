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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.codegen.config.PigCodeGenDefaultProperties;
import com.pig4cloud.pig.codegen.entity.GenTable;
import com.pig4cloud.pig.codegen.entity.GenTableColumnEntity;
import com.pig4cloud.pig.codegen.entity.GenTemplateEntity;
import com.pig4cloud.pig.codegen.service.*;
import com.pig4cloud.pig.codegen.util.VelocityKit;
import com.pig4cloud.pig.codegen.util.vo.GroupVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

	private final PigCodeGenDefaultProperties configurationProperties;

	private final GenTableColumnService columnService;

	private final GenFieldTypeService fieldTypeService;

	private final GenTableService tableService;

	private final GenGroupService genGroupService;

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

		GroupVO groupVo = genGroupService.getGroupVoById(style);
		List<GenTemplateEntity> templateList = groupVo.getTemplateList();

		String frontendPath = configurationProperties.getFrontendPath();
		String backendPath = configurationProperties.getBackendPath();

		for (GenTemplateEntity template : templateList) {
			String templateCode = template.getTemplateCode();
			String generatorPath = template.getGeneratorPath();

			dataModel.put("frontendPath", frontendPath);
			dataModel.put("backendPath", backendPath);
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

		String frontendPath = configurationProperties.getFrontendPath();
		String backendPath = configurationProperties.getBackendPath();

		return templateList.stream().map(template -> {
			String templateCode = template.getTemplateCode();
			String generatorPath = template.getGeneratorPath();

			// 预览模式下, 使用相对路径展示
			dataModel.put("frontendPath", frontendPath);
			dataModel.put("backendPath", backendPath);
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
			.eq(GenTableColumnEntity::getTableName, table.getTableName())
			.orderByAsc(GenTableColumnEntity::getSort)
			.list();

		table.setFieldList(fieldList);

		// 创建数据模型对象
		Map<String, Object> dataModel = new HashMap<>();

		// 填充数据模型
		dataModel.put("opensource", true);
		dataModel.put("isSpringBoot3", isSpringBoot3());
		dataModel.put("dbType", table.getDbType());
		dataModel.put("package", table.getPackageName());
		dataModel.put("packagePath", table.getPackageName().replace(".", "/"));
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

		// 设置子表
		String childTableName = table.getChildTableName();
		if (StrUtil.isNotBlank(childTableName)) {
			List<GenTableColumnEntity> childFieldList = columnService.lambdaQuery()
				.eq(GenTableColumnEntity::getDsName, table.getDsName())
				.eq(GenTableColumnEntity::getTableName, table.getChildTableName())
				.list();
			dataModel.put("childFieldList", childFieldList);
			dataModel.put("childTableName", childTableName);
			dataModel.put("mainField", NamingCase.toCamelCase(table.getMainField()));
			dataModel.put("childField", NamingCase.toCamelCase(table.getChildField()));
			dataModel.put("ChildClassName", NamingCase.toPascalCase(childTableName));
			dataModel.put("childClassName", StrUtil.lowerFirst(NamingCase.toPascalCase(childTableName)));
			// 设置是否是多租户模式 (判断字段列表中是否包含 tenant_id 字段)
			childFieldList.stream()
				.filter(genTableColumnEntity -> genTableColumnEntity.getFieldName().equals("tenant_id"))
				.findFirst()
				.ifPresent(columnEntity -> dataModel.put("isChildTenant", true));
		}

		// 设置是否是多租户模式 (判断字段列表中是否包含 tenant_id 字段)
		table.getFieldList()
			.stream()
			.filter(genTableColumnEntity -> genTableColumnEntity.getFieldName().equals("tenant_id"))
			.findFirst()
			.ifPresent(columnEntity -> dataModel.put("isTenant", true));

		return dataModel;
	}

	/**
	 * 判断当前是否是 SpringBoot3 版本
	 * @return true/fasle
	 */
	private boolean isSpringBoot3() {
		return StrUtil.startWith(SpringBootVersion.getVersion(), "3");
	}

	/**
	 * 将表字段按照类型分组并存储到数据模型中
	 * @param dataModel 存储数据的 Map 对象
	 * @param table 表信息对象
	 */
	private void setFieldTypeList(Map<String, Object> dataModel, GenTable table) {
		// 按字段类型分组，使用 Map 存储不同类型的字段列表
		Map<Boolean, List<GenTableColumnEntity>> typeMap = table.getFieldList()
			.stream()
			.collect(Collectors.partitioningBy(columnEntity -> BooleanUtil.toBoolean(columnEntity.getPrimaryPk())));

		// 从分组后的 Map 中获取不同类型的字段列表
		List<GenTableColumnEntity> primaryList = typeMap.get(true);
		List<GenTableColumnEntity> formList = typeMap.get(false)
			.stream()
			.filter(columnEntity -> BooleanUtil.toBoolean(columnEntity.getFormItem()))
			.collect(Collectors.toList());
		List<GenTableColumnEntity> gridList = typeMap.get(false)
			.stream()
			.filter(columnEntity -> BooleanUtil.toBoolean(columnEntity.getGridItem()))
			.collect(Collectors.toList());
		List<GenTableColumnEntity> queryList = typeMap.get(false)
			.stream()
			.filter(columnEntity -> BooleanUtil.toBoolean(columnEntity.getQueryItem()))
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
