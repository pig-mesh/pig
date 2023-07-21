DROP DATABASE IF EXISTS `pig_codegen`;

CREATE DATABASE  `pig_codegen` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
USE pig_codegen;

-- ----------------------------
-- Table structure for gen_datasource_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_datasource_conf`;
CREATE TABLE `gen_datasource_conf` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '别名',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'jdbcurl',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  `ds_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库类型',
  `conf_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置类型',
  `ds_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库名称',
  `instance` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '实例',
  `port` int DEFAULT NULL COMMENT '端口',
  `host` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主机',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据源表';

-- ----------------------------
-- Records of gen_datasource_conf
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_field_type
-- ----------------------------
DROP TABLE IF EXISTS `gen_field_type`;
CREATE TABLE `gen_field_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `column_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段类型',
  `attr_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性包名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `column_type` (`column_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1634915190321451010 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字段类型管理';

-- ----------------------------
-- Records of gen_field_type
-- ----------------------------
BEGIN;
INSERT INTO `gen_field_type` VALUES (1, 'datetime', 'LocalDateTime', 'java.time.LocalDateTime', '2023-02-06 08:45:10', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (2, 'date', 'LocalDate', 'java.time.LocalDate', '2023-02-06 08:45:10', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (3, 'tinyint', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (4, 'smallint', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (5, 'mediumint', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (6, 'int', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (7, 'integer', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (8, 'bigint', 'Long', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (9, 'float', 'Float', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (10, 'double', 'Double', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (11, 'decimal', 'BigDecimal', 'java.math.BigDecimal', '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (12, 'bit', 'Boolean', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (13, 'char', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (14, 'varchar', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (15, 'tinytext', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (16, 'text', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (17, 'mediumtext', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (18, 'longtext', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (19, 'timestamp', 'LocalDateTime', 'java.time.LocalDateTime', '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (20, 'NUMBER', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (21, 'BINARY_INTEGER', 'Integer', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (22, 'BINARY_FLOAT', 'Float', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (23, 'BINARY_DOUBLE', 'Double', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (24, 'VARCHAR2', 'String', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (25, 'NVARCHAR', 'String', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (26, 'NVARCHAR2', 'String', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (27, 'CLOB', 'String', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (28, 'int8', 'Long', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (29, 'int4', 'Integer', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (30, 'int2', 'Integer', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (31, 'numeric', 'BigDecimal', 'java.math.BigDecimal', '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (32, 'json', 'String', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for gen_group
-- ----------------------------
DROP TABLE IF EXISTS `gen_group`;
CREATE TABLE `gen_group` (
  `id` bigint NOT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组名称',
  `group_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组描述',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='模板分组';

-- ----------------------------
-- Records of gen_group
-- ----------------------------
BEGIN;
INSERT INTO `gen_group` VALUES (1, '单表增删改查', '单表增删改查', 1, ' ', 'admin', NULL, '2023-07-07 15:47:51', '0');
COMMIT;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表名',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `db_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库类型',
  `table_comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '说明',
  `author` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作者',
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目包名',
  `version` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目版本号',
  `i18n` tinyint DEFAULT '1' COMMENT '是否生成带有i18n 0 不带有 1带有',
  `style` bigint DEFAULT '0' COMMENT '代码风格',
  `child_table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '子表名称',
  `main_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主表关联键',
  `child_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '子表关联键',
  `generator_type` tinyint DEFAULT NULL COMMENT '生成方式  0：zip压缩包   1：自定义目录',
  `backend_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '后端生成路径',
  `frontend_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '前端生成路径',
  `module_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模块名',
  `function_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '功能名',
  `form_layout` tinyint DEFAULT NULL COMMENT '表单布局  1：一列   2：两列',
  `ds_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据源ID',
  `baseclass_id` bigint DEFAULT NULL COMMENT '基类ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_name` (`table_name`,`ds_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ds_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据源名称',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表名称',
  `field_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段名称',
  `field_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段类型',
  `field_comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段说明',
  `attr_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性名',
  `attr_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性包名',
  `sort` int DEFAULT NULL COMMENT '排序',
  `auto_fill` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '自动填充  DEFAULT、INSERT、UPDATE、INSERT_UPDATE',
  `primary_pk` tinyint DEFAULT NULL COMMENT '主键 0：否  1：是',
  `base_field` tinyint DEFAULT NULL COMMENT '基类字段 0：否  1：是',
  `form_item` tinyint DEFAULT NULL COMMENT '表单项 0：否  1：是',
  `form_required` tinyint DEFAULT NULL COMMENT '表单必填 0：否  1：是',
  `form_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表单类型',
  `form_validator` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表单效验',
  `grid_item` tinyint DEFAULT NULL COMMENT '列表项 0：否  1：是',
  `grid_sort` tinyint DEFAULT NULL COMMENT '列表排序 0：否  1：是',
  `query_item` tinyint DEFAULT NULL COMMENT '查询项 0：否  1：是',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '查询方式',
  `query_form_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '查询表单类型',
  `field_dict` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_template
-- ----------------------------
DROP TABLE IF EXISTS `gen_template`;
CREATE TABLE `gen_template` (
  `id` bigint NOT NULL COMMENT '主键',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `generator_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板路径',
  `template_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板描述',
  `template_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板代码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标记',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='模板';

-- ----------------------------
-- Records of gen_template
-- ----------------------------
BEGIN;
INSERT INTO `gen_template` VALUES (1, '表格', '${frontendPath}/src/views/${moduleName}/${functionName}/index.vue', '表格不含i18n', '<template>\n  <div class=\"layout-padding\">\n    <div class=\"layout-padding-auto layout-padding-view\">\n#if($queryList)\n      <el-row v-show=\"showSearch\">\n        <el-form :model=\"state.queryForm\" ref=\"queryRef\" :inline=\"true\" @keyup.enter=\"getDataList\">\n#foreach($field in $queryList)\n#if($field.queryFormType == \'select\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-select v-model=\"state.queryForm.${field.attrName}\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\">\n       #if($field.fieldDict)\n              <el-option :label=\"item.label\" :value=\"item.value\" v-for=\"(item, index) in ${field.fieldDict}\" :key=\"index\"></el-option>\n         #else\n              <el-option label=\"请选择\">0</el-option>\n         #end\n            </el-select>\n      </el-form-item>\n#elseif($field.queryFormType == \'date\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n      <el-date-picker type=\"date\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" v-model=\"state.queryForm.${field.attrName}\"></el-date-picker>\n      </el-form-item>\n#elseif($field.queryFormType == \'datetime\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\" >\n            <el-date-picker type=\"datetime\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" v-model=\"state.queryForm.${field.attrName}\"></el-date-picker>\n      </el-form-item>\n      </el-col>\n#else\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\" >\n        <el-input placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" v-model=\"state.queryForm.${field.attrName}\" />\n      </el-form-item>\n#end\n#end\n          <el-form-item>\n            <el-button icon=\"search\" type=\"primary\" @click=\"getDataList\">\n              查询\n            </el-button>\n            <el-button icon=\"Refresh\" @click=\"resetQuery\">重置</el-button>\n          </el-form-item>\n        </el-form>\n      </el-row>\n#end\n      <el-row>\n        <div class=\"mb8\" style=\"width: 100%\">\n          <el-button icon=\"folder-add\" type=\"primary\" class=\"ml10\" @click=\"formDialogRef.openDialog()\"\n            v-auth=\"\'${moduleName}_${functionName}_add\'\">\n            新 增\n          </el-button>\n          <el-button plain :disabled=\"multiple\" icon=\"Delete\" type=\"primary\"\n            v-auth=\"\'${moduleName}_${functionName}_del\'\" @click=\"handleDelete(selectObjs)\">\n            删除\n          </el-button>\n          <right-toolbar v-model:showSearch=\"showSearch\" :export=\"\'${moduleName}_${functionName}_export\'\"\n                @exportExcel=\"exportExcel\" class=\"ml10 mr20\" style=\"float: right;\"\n            @queryTable=\"getDataList\"></right-toolbar>\n        </div>\n      </el-row>\n      <el-table :data=\"state.dataList\" v-loading=\"state.loading\" border :cell-style=\"tableStyle.cellStyle\" :header-cell-style=\"tableStyle.headerCellStyle\" 	@selection-change=\"handleSelectionChange\">\n        <el-table-column type=\"selection\" width=\"40\" align=\"center\" />\n        <el-table-column type=\"index\" label=\"#\" width=\"40\" />\n      #foreach($field in $gridList)\n        #if($field.fieldDict)\n          <el-table-column prop=\"${field.attrName}\" label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" show-overflow-tooltip>\n      <template #default=\"scope\">\n                <dict-tag :options=\"$field.fieldDict\" :value=\"scope.row.${field.attrName}\"></dict-tag>\n            </template>\n          </el-table-column>\n        #else\n          <el-table-column prop=\"${field.attrName}\" label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" #if(${field.gridSort})sortable=\"custom\"#end show-overflow-tooltip/>\n        #end\n     #end\n        <el-table-column label=\"操作\" width=\"150\">\n          <template #default=\"scope\">\n            <el-button icon=\"edit-pen\" text type=\"primary\" v-auth=\"\'${moduleName}_${functionName}_edit\'\"\n              @click=\"formDialogRef.openDialog(scope.row.${pk.attrName})\">编辑</el-button>\n            <el-button icon=\"delete\" text type=\"primary\" v-auth=\"\'${moduleName}_${functionName}_del\'\" @click=\"handleDelete([scope.row.${pk.attrName}])\">删除</el-button>\n          </template>\n        </el-table-column>\n      </el-table>\n      <pagination @size-change=\"sizeChangeHandle\" @current-change=\"currentChangeHandle\" v-bind=\"state.pagination\" />\n    </div>\n\n    <!-- 编辑、新增  -->\n    <form-dialog ref=\"formDialogRef\" @refresh=\"getDataList(false)\" />\n\n  </div>\n</template>\n\n<script setup lang=\"ts\" name=\"system${ClassName}\">\nimport { BasicTableProps, useTable } from \"/@/hooks/table\";\nimport { fetchList, delObjs } from \"/@/api/${moduleName}/${functionName}\";\nimport { useMessage, useMessageBox } from \"/@/hooks/message\";\nimport { useDict } from \'/@/hooks/dict\';\n\n// 引入组件\nconst FormDialog = defineAsyncComponent(() => import(\'./form.vue\'));\n// 定义查询字典\n#set($fieldDict=[])\n#foreach($field in $queryList)\n  #if($field.fieldDict)\n    #set($void=$fieldDict.add($field.fieldDict))\n  #end\n#end\n\n#foreach($field in $gridList)\n  #if($field.fieldDict)\n    #set($void=$fieldDict.add($field.fieldDict))\n  #end\n#end\n#if($fieldDict)\nconst { $dict.format($fieldDict) } = useDict($dict.quotation($fieldDict))\n#end\n// 定义变量内容\nconst formDialogRef = ref()\n// 搜索变量\nconst queryRef = ref()\nconst showSearch = ref(true)\n// 多选变量\nconst selectObjs = ref([]) as any\nconst multiple = ref(true)\n\nconst state: BasicTableProps = reactive<BasicTableProps>({\n  queryForm: {},\n  pageList: fetchList\n})\n\n//  table hook\nconst {\n  getDataList,\n  currentChangeHandle,\n  sizeChangeHandle,\n  sortChangeHandle,\n  downBlobFile,\n	tableStyle\n} = useTable(state)\n\n// 清空搜索条件\nconst resetQuery = () => {\n  // 清空搜索条件\n  queryRef.value?.resetFields()\n  // 清空多选\n  selectObjs.value = []\n  getDataList()\n}\n\n// 导出excel\nconst exportExcel = () => {\n  downBlobFile(\'/${moduleName}/${functionName}/export\', state.queryForm, \'${functionName}.xlsx\')\n}\n\n// 多选事件\nconst handleSelectionChange = (objs: { $pk.attrName: string }[]) => {\n  selectObjs.value = objs.map(({ $pk.attrName }) => $pk.attrName);\n  multiple.value = !objs.length;\n};\n\n// 删除操作\nconst handleDelete = async (ids: string[]) => {\n  try {\n    await useMessageBox().confirm(\'此操作将永久删除\');\n  } catch {\n    return;\n  }\n\n  try {\n    await delObjs(ids);\n    getDataList();\n    useMessage().success(\'删除成功\');\n  } catch (err: any) {\n    useMessage().error(err.msg);\n  }\n};\n</script>', '2023-02-23 01:19:35', '2023-07-21 22:12:38', '0', 1, '', 'admin');
INSERT INTO `gen_template` VALUES (2, '表单', '${frontendPath}/src/views/${moduleName}/${functionName}/form.vue', '表单不含i18n', '<template>\n    <el-dialog :title=\"form.${pk.attrName} ? \'编辑\' : \'新增\'\" v-model=\"visible\"\n      :close-on-click-modal=\"false\" draggable>\n      <el-form ref=\"dataFormRef\" :model=\"form\" :rules=\"dataRules\" formDialogRef label-width=\"90px\" v-loading=\"loading\">\n       <el-row :gutter=\"24\">\n\n#foreach($field in $formList)\n#if($field.attrName != ${pk.attrName})\n#if($formLayout == 1)\n    <el-col :span=\"24\" class=\"mb20\">\n#elseif($formLayout == 2)\n    <el-col :span=\"12\" class=\"mb20\">\n#end\n\n#if($field.formType == \'text\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n        <el-input v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\"/>\n      </el-form-item>\n      </el-col>\n#elseif($field.formType == \'textarea\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n        <el-input type=\"textarea\" v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\"/>\n      </el-form-item>\n      </el-col>\n#elseif($field.formType == \'select\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n          <el-select v-model=\"form.${field.attrName}\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\">\n     #if($field.fieldDict)\n            <el-option :value=\"item.value\" :label=\"item.label\" v-for=\"(item, index) in ${field.fieldDict}\" :key=\"index\"></el-option>\n       #end\n     #if(!$field.fieldDict)\n            <el-option label=\"请选择\">0</el-option>\n       #end\n          </el-select>\n        </el-form-item>\n      </el-col>\n#elseif($field.formType == \'radio\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-radio-group v-model=\"form.${field.attrName}\">\n     #if($field.fieldDict)\n             <el-radio :label=\"item.value\" v-for=\"(item, index) in ${field.fieldDict}\" border :key=\"index\">{{ item.label }}\n            </el-radio>\n       #else\n           <el-radio label=\"${field.fieldComment}\" border>${field.fieldComment}</el-radio>\n       #end\n            </el-radio-group>\n        </el-form-item>\n      </el-col>\n#elseif($field.formType == \'checkbox\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-checkbox-group v-model=\"form.${field.attrName}\">\n     #if($field.fieldDict)\n                <el-checkbox :label=\"item.value\" :name=\"item.label\" v-for=\"(item, index) in ${field.fieldDict}\" :key=\"index\"></el-checkbox>\n       #end\n     #if(!$field.fieldDict)\n                <el-checkbox label=\"启用\" name=\"type\"></el-checkbox>\n                <el-checkbox label=\"禁用\" name=\"type\"></el-checkbox>\n       #end\n            </<el-checkbox-group>\n        </el-form-item>\n      </el-col>\n#elseif($field.formType == \'date\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n      <el-date-picker type=\"date\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" v-model=\"form.${field.attrName}\" :value-format=\"dateStr\"></el-date-picker>\n      </el-form-item>\n      </el-col>\n#elseif($field.formType == \'datetime\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-date-picker type=\"datetime\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" v-model=\"form.${field.attrName}\" :value-format=\"dateTimeStr\"></el-date-picker>\n      </el-form-item>\n      </el-col>\n\n#elseif($field.formType == \'number\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n        <el-input-number :min=\"1\" :max=\"1000\" v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\"></el-input-number>\n      </el-form-item>\n    </el-col>\n#elseif($field.formType == \'upload-file\')\n  <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n    <upload-file v-model:imageUrl=\"form.${field.attrName}\"></upload-file>\n  </el-form-item>\n  </el-col>\n#elseif($field.formType == \'upload-img\')\n  <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n    <upload-img v-model=\"form.${field.attrName}\"></upload-img>\n  </el-form-item>\n  </el-col>\n#elseif($field.formType == \'editor\')\n  <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n    <editor v-if=\"visible\" v-model:get-html=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\"></editor>\n  </el-form-item>\n  </el-col>\n#end\n\n#if(!$field.formType)\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${column.attrName}\">\n        <el-input v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\"/>\n      </el-form-item>\n    </el-col>\n#end\n#end\n#end\n			</el-row>\n      </el-form>\n      <template #footer>\n        <span class=\"dialog-footer\">\n          <el-button @click=\"visible = false\">取消</el-button>\n          <el-button type=\"primary\" @click=\"onSubmit\" :disabled=\"loading\">确认</el-button>\n        </span>\n      </template>\n    </el-dialog>\n</template>\n\n<script setup lang=\"ts\" name=\"${ClassName}Dialog\">\nimport { useDict } from \'/@/hooks/dict\';\nimport { useMessage } from \"/@/hooks/message\";\nimport { getObj, addObj, putObj } from \'/@/api/${moduleName}/${functionName}\'\nimport { rule } from \'/@/utils/validate\';\nconst emit = defineEmits([\'refresh\']);\n\n// 定义变量内容\nconst dataFormRef = ref();\nconst visible = ref(false)\nconst loading = ref(false)\n// 定义字典\n#set($fieldDict=[])\n#foreach($field in $gridList)\n	#if($field.fieldDict)\n		#set($void=$fieldDict.add($field.fieldDict))\n	#end\n#end\n#if($fieldDict)\nconst { $dict.format($fieldDict) } = useDict($dict.quotation($fieldDict))\n#end\n\n// 提交表单数据\nconst form = reactive({\n#if(!$formList.contains(${pk.attrName}))\n		${pk.attrName}:\'\',\n#end\n#foreach($field in $formList)\n#if($field.formType == \'number\')\n		${field.attrName}: 0,\n#else\n	  ${field.attrName}: \'\',\n#end\n#end\n});\n\n// 定义校验规则\nconst dataRules = ref({\n#foreach($field in $formList)\n#if($field.formRequired && $field.formValidator)\n    ${field.attrName}: [{required: true, message: \'${field.fieldComment}不能为空\', trigger: \'blur\'}, { validator: rule.${field.formValidator}, trigger: \'blur\' }],\n#elseif($field.formRequired)\n        ${field.attrName}: [{required: true, message: \'${field.fieldComment}不能为空\', trigger: \'blur\'}],\n#elseif($field.formValidator)\n        ${field.attrName}: [{ validator: rule.${field.formValidator}, trigger: \'blur\' }],\n#end\n#end\n})\n\n// 打开弹窗\nconst openDialog = (id: string) => {\n  visible.value = true\n  form.${pk.attrName} = \'\'\n\n  // 重置表单数据\n	nextTick(() => {\n		dataFormRef.value?.resetFields();\n	});\n\n  // 获取${className}信息\n  if (id) {\n    form.${pk.attrName} = id\n    get${className}Data(id)\n  }\n};\n\n// 提交\nconst onSubmit = async () => {\n	const valid = await dataFormRef.value.validate().catch(() => {});\n	if (!valid) return false;\n\n	try {\n    loading.value = true;\n		form.${pk.attrName} ? await putObj(form) : await addObj(form);\n		useMessage().success(form.${pk.attrName} ? \'修改成功\' : \'添加成功\');\n		visible.value = false;\n		emit(\'refresh\');\n	} catch (err: any) {\n		useMessage().error(err.msg);\n	} finally {\n    loading.value = false;\n  }\n};\n\n\n// 初始化表单数据\nconst get${className}Data = (id: string) => {\n  // 获取数据\n  loading.value = true\n  getObj(id).then((res: any) => {\n    Object.assign(form, res.data)\n  }).finally(() => {\n    loading.value = false\n  })\n};\n\n// 暴露变量\ndefineExpose({\n  openDialog\n});\n</script>', '2023-02-23 01:19:48', '2023-07-21 22:12:41', '0', 1, '', 'admin');
INSERT INTO `gen_template` VALUES (3, 'Controller', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java', '后台Controller', 'package ${package}.${moduleName}.controller;\n\n#if($queryList)\nimport cn.hutool.core.util.StrUtil;\n#end\nimport cn.hutool.core.collection.CollUtil;\nimport com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\nimport com.baomidou.mybatisplus.core.toolkit.Wrappers;\nimport com.baomidou.mybatisplus.extension.plugins.pagination.Page;\nimport com.pig4cloud.pig.common.core.util.R;\nimport com.pig4cloud.pig.common.log.annotation.SysLog;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport ${package}.${moduleName}.service.${ClassName}Service;\nimport org.springframework.security.access.prepost.PreAuthorize;\nimport com.pig4cloud.plugin.excel.annotation.ResponseExcel;\nimport io.swagger.v3.oas.annotations.security.SecurityRequirement;\nimport org.springdoc.core.annotations.ParameterObject;\nimport org.springframework.http.HttpHeaders;\nimport io.swagger.v3.oas.annotations.tags.Tag;\nimport io.swagger.v3.oas.annotations.Operation;\nimport lombok.RequiredArgsConstructor;\nimport org.springframework.web.bind.annotation.*;\n\nimport java.util.List;\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@RestController\n@RequiredArgsConstructor\n@RequestMapping(\"/${functionName}\" )\n@Tag(description = \"${functionName}\" , name = \"${tableComment}管理\" )\n@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)\npublic class ${ClassName}Controller {\n\n    private final  ${ClassName}Service ${className}Service;\n\n    /**\n     * 分页查询\n     * @param page 分页对象\n     * @param ${className} ${tableComment}\n     * @return\n     */\n    @Operation(summary = \"分页查询\" , description = \"分页查询\" )\n    @GetMapping(\"/page\" )\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_view\')\" )\n    public R get${ClassName}Page(@ParameterObject Page page,@ParameterObject ${ClassName}Entity ${className}) {\n        LambdaQueryWrapper<${ClassName}Entity> wrapper = Wrappers.lambdaQuery();\n#foreach ($field in $queryList)\n#set($getAttrName=$str.getProperty($field.attrName))\n#set($var=\"${className}.$getAttrName()\")\n#if($field.queryType == \'=\')\n		wrapper.eq(StrUtil.isNotBlank($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'like\' )\n		wrapper.like(StrUtil.isNotBlank($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'!-\' )\n		wrapper.ne(StrUtil.isNotBlank($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'>\' )\n		wrapper.gt(StrUtil.isNotBlank($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'<\' )\n		wrapper.lt(StrUtil.isNotBlank($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'>=\' )\n		wrapper.ge(StrUtil.isNotBlank($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'<=\' )\n		wrapper.le(StrUtil.isNotBlank($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'left like\' )\n		wrapper.likeLeft(StrUtil.isNotBlank($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'right like\' )\n		wrapper.likeRight(StrUtil.isNotBlank($var),${ClassName}Entity::$getAttrName,$var);\n#end\n#end\n        return R.ok(${className}Service.page(page, wrapper));\n    }\n\n\n    /**\n     * 通过id查询${tableComment}\n     * @param ${pk.attrName} id\n     * @return R\n     */\n    @Operation(summary = \"通过id查询\" , description = \"通过id查询\" )\n    @GetMapping(\"/{${pk.attrName}}\" )\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_view\')\" )\n    public R getById(@PathVariable(\"${pk.attrName}\" ) ${pk.attrType} ${pk.attrName}) {\n        return R.ok(${className}Service.getById(${pk.attrName}));\n    }\n\n    /**\n     * 新增${tableComment}\n     * @param ${className} ${tableComment}\n     * @return R\n     */\n    @Operation(summary = \"新增${tableComment}\" , description = \"新增${tableComment}\" )\n    @SysLog(\"新增${tableComment}\" )\n    @PostMapping\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_add\')\" )\n    public R save(@RequestBody ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.save(${className}));\n    }\n\n    /**\n     * 修改${tableComment}\n     * @param ${className} ${tableComment}\n     * @return R\n     */\n    @Operation(summary = \"修改${tableComment}\" , description = \"修改${tableComment}\" )\n    @SysLog(\"修改${tableComment}\" )\n    @PutMapping\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_edit\')\" )\n    public R updateById(@RequestBody ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.updateById(${className}));\n    }\n\n    /**\n     * 通过id删除${tableComment}\n     * @param ids ${pk.attrName}列表\n     * @return R\n     */\n    @Operation(summary = \"通过id删除${tableComment}\" , description = \"通过id删除${tableComment}\" )\n    @SysLog(\"通过id删除${tableComment}\" )\n    @DeleteMapping\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_del\')\" )\n    public R removeById(@RequestBody ${pk.attrType}[] ids) {\n        return R.ok(${className}Service.removeBatchByIds(CollUtil.toList(ids)));\n    }\n\n\n    /**\n     * 导出excel 表格\n     * @param ${className} 查询条件\n     * @return excel 文件流\n     */\n    @ResponseExcel\n    @GetMapping(\"/export\")\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_export\')\" )\n    public List<${ClassName}Entity> export(${ClassName}Entity ${className}) {\n        return ${className}Service.list(Wrappers.query(${className}));\n    }\n}', '2023-02-23 01:16:17', '2023-07-21 22:02:51', '0', 1, ' ', ' ');
INSERT INTO `gen_template` VALUES (4, 'Service', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/${ClassName}Service.java', 'Service', 'package ${package}.${moduleName}.service;\n\n#if($ChildClassName)\nimport com.github.yulichang.extension.mapping.base.MPJDeepService;\nimport ${package}.${moduleName}.entity.${ChildClassName}Entity;\n#else\nimport com.baomidou.mybatisplus.extension.service.IService;\n#end\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\n\n#if($ChildClassName)\npublic interface ${ClassName}Service extends MPJDeepService<${ClassName}Entity> {\n    Boolean saveDeep(${ClassName}Entity ${className});\n\n    Boolean updateDeep(${ClassName}Entity ${className});\n\n    Boolean removeDeep(Long[] ids);\n\n    Boolean removeChild(Long[] ids);\n#else\npublic interface ${ClassName}Service extends IService<${ClassName}Entity> {\n#end\n\n}', '2023-02-23 01:16:53', '2023-06-04 10:35:25', '0', 1, ' ', ' ');
INSERT INTO `gen_template` VALUES (5, 'ServiceImpl', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/impl/${ClassName}ServiceImpl.java', 'ServiceImpl', 'package ${package}.${moduleName}.service.impl;\n\nimport com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport ${package}.${moduleName}.mapper.${ClassName}Mapper;\nimport ${package}.${moduleName}.service.${ClassName}Service;\nimport org.springframework.stereotype.Service;\n#if($ChildClassName)\nimport cn.hutool.core.collection.CollUtil;\nimport com.baomidou.mybatisplus.core.toolkit.Wrappers;\nimport ${package}.${moduleName}.entity.${ChildClassName}Entity;\nimport ${package}.${moduleName}.mapper.${ChildClassName}Mapper;\nimport org.springframework.transaction.annotation.Transactional;\nimport lombok.RequiredArgsConstructor;\nimport java.util.Objects;\n#end\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Service\n#if($ChildClassName)\n@RequiredArgsConstructor\n#end\npublic class ${ClassName}ServiceImpl extends ServiceImpl<${ClassName}Mapper, ${ClassName}Entity> implements ${ClassName}Service {\n#if($ChildClassName)\n  private final ${ChildClassName}Mapper ${childClassName}Mapper;\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean saveDeep(${ClassName}Entity ${className}) {\n        baseMapper.insert(${className});\n        for (${ChildClassName}Entity  ${childClassName} : ${className}.get${ChildClassName}List()) {\n            ${childClassName}.$str.setProperty($childField)(${className}.$str.getProperty($mainField)());\n            ${childClassName}Mapper.insert( ${childClassName});\n        }\n\n        return Boolean.TRUE;\n    }\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean updateDeep(${ClassName}Entity ${className}) {\n        baseMapper.updateById(${className});\n        for (${ChildClassName}Entity  ${childClassName} : ${className}.get${ChildClassName}List()) {\n#foreach ($field in $childFieldList)\n#if($field.primaryPk)\n#set($getChildPkName=$str.getProperty($field.attrName))\n#end\n#end\n            if (Objects.isNull(${childClassName}.$getChildPkName())) {\n                ${childClassName}.$str.setProperty($childField)(${className}.getId());\n                ${childClassName}Mapper.insert(${childClassName});\n            } else {\n                ${childClassName}Mapper.updateById(${childClassName});\n            }\n        }\n        return Boolean.TRUE;\n    }\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean removeDeep(Long[] ids) {\n        baseMapper.deleteBatchIds(CollUtil.toList(ids));\n        ${childClassName}Mapper.delete(Wrappers.<${ChildClassName}Entity>lambdaQuery().in(${ChildClassName}Entity::$str.getProperty($childField), ids));\n        return Boolean.TRUE;\n    }\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean removeChild(Long[] ids) {\n        ${childClassName}Mapper.deleteBatchIds(CollUtil.toList(ids));\n        return Boolean.TRUE;\n    }\n#end\n}', '2023-02-23 01:17:36', '2023-06-04 10:35:21', '0', 1, ' ', ' ');
INSERT INTO `gen_template` VALUES (6, '实体', '${backendPath}/src/main/java/${packagePath}/${moduleName}/entity/${ClassName}Entity.java', 'Entity', 'package ${package}.${moduleName}.entity;\n\nimport com.baomidou.mybatisplus.annotation.*;\nimport com.baomidou.mybatisplus.extension.activerecord.Model;\nimport io.swagger.v3.oas.annotations.media.Schema;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\n#foreach($import in $importList)\nimport $import;\n#end\n#if($ChildClassName)\nimport com.alibaba.excel.annotation.ExcelIgnore;\nimport com.github.yulichang.annotation.EntityMapping;\nimport java.util.List;\n#end\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Data\n@TableName(\"${tableName}\")\n@EqualsAndHashCode(callSuper = true)\n@Schema(description = \"${tableComment}\")\npublic class ${ClassName}Entity extends Model<${ClassName}Entity> {\n\n#foreach ($field in $fieldList)\n#if(${field.fieldComment})#set($comment=${field.fieldComment})#else #set($comment=${field.attrName})#end\n\n	/**\n	* $comment\n	*/\n#if($field.primaryPk)\n    @TableId(type = IdType.ASSIGN_ID)\n#end\n#if($field.autoFill == \'INSERT\')\n	@TableField(fill = FieldFill.INSERT)\n#elseif($field.autoFill == \'INSERT_UPDATE\')\n	@TableField(fill = FieldFill.INSERT_UPDATE)\n#elseif($field.autoFill == \'UPDATE\')\n	@TableField(fill = FieldFill.UPDATE)\n#end\n#if($field.fieldName == \'del_flag\')\n    @TableLogic\n		@TableField(fill = FieldFill.INSERT)\n#end\n    @Schema(description=\"$comment\"#if($field.hidden),hidden=$field.hidden#end)\n    private $field.attrType $field.attrName;\n#end\n#if($ChildClassName)\n    @ExcelIgnore\n    @TableField(exist = false)\n    @EntityMapping(thisField = \"$mainField\", joinField = \"$childField\")\n    private List<${ChildClassName}Entity> ${childClassName}List;\n#end\n}', '2023-02-23 01:17:53', '2023-06-04 10:45:15', '0', 1, ' ', ' ');
INSERT INTO `gen_template` VALUES (7, 'Mapper', '${backendPath}/src/main/java/${packagePath}/${moduleName}/mapper/${ClassName}Mapper.java', 'Mapper', 'package ${package}.${moduleName}.mapper;\n\nimport com.baomidou.mybatisplus.core.mapper.BaseMapper;\n#if($ChildClassName)\nimport ${package}.${moduleName}.entity.${ChildClassName}Entity;\n#else\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\n#end\nimport org.apache.ibatis.annotations.Mapper;\n\n@Mapper\n#if($ChildClassName)\npublic interface ${ChildClassName}Mapper extends BaseMapper<${ChildClassName}Entity> {\n#else\npublic interface ${ClassName}Mapper extends BaseMapper<${ClassName}Entity> {\n#end\n\n}', '2023-02-23 01:18:18', '2023-07-21 22:01:14', '0', 1, ' ', ' ');
INSERT INTO `gen_template` VALUES (8, 'Mapper.xml', '${backendPath}/src/main/resources/mapper/${ClassName}Mapper.xml', 'Mapper.xml', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n<mapper namespace=\"${package}.${moduleName}.mapper.${ClassName}Mapper\">\n\n  <resultMap id=\"${className}Map\" type=\"${package}.${moduleName}.entity.${ClassName}Entity\">\n#foreach ($field in $fieldList)\n	  #if($field.primaryPk)\n        <id property=\"$field.attrName\" column=\"$field.fieldName\"/>\n      #end\n      #if(!$field.primaryPk)\n        <result property=\"$field.attrName\" column=\"$field.fieldName\"/>\n      #end\n#end\n  </resultMap>\n</mapper>', '2023-02-23 01:18:35', '2023-06-04 10:34:56', '0', 1, ' ', ' ');
INSERT INTO `gen_template` VALUES (9, '权限菜单', '${backendPath}/menu/${functionName}_menu.sql', 'menu.sql', '-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 , 默认租户 1\n#set($menuId=${dateTool.getSystemTime()})\n\n    -- 菜单SQL\n    insert into `sys_menu` ( `menu_id`,`parent_id`, `path`, `permission`, `menu_type`, `icon`, `del_flag`, `create_time`, `sort_order`, `update_time`, `name`)\n    values (${menuId}, \'-1\', \'/${moduleName}/${functionName}/index\', \'\', \'0\', \'icon-bangzhushouji\', \'0\', null , \'8\', null , \'${tableComment}管理\');\n\n-- 按钮父菜单ID\n    set @parentId = @@identity;\n\n-- 菜单对应按钮SQL\n    insert into `sys_menu` ( `menu_id`,`parent_id`, `permission`, `menu_type`, `path`, `icon`, `del_flag`, `create_time`, `sort_order`, `update_time`, `name`)\n    values (${math.add($menuId,1)},${menuId}, \'${moduleName}_${functionName}_view\', \'1\', null, \'1\',  \'0\', null, \'0\', null, \'${tableComment}查看\');\n\n    insert into `sys_menu` ( `menu_id`,`parent_id`, `permission`, `menu_type`, `path`, `icon`, `del_flag`, `create_time`, `sort_order`, `update_time`, `name`)\n    values (${math.add($menuId,2)},${menuId}, \'${moduleName}_${functionName}_add\', \'1\', null, \'1\',  \'0\', null, \'1\', null, \'${tableComment}新增\');\n\n    insert into `sys_menu` (`menu_id`, `parent_id`, `permission`, `menu_type`, `path`, `icon`,  `del_flag`, `create_time`, `sort_order`, `update_time`, `name`)\n    values (${math.add($menuId,3)},${menuId}, \'${moduleName}_${functionName}_edit\', \'1\', null, \'1\',  \'0\', null, \'2\', null, \'${tableComment}修改\');\n\n    insert into `sys_menu` (`menu_id`, `parent_id`, `permission`, `menu_type`, `path`, `icon`, `del_flag`, `create_time`, `sort_order`, `update_time`, `name`)\n    values (${math.add($menuId,4)},${menuId}, \'${moduleName}_${functionName}_del\', \'1\', null, \'1\',  \'0\', null, \'3\', null, \'${tableComment}删除\');\n\n    insert into `sys_menu` ( `menu_id`,`parent_id`, `permission`, `menu_type`, `path`, `icon`, `del_flag`, `create_time`, `sort_order`, `update_time`, `name`)\n    values (${math.add($menuId,5)},${menuId}, \'${moduleName}_${functionName}_export\', \'1\', null, \'1\',  \'0\', null, \'3\', null, \'导入导出\');', '2023-02-23 01:19:08', '2023-07-21 22:08:16', '0', 1, ' ', ' ');
INSERT INTO `gen_template` VALUES (10, 'api.ts', '${frontendPath}/src/api/${moduleName}/${functionName}.ts', 'api.ts', 'import request from \"/@/utils/request\"\n\nexport function fetchList(query?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}/page\',\n    method: \'get\',\n    params: query\n  })\n}\n\nexport function addObj(obj?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'post\',\n    data: obj\n  })\n}\n\nexport function getObj(id?: string) {\n  return request({\n    url: \'/${moduleName}/${functionName}/\' + id,\n    method: \'get\'\n  })\n}\n\nexport function delObjs(ids?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'delete\',\n    data: ids\n  })\n}\n\nexport function putObj(obj?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'put\',\n    data: obj\n  })\n}\n\n#if($ChildClassName)\nexport function delChildObj(ids?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}/child\',\n    method: \'delete\',\n    data: ids\n  })\n}\n#end', '2023-02-23 01:19:23', '2023-06-04 10:34:17', '0', 1, ' ', ' ');
COMMIT;

-- ----------------------------
-- Table structure for gen_template_group
-- ----------------------------
DROP TABLE IF EXISTS `gen_template_group`;
CREATE TABLE `gen_template_group` (
  `group_id` bigint NOT NULL COMMENT '分组id',
  `template_id` bigint NOT NULL COMMENT '模板id',
  PRIMARY KEY (`group_id`,`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='模板分组关联表';

-- ----------------------------
-- Records of gen_template_group
-- ----------------------------
BEGIN;
INSERT INTO `gen_template_group` VALUES (1, 1);
INSERT INTO `gen_template_group` VALUES (1, 2);
INSERT INTO `gen_template_group` VALUES (1, 3);
INSERT INTO `gen_template_group` VALUES (1, 4);
INSERT INTO `gen_template_group` VALUES (1, 5);
INSERT INTO `gen_template_group` VALUES (1, 6);
INSERT INTO `gen_template_group` VALUES (1, 7);
INSERT INTO `gen_template_group` VALUES (1, 8);
INSERT INTO `gen_template_group` VALUES (1, 9);
INSERT INTO `gen_template_group` VALUES (1, 10);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
