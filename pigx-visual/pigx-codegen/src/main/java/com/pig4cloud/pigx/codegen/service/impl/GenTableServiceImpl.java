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
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.EnumUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.codegen.entity.GenGroupEntity;
import com.pig4cloud.pigx.codegen.entity.GenTable;
import com.pig4cloud.pigx.codegen.entity.GenTableColumnEntity;
import com.pig4cloud.pigx.codegen.mapper.GenTableMapper;
import com.pig4cloud.pigx.codegen.service.GenGroupService;
import com.pig4cloud.pigx.codegen.service.GenTableColumnService;
import com.pig4cloud.pigx.codegen.service.GenTableService;
import com.pig4cloud.pigx.codegen.util.*;
import lombok.RequiredArgsConstructor;
import org.anyline.metadata.Column;
import org.anyline.metadata.Database;
import org.anyline.metadata.Table;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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
     *
     * @return map
     */
    @Override
    public Map<String, Object> getGeneratorConfig() {
        ClassPathResource classPathResource = new ClassPathResource(CONFIG_PATH);
        JSONObject jsonObject = JSONUtil.parseObj(IoUtil.readUtf8(classPathResource.getStream()));
        return jsonObject.getRaw();
    }

    /**
     * 查询表ddl 语句
     *
     * @param dsName    数据源名称
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
     * 查询对应数据源的表
     *
     * @param page  分页信息
     * @param table 查询条件
     * @return 表
     */
    @Override
    public IPage queryTablePage(Page<Table> page, GenTable table) {
        // 手动切换数据源
        DynamicDataSourceContextHolder.push(table.getDsName());
        List<Table> tableList = ServiceProxy.metadata().tables().values().stream()
                .filter(t -> StrUtil.containsIgnoreCase(t.getTableName(), table.getTableName())).toList();

        // 根据 page 进行分页
        List<Table> records = CollUtil.page((int) page.getCurrent() - 1, (int) page.getSize(), tableList);
        page.setTotal(tableList.size());
        page.setRecords(records);
        return page;
    }

    /**
     * 查询表信息（列），然后插入到中间表中
     *
     * @param dsName    数据源
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

    @Transactional(rollbackFor = Exception.class)
    protected GenTable tableImport(String dsName, String tableName) {
        // 手动切换数据源
        DynamicDataSourceContextHolder.push(dsName);

        // 查询表是否存在
        GenTable table = new GenTable();
        // 从数据库获取表信息
        AnylineService service = ServiceProxy.service();
        Table tableMetadata = service.metadata().table(tableName);
        Database database = service.metadata().database();
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
        table.setTableComment(tableMetadata.getComment());

        table.setDbType(database.getDatabase().title());
        table.setFormLayout(FormLayoutEnum.TWO.getValue());
        table.setGeneratorType(GeneratorTypeEnum.ZIP_DOWNLOAD.getValue());
        table.setClassName(NamingCase.toPascalCase(tableName));
        table.setModuleName(GenKit.getModuleName(table.getPackageName()));
        table.setFunctionName(GenKit.getFunctionName(tableName));
        table.setCreateTime(LocalDateTime.now());

        // 使用默认数据源
        DynamicDataSourceContextHolder.clear();
        this.save(table);

        // 获取原生字段数据
        List<GenTableColumnEntity> tableFieldList = getGenTableColumnEntities(dsName, tableName, tableMetadata);

        // 初始化字段数据
        columnService.initFieldList(tableFieldList);
        // 保存列数据
        columnService.saveOrUpdateBatch(tableFieldList);

        table.setFieldList(tableFieldList);
        return table;
    }

    /**
     * 获取表字段信息
     *
     * @param dsName        数据源信息
     * @param tableName     表名称
     * @param tableMetadata 表的元数据
     * @return list
     */
    private static @NotNull List<GenTableColumnEntity> getGenTableColumnEntities(String dsName, String tableName, Table tableMetadata) {
        List<GenTableColumnEntity> tableFieldList = new ArrayList<>();
        LinkedHashMap<String, Column> columns = tableMetadata.getColumns();
        columns.forEach((columnName, column) -> {
            GenTableColumnEntity genTableColumnEntity = new GenTableColumnEntity();
            genTableColumnEntity.setTableName(tableName);
            genTableColumnEntity.setDsName(dsName);
            genTableColumnEntity.setFieldName(column.getName());
            genTableColumnEntity.setFieldComment(column.getComment());
            genTableColumnEntity.setFieldType(column.getTypeName());
            genTableColumnEntity.setPrimaryPk(column.isPrimaryKey() == 1 ? BoolFillEnum.TRUE.getValue() : BoolFillEnum.FALSE.getValue());
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
