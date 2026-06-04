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
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.config.PigCodeGenDefaultProperties;
import com.pig4cloud.pig.codegen.entity.GenGroupEntity;
import com.pig4cloud.pig.codegen.entity.GenTable;
import com.pig4cloud.pig.codegen.entity.GenTableColumnEntity;
import com.pig4cloud.pig.codegen.mapper.GenTableMapper;
import com.pig4cloud.pig.codegen.service.GenGroupService;
import com.pig4cloud.pig.codegen.service.GenTableColumnService;
import com.pig4cloud.pig.codegen.service.GenTableService;
import com.pig4cloud.pig.codegen.util.*;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import org.anyline.metadata.Column;
import org.anyline.metadata.Database;
import org.anyline.metadata.Table;
import org.anyline.proxy.CacheProxy;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * 列属性
 *
 * @author pig code generator
 * @date 2023-02-06 20:34:55
 */
@Service
@RequiredArgsConstructor
public class GenTableServiceImpl extends ServiceImpl<GenTableMapper, GenTable> implements GenTableService {

	private final PigCodeGenDefaultProperties defaultProperties;

	private final GenTableColumnService columnService;

	private final GenGroupService genGroupService;

	/**
	 * 查询表ddl 语句
	 * @param dsName 数据源名称
	 * @param tableName 表名称
	 * @return ddl 语句
	 * @throws Exception
	 */
	@Override
	public String queryTableDdl(String dsName, String tableName) throws Exception {
		// 手动切换数据源
		DynamicDataSourceContextHolder.push(dsName);
		Table table = ServiceProxy.metadata().table(tableName); // 获取表结构
		table.execute(false);// 不执行SQL
		ServiceProxy.ddl().create(table);
		return table.getDdl();// 返回创建表的DDL
	}

	/**
	 * 查询表的全部字段
	 * @param dsName 数据源
	 * @param tableName 表名称
	 * @return column
	 */
	@Override
	public List<Column> queryTableColumn(String dsName, String tableName) {
		// 手动切换数据源
		DynamicDataSourceContextHolder.push(dsName);
		CacheProxy.clear();
		return ServiceProxy.metadata().columns(tableName).values().stream().toList();
	}

	/**
	 * 查询对应数据源的表
	 * @param page 分页信息
	 * @param table 查询条件
	 * @return 表
	 */
	@Override
	public IPage queryTablePage(Page<Table> page, GenTable table) {
		// 手动切换数据源
		DynamicDataSourceContextHolder.push(table.getDsName());
		CacheProxy.clear();
		List<Table> tableList = ServiceProxy.metadata().tables().values().stream().filter(t -> {
			if (StrUtil.isBlank(table.getTableName())) {
				return true;
			}

			// 通过表名和表注释进行模糊查询
			return StrUtil.containsIgnoreCase(t.getName(false), table.getTableName())
					|| StrUtil.containsIgnoreCase(t.getComment(), table.getTableName());
		})
			// 根据 createTime 、updateTime 倒序排序
			.sorted((o1, o2) -> {
				long time1 = (o1.getUpdateTime() != null ? o1.getUpdateTime().getTime()
						: (o1.getCreateTime() != null ? o1.getCreateTime().getTime() : 0));
				long time2 = (o2.getUpdateTime() != null ? o2.getUpdateTime().getTime()
						: (o2.getCreateTime() != null ? o2.getCreateTime().getTime() : 0));
				return NumberUtil.compare(time2, time1);
			})
			.toList();

		// 根据 page 进行分页
		List<Table> records = CollUtil.page((int) page.getCurrent() - 1, (int) page.getSize(), tableList);
		page.setTotal(tableList.size());
		page.setRecords(records);
		return page;
	}

	/**
	 * 查询数据源里面的全部表
	 * @param dsName 数据源名称
	 * @return table
	 */
	@Override
	public List<Table> queryTableList(String dsName) {
		// 手动切换数据源
		DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
		DynamicDataSourceContextHolder.push(dsName);
		CacheProxy.clear();
		List<Table> tableList = ServiceProxy.metadata().tables().values().stream().toList();
		if (dynamicRoutingDataSource.getDataSource(dsName) instanceof ItemDataSource itemDataSource) {
			for (Table table : tableList) {
				if (itemDataSource.getDataSource() instanceof DruidDataSource druidDataSource) {
					table.setExtend(druidDataSource.getDbType());
				}
			}
		}
		return tableList;
	}

