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
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.NamingCase;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.admin.api.entity.SysMenu;
import com.pig4cloud.pigx.admin.api.entity.SysRouteConf;
import com.pig4cloud.pigx.admin.api.feign.RemoteMenuService;
import com.pig4cloud.pigx.admin.api.feign.RemoteRouteConfService;
import com.pig4cloud.pigx.codegen.config.PigxCodeGenDefaultProperties;
import com.pig4cloud.pigx.codegen.entity.GenFormConf;
import com.pig4cloud.pigx.codegen.entity.GenTable;
import com.pig4cloud.pigx.codegen.entity.GenTableColumnEntity;
import com.pig4cloud.pigx.codegen.entity.GenTemplateEntity;
import com.pig4cloud.pigx.codegen.service.*;
import com.pig4cloud.pigx.codegen.util.DataModelConstants;
import com.pig4cloud.pigx.codegen.util.GeneratorStyleEnum;
import com.pig4cloud.pigx.codegen.util.VelocityKit;
import com.pig4cloud.pigx.codegen.util.vo.GroupVO;
import com.pig4cloud.pigx.common.core.constant.enums.MenuTypeEnum;
import com.pig4cloud.pigx.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pigx.common.core.exception.CheckedException;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.RetOps;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootVersion;
import org.springframework.stereotype.Service;

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

    private final PigxCodeGenDefaultProperties defaultProperties;

    private final GenTableColumnService columnService;

    private final GenFormConfService formConfService;

    private final GenFieldTypeService fieldTypeService;

    private final GenTableService tableService;

    private final GenGroupService genGroupService;

    private final GenTemplateService genTemplateService;

    private final RemoteMenuService menuService;

    private final RemoteRouteConfService routeService;

    /**
     * 生成代码zip写出
     *
     * @param tableId 表
     * @param zip     输出流
     */
    @Override
    @SneakyThrows
    public void downloadCode(Long tableId, ZipOutputStream zip) {
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableId);

        Long style = (Long) dataModel.get(GenTable.Fields.style);

        GroupVO groupVo = genGroupService.getGroupVoById(style);
        List<GenTemplateEntity> templateList = groupVo.getTemplateList();

        String frontendPath = defaultProperties.getFrontendPath();
        String backendPath = defaultProperties.getBackendPath();

        // 同步数据
        this.syncRouteAndMenu(tableId);

        for (GenTemplateEntity template : templateList) {
            String templateCode = template.getTemplateCode();
            String generatorPath = template.getGeneratorPath();

            dataModel.put(GenTable.Fields.frontendPath, frontendPath);
            dataModel.put(GenTable.Fields.backendPath, backendPath);
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
     *
     * @param tableId 表
     * @return [{模板名称:渲染结果}]
     */
    @Override
    @SneakyThrows
    public List<Map<String, String>> preview(Long tableId) {
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableId);

        Long style = (Long) dataModel.get(GenTable.Fields.style);

        // 获取模板列表，Lambda 表达式简化代码
        List<GenTemplateEntity> templateList = genGroupService.getGroupVoById(style).getTemplateList();

        String frontendPath = defaultProperties.getFrontendPath();
        String backendPath = defaultProperties.getBackendPath();

        // 如果是同步菜单的模式则不生成SQL
        List<Map<String, String>> result = new ArrayList<>();
        Long syncMenuId = MapUtil.getLong(dataModel, GenTable.Fields.syncMenuId);
        for (GenTemplateEntity template : templateList) {
            // 跳过菜单文件生成
            if (Objects.nonNull(syncMenuId) && template.getGeneratorPath().contains("menu.sql")) {
                continue;
            }

            String templateCode = template.getTemplateCode();
            String generatorPath = template.getGeneratorPath();

            // 预览模式下, 使用相对路径展示
            dataModel.put(GenTable.Fields.frontendPath, frontendPath);
            dataModel.put(GenTable.Fields.backendPath, backendPath);
            String content = VelocityKit.renderStr(templateCode, dataModel);
            String path = VelocityKit.renderStr(generatorPath, dataModel);

            // 使用 map 简化代码
            result.add(new HashMap<>(4) {
                {
                    put("code", content);
                    put("codePath", path);
                }
            });
        }

        return result;
    }

    /**
     * 目标目录写入渲染结果方法
     *
     * @param tableId 表
     */
    @Override
    public void generatorCode(Long tableId) {
        // 数据模型
        Map<String, Object> dataModel = getDataModel(tableId);
        Long style = (Long) dataModel.get(GenTable.Fields.style);

        // 获取模板列表，Lambda 表达式简化代码
        List<GenTemplateEntity> templateList = genGroupService.getGroupVoById(style).getTemplateList();
        Long syncMenuId = MapUtil.getLong(dataModel, GenTable.Fields.syncMenuId);
        this.syncRouteAndMenu(tableId);
        for (GenTemplateEntity template : templateList) {
            // 跳过菜单文件生成
            if (Objects.nonNull(syncMenuId) && template.getGeneratorPath().contains("menu.sql")) {
                continue;
            }

            String templateCode = template.getTemplateCode();
            String generatorPath = template.getGeneratorPath();
            String content = VelocityKit.renderStr(templateCode, dataModel);
            String path = VelocityKit.renderStr(generatorPath, dataModel);
            FileUtil.writeUtf8String(content, path);
        }
    }

    /**
     * 获取表单设计器需要的 JSON 方法
     *
     * @param dsName    数据源名称
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
        GenTemplateEntity genTemplateEntity = genTemplateService.getOneOpt(Wrappers.<GenTemplateEntity>lambdaQuery()
                        .likeRight(GenTemplateEntity::getTemplateName, GeneratorStyleEnum.VFORM_JSON.getDesc())
                        .orderByDesc(GenTemplateEntity::getCreateTime), false
                )
                .orElseThrow(() -> new CheckedException("模板不存在"));
        // 渲染模板并返回结果
        return VelocityKit.renderStr(genTemplateEntity.getTemplateCode(), dataModel);
    }

    /**
     * 获取sfc vue
     *
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
        Map<String, List<JSONObject>> resultMap = formConfService.parse(formConf.getFormInfo());

        // 遍历 widgetList
        dataModel.put("resultMap", resultMap);

        // 获取模板信息 查询模板中最新的 vform.json 文件
        GenTemplateEntity genTemplateEntity = genTemplateService.getOneOpt(Wrappers.<GenTemplateEntity>lambdaQuery()
                        .likeRight(GenTemplateEntity::getTemplateName, GeneratorStyleEnum.VFORM_VUE.getDesc())
                        .orderByDesc(GenTemplateEntity::getCreateTime), false
                )
                .orElseThrow(() -> new CheckedException("模板不存在"));

        // 渲染模板并返回结果
        return VelocityKit.renderStr(genTemplateEntity.getTemplateCode(), dataModel);
    }

    /**
     * 同步路由和菜单
     *
     * @param tableId 表ID
     */
    @Override
    public void syncRouteAndMenu(Long tableId) {
        GenTable table = tableService.getById(tableId);
        syncMenu(table);
        syncRoute(table);
    }

    /**
     * 同步菜单，同步按钮
     *
     * @param table 表配置
     */
    private void syncMenu(GenTable table) {
        if (Objects.isNull(table.getSyncMenuId())) {
            return;
        }

        String menuName = String.format("%s管理", table.getTableComment());
        SysMenu query = new SysMenu();
        query.setName(menuName);
        query.setMenuType(MenuTypeEnum.LEFT_MENU.getType());

        List<SysMenu> existingMenus = RetOps.of(menuService.getMenuDetails(query)).getData().orElse(Collections.emptyList());

        if (!CollUtil.isEmpty(existingMenus)) {
            return;
        }

        SysMenu sysMenu = createMenu(table, menuName);
        R<SysMenu> sysMenuR = menuService.saveMenu(sysMenu);

        if (sysMenuR.getData() != null) {
            createButtons(table, sysMenuR.getData().getMenuId());
        }
    }

    /**
     * 创建菜单
     *
     * @param table    表配置
     * @param menuName 菜单名称
     * @return {@link SysMenu }
     */
    private SysMenu createMenu(GenTable table, String menuName) {
        SysMenu sysMenu = new SysMenu();
        sysMenu.setParentId(table.getSyncMenuId());
        sysMenu.setName(menuName);
        sysMenu.setMenuType(MenuTypeEnum.LEFT_MENU.getType());
        sysMenu.setPath(String.format("/%s/%s/index", table.getModuleName(), table.getFunctionName()));
        return sysMenu;
    }

    /**
     * 创建按钮
     *
     * @param table    表配置
     * @param parentId 父 ID
     */
    private void createButtons(GenTable table, Long parentId) {
        String[] buttonNames = {"查看", "新增", "编辑", "删除", "导入导出"};
        String[] permissions = {"view", "add", "edit", "del", "export"};

        for (int i = 0; i < buttonNames.length; i++) {
            SysMenu button = new SysMenu();
            button.setParentId(parentId);
            button.setMenuType(MenuTypeEnum.BUTTON.getType());
            button.setName(buttonNames[i]);
            button.setPermission(String.format("%s_%s_%s", table.getModuleName(), table.getFunctionName(), permissions[i]));
            menuService.saveMenu(button);
        }
    }

    /**
     * 同步路由
     *
     * @param table 表配置
     */
    private void syncRoute(GenTable table) {
        if (!YesNoEnum.YES.getCode().equals(table.getSyncRoute())) {
            return;
        }

        List<SysRouteConf> existingRoutes = RetOps.of(routeService.getRouteDetails()).getData().orElse(Collections.emptyList());
        boolean exist = existingRoutes.stream()
                .anyMatch(routeConf -> routeConf.getRouteId().equals(table.getModuleName()));

        if (exist) {
            return;
        }

        SysRouteConf sysRouteConf = createRoute(table);
        routeService.saveSysRouteConf(sysRouteConf);
    }

    /**
     * 创建路由
     *
     * @param table 表配置
     * @return {@link SysRouteConf }
     */
    private SysRouteConf createRoute(GenTable table) {
        SysRouteConf sysRouteConf = new SysRouteConf();
        sysRouteConf.setRouteId(table.getModuleName());
        sysRouteConf.setRouteName(table.getModuleName());
        sysRouteConf.setPredicates(String.format("[{\"args\": {\"_genkey_0\": \"/%s/**\"}, \"name\": \"Path\"}]", table.getModuleName()));
        sysRouteConf.setFilters("[]");
        sysRouteConf.setUri(String.format("lb://%s-biz", table.getModuleName()));
        return sysRouteConf;
    }

    /**
     * 通过 Lambda 表达式优化的获取数据模型方法
     *
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
        dataModel.put(DataModelConstants.IS_SPRING_BOOT_3, isSpringBoot3());
        dataModel.put(DataModelConstants.SYNC_MENU_ID, Objects.nonNull(table.getSyncMenuId()));
        dataModel.put(DataModelConstants.DB_TYPE, table.getDbType());
        dataModel.put(DataModelConstants.PACKAGE, table.getPackageName());
        dataModel.put(DataModelConstants.PACKAGE_PATH, table.getPackageName().replace(".", "/"));
        dataModel.put(DataModelConstants.VERSION, table.getVersion());
        dataModel.put(DataModelConstants.MODULE_NAME, table.getModuleName());
        dataModel.put(DataModelConstants.MODULE_NAME_UPPER_FIRST, StrUtil.upperFirst(table.getModuleName()));
        dataModel.put(DataModelConstants.FUNCTION_NAME, table.getFunctionName());
        dataModel.put(DataModelConstants.FUNCTION_NAME_UPPER_FIRST, StrUtil.upperFirst(table.getFunctionName()));
        dataModel.put(DataModelConstants.FORM_LAYOUT, table.getFormLayout());
        dataModel.put(DataModelConstants.STYLE, table.getStyle());
        dataModel.put(DataModelConstants.AUTHOR, table.getAuthor());
        dataModel.put(DataModelConstants.DATETIME, DateUtil.now());
        dataModel.put(DataModelConstants.DATE, DateUtil.today());
        setFieldTypeList(dataModel, table);

        // 获取导入的包列表
        Set<String> importList = fieldTypeService.getPackageByTableId(table.getDsName(), table.getTableName());
        dataModel.put(DataModelConstants.IMPORT_LIST, importList);
        dataModel.put(DataModelConstants.TABLE_NAME, table.getTableName());
        dataModel.put(DataModelConstants.TABLE_COMMENT, table.getTableComment());
        dataModel.put(DataModelConstants.CLASS_NAME, StrUtil.lowerFirst(table.getClassName()));
        dataModel.put(DataModelConstants.CLASS_NAME_UPPER_FIRST, table.getClassName());
        dataModel.put(DataModelConstants.FIELD_LIST, table.getFieldList());

        dataModel.put(DataModelConstants.BACKEND_PATH, table.getBackendPath());
        dataModel.put(DataModelConstants.FRONTEND_PATH, table.getFrontendPath());

        // 设置子表
        String childTableName = table.getChildTableName();
        if (StrUtil.isNotBlank(childTableName)) {
            List<GenTableColumnEntity> childFieldList = columnService.lambdaQuery()
                    .eq(GenTableColumnEntity::getDsName, table.getDsName())
                    .eq(GenTableColumnEntity::getTableName, table.getChildTableName())
                    .list();
            dataModel.put(DataModelConstants.CHILD_FIELD_LIST, childFieldList);
            dataModel.put(DataModelConstants.CHILD_TABLE_NAME, childTableName);
            dataModel.put(DataModelConstants.MAIN_FIELD, NamingCase.toCamelCase(table.getMainField()));
            dataModel.put(DataModelConstants.CHILD_FIELD, NamingCase.toCamelCase(table.getChildField()));
            dataModel.put(DataModelConstants.CHILD_CLASS_NAME_UPPER_FIRST, NamingCase.toPascalCase(childTableName));
            dataModel.put(DataModelConstants.CHILD_CLASS_NAME, StrUtil.lowerFirst(NamingCase.toPascalCase(childTableName)));
            // 设置是否是多租户模式 (判断字段列表中是否包含 tenant_id 字段)
            childFieldList.stream().filter(genTableColumnEntity -> genTableColumnEntity.getFieldName().equals("tenant_id"))
                    .findFirst().ifPresent(columnEntity -> dataModel.put(DataModelConstants.IS_CHILD_TENANT, true));
        }

        // 设置是否是多租户模式 (判断字段列表中是否包含 tenant_id 字段)
        table.getFieldList().stream().filter(genTableColumnEntity -> genTableColumnEntity.getFieldName().equals("tenant_id"))
                .findFirst().ifPresent(columnEntity -> dataModel.put(DataModelConstants.IS_TENANT, true));

        return dataModel;
    }

    /**
     * 判断当前是否是 SpringBoot3 版本
     *
     * @return true/fasle
     */
    private boolean isSpringBoot3() {
        return StrUtil.startWith(SpringBootVersion.getVersion(), "3");
    }

    /**
     * 将表字段按照类型分组并存储到数据模型中
     *
     * @param dataModel 存储数据的 Map 对象
     * @param table     表信息对象
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