	/**
	 * 查询表信息（列），然后插入到中间表中
	 * @param dsName 数据源
	 * @param tableName 表名
	 * @return GenTable
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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public GenTable syncTable(String dsName, String tableName) {
		return AnylineDataSourceHelper.execute(dsName, () -> {
			CacheProxy.clear();
			AnylineService service = ServiceProxy.service(dsName);
			Table tableMetadata = service.metadata().table(tableName);
			Database database = service.metadata().database();
			GenTable genTable = baseMapper.selectOne(Wrappers.<GenTable>lambdaQuery()
				.eq(GenTable::getTableName, tableName)
				.eq(GenTable::getDsName, dsName));
			if (Objects.isNull(genTable)) {
				genTable = this.buildGenTable(dsName, tableName, tableMetadata, database);
				DynamicDataSourceContextHolder.clear();
				this.save(genTable);
			}
			else {
				this.refreshTableMetadata(genTable, tableMetadata, database);
				DynamicDataSourceContextHolder.clear();
				this.updateById(genTable);
			}

			List<GenTableColumnEntity> tableFieldList = getGenTableColumnEntities(dsName, tableName, tableMetadata);
			columnService.syncFieldList(dsName, tableName, tableFieldList);
			return this.queryOrBuildTable(dsName, tableName);
		});
	}

	@Transactional(rollbackFor = Exception.class)
	protected GenTable tableImport(String dsName, String tableName) {
		return AnylineDataSourceHelper.execute(dsName, () -> {
			CacheProxy.clear();
			AnylineService service = ServiceProxy.service(dsName);
			Table tableMetadata = service.metadata().table(tableName);
			Database database = service.metadata().database();
			GenTable table = this.buildGenTable(dsName, tableName, tableMetadata, database);

			DynamicDataSourceContextHolder.clear();
			this.save(table);

			List<GenTableColumnEntity> tableFieldList = getGenTableColumnEntities(dsName, tableName, tableMetadata);
			columnService.initFieldList(tableFieldList);
			columnService.saveOrUpdateBatch(tableFieldList);

			table.setFieldList(tableFieldList);
			return table;
		});
	}

	private GenTable buildGenTable(String dsName, String tableName, Table tableMetadata, Database database) {
		GenTable table = new GenTable();
		table.setPackageName(defaultProperties.getPackageName());
		table.setVersion(defaultProperties.getVersion());
		table.setBackendPath(defaultProperties.getBackendPath());
		table.setFrontendPath(defaultProperties.getFrontendPath());
		table.setAuthor(defaultProperties.getAuthor());
		table.setEmail(defaultProperties.getEmail());
		table.setTableName(tableName);
		table.setDsName(dsName);
		table.setTableComment(tableMetadata.getComment());
		table.setDbType(database.getDatabaseType().title());
		table.setFormLayout(defaultProperties.getFormLayout());
		table.setSyncRoute(defaultProperties.getSyncRoute());
		table.setGeneratorType(defaultProperties.getGeneratorType());
		table.setClassName(NamingCase.toPascalCase(tableName));
		table.setModuleName(defaultProperties.getModuleName());
		table.setFunctionName(GenKit.getFunctionName(tableName));
		table.setCreateTime(LocalDateTime.now());
		return table;
	}

	private void refreshTableMetadata(GenTable table, Table tableMetadata, Database database) {
		table.setTableComment(tableMetadata.getComment());
		table.setDbType(database.getDatabaseType().title());
	}

	/**
	 * 获取表字段信息
	 * @param dsName 数据源信息
	 * @param tableName 表名称
	 * @param tableMetadata 表的元数据
	 * @return list
	 */
	private static @NotNull List<GenTableColumnEntity> getGenTableColumnEntities(String dsName, String tableName,
			Table tableMetadata) {
		List<GenTableColumnEntity> tableFieldList = new ArrayList<>();
		LinkedHashMap<String, Column> columns = tableMetadata.getColumns();
		columns.forEach((columnName, column) -> {
			GenTableColumnEntity genTableColumnEntity = new GenTableColumnEntity();
			genTableColumnEntity.setTableName(tableName);
			genTableColumnEntity.setDsName(dsName);
			genTableColumnEntity.setFieldName(column.getName());
			genTableColumnEntity.setFieldComment(column.getComment());
			genTableColumnEntity.setFieldType(column.getTypeName());
			genTableColumnEntity
				.setPrimaryPk(column.isPrimaryKey() ? BoolFillEnum.TRUE.getValue() : BoolFillEnum.FALSE.getValue());
			genTableColumnEntity.setAutoFill(AutoFillEnum.DEFAULT.name());
			genTableColumnEntity.setFormItem(BoolFillEnum.TRUE.getValue());
			genTableColumnEntity.setGridItem(BoolFillEnum.TRUE.getValue());

			// 审计字段处理
			if (EnumUtil.contains(CommonColumnFiledEnum.class, column.getName())) {
				CommonColumnFiledEnum commonColumnFiledEnum = CommonColumnFiledEnum.valueOf(column.getName());
				genTableColumnEntity.setFormItem(commonColumnFiledEnum.getFormItem());
				genTableColumnEntity.setGridItem(commonColumnFiledEnum.getGridItem());
				genTableColumnEntity.setAutoFill(commonColumnFiledEnum.getAutoFill());
				genTableColumnEntity.setSort(commonColumnFiledEnum.getSort());
			}
			tableFieldList.add(genTableColumnEntity);
		});
		return tableFieldList;
	}

}
