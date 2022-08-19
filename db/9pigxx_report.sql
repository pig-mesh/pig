USE pigxx_report;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for access_authority
-- ----------------------------
DROP TABLE IF EXISTS `access_authority`;
CREATE TABLE `access_authority` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `parent_target` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '父ID',
                                    `target` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '菜单代码',
                                    `target_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '菜单名称',
                                    `action` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '按钮代码',
                                    `action_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '按钮名称',
                                    `sort` int DEFAULT NULL,
                                    `enable_flag` int NOT NULL DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                    `delete_flag` int DEFAULT '0' COMMENT ' 0--未删除 1--已删除 DIC_NAME=DEL_FLAG',
                                    `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '创建人',
                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '修改人',
                                    `update_time` datetime NOT NULL COMMENT '修改时间',
                                    `version` tinyint DEFAULT NULL,
                                    PRIMARY KEY (`id`) USING BTREE,
                                    UNIQUE KEY `TA_uniq_index` (`target`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='运营权限表';

-- ----------------------------
-- Records of access_authority
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for access_role
-- ----------------------------
DROP TABLE IF EXISTS `access_role`;
CREATE TABLE `access_role` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `role_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色编码',
                               `role_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色名称',
                               `enable_flag` int NOT NULL DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                               `delete_flag` int DEFAULT '0' COMMENT ' 0--未删除 1--已删除 DIC_NAME=DEL_FLAG',
                               `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '创建人',
                               `create_time` datetime NOT NULL COMMENT '创建时间',
                               `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '更新人',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               `version` tinyint DEFAULT NULL,
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='运营角色表';

-- ----------------------------
-- Records of access_role
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for access_role_authority
-- ----------------------------
DROP TABLE IF EXISTS `access_role_authority`;
CREATE TABLE `access_role_authority` (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                         `role_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色名称',
                                         `target` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '权限目标',
                                         `action` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC COMMENT='运营角色权限表';

-- ----------------------------
-- Records of access_role_authority
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for access_user
-- ----------------------------
DROP TABLE IF EXISTS `access_user`;
CREATE TABLE `access_user` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                               `login_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT ' 登录名',
                               `real_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '真实用户',
                               `password` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
                               `phone` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '手机号码',
                               `email` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '用户邮箱',
                               `remark` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
                               `last_login_time` datetime DEFAULT NULL COMMENT '最后一次登陆时间',
                               `last_login_ip` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '最后一次登录IP',
                               `enable_flag` int NOT NULL DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                               `delete_flag` int NOT NULL DEFAULT '0' COMMENT ' 0--未删除 1--已删除 DIC_NAME=DEL_FLAG',
                               `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '更新人',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               `version` tinyint DEFAULT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `IDX1` (`login_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='运营用户表';

-- ----------------------------
-- Records of access_user
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for access_user_role
-- ----------------------------
DROP TABLE IF EXISTS `access_user_role`;
CREATE TABLE `access_user_role` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
                                    `login_name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '权限目标',
                                    `role_code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '角色名称',
                                    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='运营角色权限表';

-- ----------------------------
-- Records of access_user_role
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gaea_dict
-- ----------------------------
DROP TABLE IF EXISTS `gaea_dict`;
CREATE TABLE `gaea_dict` (
                             `id` int NOT NULL AUTO_INCREMENT,
                             `dict_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典名称',
                             `dict_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典编码',
                             `remark` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
                             `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                             `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
                             `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新用户',
                             `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
                             `version` int DEFAULT NULL COMMENT '版本',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数组字典';

-- ----------------------------
-- Records of gaea_dict
-- ----------------------------
BEGIN;
INSERT INTO `gaea_dict` VALUES (4, '删除状态', 'DELETE_FLAG', NULL, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict` VALUES (6, '是否启用', 'ENABLE_FLAG', NULL, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict` VALUES (7, '文件状态', 'FILE_STATUS', NULL, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict` VALUES (8, '是否启用', 'FILTER_FLAG', NULL, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict` VALUES (9, '数据过滤类型', 'FILTER_TYPE', NULL, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict` VALUES (11, '指标类型', 'ITEM_TYPE', NULL, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict` VALUES (12, '国际化标识', 'LOCALE', 'ssssss', 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-27 22:20:00', 2);
INSERT INTO `gaea_dict` VALUES (35, '数据源类型', 'SOURCE_TYPE', '', 'admin', '2021-03-23 13:16:58', 'admin', '2021-04-13 16:43:30', 4);
INSERT INTO `gaea_dict` VALUES (36, '数据转换类型', 'TRANSFORM_TYPE', NULL, 'admin', '2021-03-23 13:24:28', 'admin', '2021-03-23 13:24:32', 1);
INSERT INTO `gaea_dict` VALUES (37, '是否必填', 'REQUIRED_FLAG', NULL, 'admin', '2021-03-23 13:25:50', 'admin', '2021-03-23 13:25:53', 1);
INSERT INTO `gaea_dict` VALUES (39, '行业类型', 'SYS_CATA_TYPE', '系统所属行业类型', 'admin', '2021-03-25 13:07:38', 'admin', '2021-03-25 13:07:38', 1);
INSERT INTO `gaea_dict` VALUES (40, '报表类型', 'REPORT_TYPE', '报表类型', 'admin', '2021-03-26 11:48:06', 'admin', '2021-03-26 11:48:11', NULL);
INSERT INTO `gaea_dict` VALUES (45, '菜单国际化', 'MENU_LANGUAGE', '菜单国际化配置', 'admin', '2021-04-06 16:33:33', 'admin', '2021-04-06 16:33:33', 1);
INSERT INTO `gaea_dict` VALUES (46, '报表分组', 'REPORT_GROUP', '报表分组', 'admin', '2021-04-07 09:36:40', 'admin', '2021-04-07 09:36:42', 1);
INSERT INTO `gaea_dict` VALUES (47, '报表组件类型', 'DASHBOARD_PANEL_TYPE', '报表组件类型', 'admin', '2021-04-12 10:42:50', 'admin', '2021-04-12 10:42:55', 1);
INSERT INTO `gaea_dict` VALUES (48, '图表属性', 'CHART_PROPERTIES', '报表属性', 'admin', '2021-04-29 10:28:15', 'admin', '2021-06-23 10:47:20', 3);
INSERT INTO `gaea_dict` VALUES (49, '分享有效期', 'SHARE_VAILD', '报表分享', 'admin', '2021-08-18 13:29:19', 'admin', '2021-08-18 13:29:24', 1);
INSERT INTO `gaea_dict` VALUES (50, '柱状图属性', 'BAR_PROPERTIES', '柱状图属性', 'admin', '2021-04-29 10:28:15', 'admin', '2021-06-23 10:47:20', 1);
INSERT INTO `gaea_dict` VALUES (51, '折线图属性', 'LINE_PROPERTIES', '折线图属性', 'admin', '2021-04-29 10:28:15', 'admin', '2021-06-23 10:47:20', 1);
INSERT INTO `gaea_dict` VALUES (52, '柱线图属性', 'BAR_LINE_PROPERTIES', '柱线图属性', 'admin', '2021-04-29 10:28:15', 'admin', '2021-06-23 10:47:20', 1);
INSERT INTO `gaea_dict` VALUES (53, '饼图属性', 'PIE_PROPERTIES', '饼图、漏斗图', 'admin', '2021-04-29 10:28:15', 'admin', '2021-06-23 10:47:20', 1);
INSERT INTO `gaea_dict` VALUES (54, '单文本图属性', 'TEXT_PROPERTIES', '仪表盘、百分比、文本框、滚动文本', 'admin', '2021-04-29 10:28:15', 'admin', '2021-06-23 10:47:20', 1);
INSERT INTO `gaea_dict` VALUES (55, '堆叠图属性', 'STACK_PROPERTIES', '堆叠图属性', 'admin', '2021-04-29 10:28:15', 'admin', '2021-06-23 10:47:20', 1);
INSERT INTO `gaea_dict` VALUES (56, '地图属性', 'MAP_PROPERTIES', '地图属性', 'admin', '2021-04-29 10:28:15', 'admin', '2021-06-23 10:47:20', 1);
INSERT INTO `gaea_dict` VALUES (57, '数据集类型', 'SET_TYPE', '数据集类型', 'admin', '2021-11-16 14:43:12', 'admin', '2021-11-16 14:43:12', 1);
INSERT INTO `gaea_dict` VALUES (58, 'XY坐标属性', 'COORD_PROPERTIES', 'XY坐标属性', 'admin', '2022-08-17 18:06:18', 'admin', '2022-08-17 18:06:18', 1);
INSERT INTO `gaea_dict` VALUES (59, '源端目标端属性', 'SOUTAR_PROPERTIES', '源端目标端属性', 'admin', '2022-08-17 18:06:18', 'admin', '2022-08-17 18:06:18', 1);
COMMIT;

-- ----------------------------
-- Table structure for gaea_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `gaea_dict_item`;
CREATE TABLE `gaea_dict_item` (
                                  `id` int NOT NULL AUTO_INCREMENT,
                                  `dict_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据字典编码',
                                  `item_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典项名称',
                                  `item_value` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典项值',
                                  `item_extend` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '字典扩展项',
                                  `enabled` int DEFAULT '1' COMMENT '1:启用 0:禁用',
                                  `locale` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '语言标识',
                                  `remark` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
                                  `sort` int DEFAULT NULL COMMENT '排序',
                                  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                                  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新用户',
                                  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
                                  `version` int DEFAULT NULL COMMENT '版本',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=325 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据字典项';

-- ----------------------------
-- Records of gaea_dict_item
-- ----------------------------
BEGIN;
INSERT INTO `gaea_dict_item` VALUES (1, 'AUDIT_FLAG', '待审核', 'waiting', NULL, 1, 'zh', '待审核', 1, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (2, 'AUDIT_FLAG', '审核中', 'ongoing', NULL, 1, 'zh', '审核中', 2, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (3, 'AUDIT_FLAG', '通过', 'approved', NULL, 1, 'zh', '通过', 3, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (4, 'AUDIT_FLAG', '拒绝', 'rejected', NULL, 1, 'zh', '拒绝', 4, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (5, 'DELETE_FLAG', '已删除', '1', NULL, 1, 'zh', '已删除', 5, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-31 12:04:26', 2);
INSERT INTO `gaea_dict_item` VALUES (6, 'DELETE_FLAG', '未删除', '0', NULL, 1, 'zh', '未删除', 6, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-31 12:04:49', 3);
INSERT INTO `gaea_dict_item` VALUES (7, 'ENABLE_FLAG', '禁用', '0', NULL, 1, 'zh', '已禁用', 2, 'admin', '2021-03-09 16:43:09', 'admin', '2021-04-08 14:41:13', 3);
INSERT INTO `gaea_dict_item` VALUES (8, 'ENABLE_FLAG', '启用', '1', NULL, 1, 'zh', '已启用', 1, 'admin', '2021-03-09 16:43:09', 'admin', '2021-04-08 14:41:18', 3);
INSERT INTO `gaea_dict_item` VALUES (9, 'SYSTEM_CODE', 'CTS电脑端', 'CTS-PC', NULL, 1, 'zh', 'CTS电脑端', 9, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (10, 'SYSTEM_CODE', 'CTS App端', 'CTS-APP', NULL, 1, 'zh', 'CTS App端', 10, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (11, 'FILTER_FLAG', '已禁用', '0', NULL, 1, 'zh', '已禁用', 7, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (12, 'FILTER_FLAG', '已启用', '1', NULL, 1, 'zh', '已启用', 8, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (13, 'FILTER_TYPE', 'js脚本', 'jsCalc', NULL, 1, 'zh', 'js脚本', 3, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (14, 'FILTER_TYPE', '新增字段', 'addField', NULL, 1, 'zh', '新增字段', 3, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (15, 'FILTER_TYPE', '替换字段', 'replaceField', NULL, 1, 'zh', '替换字段', 3, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (16, 'FILTER_TYPE', 'ip解析java处理', 'ipTransform', NULL, 1, 'zh', 'ip解析java处理', 3, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (17, 'FILTER_TYPE', 'vpn在线时长java处理', 'vpnTransform', NULL, 1, 'zh', 'vpn在线时长java处理', 3, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (18, 'SETTING_TYPE', '字符串', 'input', NULL, 1, 'zh', '字符串', 10, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (19, 'SETTING_TYPE', '数字', 'input-number', NULL, 1, 'zh', '数字', 10, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (20, 'SETTING_TYPE', '文本区域', 'textarea', NULL, 1, 'zh', '文本区域', 10, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (21, 'SETTING_TYPE', '数据字典', 'code-select', NULL, 1, 'zh', '数据字典', 10, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (22, 'SETTING_TYPE', '下拉列表', 'select', NULL, 1, 'zh', '下拉列表', 10, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (23, 'SETTING_TYPE', '单选按钮', 'radio-group', NULL, 1, 'zh', '单选按钮', 10, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (24, 'SETTING_TYPE', '多选按钮', 'checkbox-group', NULL, 1, 'zh', '多选按钮', 10, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (25, 'SETTING_TYPE', '自定义表单', 'custom-form', NULL, 1, 'zh', '自定义表单', 10, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (86, 'FILE_STATUS', '生成中', 'creating', NULL, 1, 'zh', '生成中', 3, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (87, 'FILE_STATUS', '生成成功', 'success', NULL, 1, 'zh', '生成成功', 3, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (88, 'FILE_STATUS', '生成失败', 'failed', NULL, 1, 'zh', '生成失败', 3, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (89, 'LOCALE', '中文', 'zh', NULL, 1, 'zh', '中文', NULL, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (90, 'LOCALE', '英文', 'en', NULL, 1, 'zh', '英文', NULL, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (91, 'LOCALE', 'Chinese', 'zh', NULL, 1, 'en', 'Chinese', NULL, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (92, 'LOCALE', 'English', 'en', NULL, 1, 'en', 'English', 1, 'admin', '2021-03-09 16:43:09', 'admin', '2021-03-09 16:43:09', 1);
INSERT INTO `gaea_dict_item` VALUES (140, 'REQUIRED_FLAG', '必填', '1', NULL, 1, 'zh', NULL, 2, 'admin', '2021-03-23 10:54:08', 'admin', '2021-03-23 10:54:08', 1);
INSERT INTO `gaea_dict_item` VALUES (141, 'REQUIRED_FLAG', '非必填', '0', NULL, 1, 'zh', NULL, 2, 'admin', '2021-03-23 10:54:08', 'admin', '2021-03-23 10:54:08', 1);
INSERT INTO `gaea_dict_item` VALUES (150, 'TRANSFORM_TYPE', 'js脚本', 'js', NULL, 1, 'zh', NULL, 2, 'admin', '2021-03-23 10:54:08', 'admin', '2021-03-23 10:54:08', 1);
INSERT INTO `gaea_dict_item` VALUES (151, 'TRANSFORM_TYPE', 'java脚本', 'javaBean', NULL, 1, 'zh', NULL, 2, 'admin', '2021-03-23 10:54:08', 'admin', '2021-03-23 10:54:08', 1);
INSERT INTO `gaea_dict_item` VALUES (152, 'TRANSFORM_TYPE', '字典翻译', 'dict', '{\"dict1\": {\"1\": \"男\",\"0\": \"女\"},\"dict2\": {\"mysql\": \"mysql\",\"sqlserver\": \"sqlserver\"}}', 1, 'zh', NULL, 2, 'admin', '2021-03-23 10:54:08', 'admin', '2021-03-23 10:54:08', 1);
INSERT INTO `gaea_dict_item` VALUES (153, 'SYS_CATA_TYPE', '行业01', '1001', NULL, 1, 'zh', NULL, 1, 'admin', '2021-03-25 13:10:32', 'admin', '2021-03-25 13:10:32', 1);
INSERT INTO `gaea_dict_item` VALUES (154, 'SYS_CATA_TYPE', '行业02', '1002', NULL, 1, 'zh', NULL, 2, 'admin', '2021-03-25 13:11:01', 'admin', '2021-03-25 13:11:01', 1);
INSERT INTO `gaea_dict_item` VALUES (159, 'REPORT_TYPE', '大屏报表', 'report_screen', NULL, 1, 'zh', NULL, NULL, 'admin', '2021-03-26 11:50:25', 'admin', '2021-03-26 11:50:21', NULL);
INSERT INTO `gaea_dict_item` VALUES (230, 'REPORT_GROUP', '分组1', 'group_1', NULL, 1, 'zh', NULL, 1, 'admin', '2021-04-07 09:39:45', 'admin', '2021-04-13 16:44:31', 2);
INSERT INTO `gaea_dict_item` VALUES (231, 'REPORT_GROUP', '分组2', 'group_2', NULL, 1, 'zh', NULL, 1, 'admin', '2021-04-07 09:40:55', 'admin', '2021-04-07 09:40:52', 1);
INSERT INTO `gaea_dict_item` VALUES (232, 'INF_STATUS', '审核通过', '5', NULL, 1, 'zh', NULL, 25, 'admin', '2021-04-07 11:02:42', 'admin', '2021-04-07 15:01:35', 3);
INSERT INTO `gaea_dict_item` VALUES (233, 'RULE_PARAM_TYPE', 'int', 'int', NULL, 1, 'zh', NULL, 3, 'zouya', '2021-04-09 09:43:22', 'zouya', '2021-04-09 09:43:22', 1);
INSERT INTO `gaea_dict_item` VALUES (234, 'PUSH_TYPE', 'APP端', 'appsp', NULL, 1, 'zh', 'App端推送采用推送平台Appsp', 4, 'admin', '2021-04-09 10:13:33', 'admin', '2021-04-09 10:14:11', 3);
INSERT INTO `gaea_dict_item` VALUES (240, 'DASHBOARD_PANEL_TYPE', '文本', 'text', NULL, 1, 'zh', 'App端推送采用推送平台Appsp', 4, 'admin', '2021-04-09 10:13:33', 'admin', '2021-04-09 10:14:11', 3);
INSERT INTO `gaea_dict_item` VALUES (241, 'DASHBOARD_PANEL_TYPE', '滚动文本', '\r\nscrollingText', NULL, 1, 'zh', 'App端推送采用推送平台Appsp', 4, 'admin', '2021-04-09 10:13:33', 'admin', '2021-04-09 10:14:11', 3);
INSERT INTO `gaea_dict_item` VALUES (242, 'DASHBOARD_PANEL_TYPE', '超链接', 'hyperlinks', NULL, 1, 'zh', 'App端推送采用推送平台Appsp', 4, 'admin', '2021-04-09 10:13:33', 'admin', '2021-04-09 10:14:11', 3);
INSERT INTO `gaea_dict_item` VALUES (243, 'DASHBOARD_PANEL_TYPE', '当前时间', 'currentTime', NULL, 1, 'zh', 'App端推送采用推送平台Appsp', 4, 'admin', '2021-04-09 10:13:33', 'admin', '2021-04-09 10:14:11', 3);
INSERT INTO `gaea_dict_item` VALUES (244, 'DASHBOARD_PANEL_TYPE', '图片', 'picture', NULL, 1, 'zh', 'App端推送采用推送平台Appsp', 4, 'admin', '2021-04-09 10:13:33', 'admin', '2021-04-09 10:14:11', 3);
INSERT INTO `gaea_dict_item` VALUES (245, 'DASHBOARD_PANEL_TYPE', '轮播图片', 'rotatePictures', NULL, 1, 'zh', 'App端推送采用推送平台Appsp', 4, 'admin', '2021-04-09 10:13:33', 'admin', '2021-04-09 10:14:11', 3);
INSERT INTO `gaea_dict_item` VALUES (246, 'MENU_LANGUAGE', '图标库', 'SvgDemo', '', 1, 'zh', '', 1, 'admin', '2021-04-06 16:34:10', 'admin', '2021-04-06 16:34:10', 1);
INSERT INTO `gaea_dict_item` VALUES (247, 'MENU_LANGUAGE', '代码生成', 'Generator', NULL, 1, 'zh', '', 1, 'admin', '2021-04-06 16:34:10', 'admin', '2021-04-06 16:34:10', 1);
INSERT INTO `gaea_dict_item` VALUES (248, 'MENU_LANGUAGE', '项目列表', 'Project', '', 1, 'zh', '', 1, 'admin', '2021-04-06 16:34:10', 'admin', '2021-04-06 16:34:10', 1);
INSERT INTO `gaea_dict_item` VALUES (249, 'MENU_LANGUAGE', '生成示例-单表', 'AlipayConfig', '', 1, 'zh', '', 1, 'admin', '2021-04-06 16:34:10', 'admin', '2021-04-06 16:34:10', 1);
INSERT INTO `gaea_dict_item` VALUES (250, 'MENU_LANGUAGE', '设备信息-主表', 'DeviceInfo', '', 1, 'zh', '', 1, 'admin', '2021-04-06 16:34:10', 'admin', '2021-04-06 16:34:10', 1);
INSERT INTO `gaea_dict_item` VALUES (251, 'MENU_LANGUAGE', '设备类型-子表', 'DeviceModel', '', 1, 'zh', '', 1, 'admin', '2021-04-06 16:34:10', 'admin', '2021-04-06 16:34:10', 1);
INSERT INTO `gaea_dict_item` VALUES (252, 'MENU_LANGUAGE', '设备日志-子表', 'DeviceLogDetail', '', 1, 'zh', '', 1, 'admin', '2021-04-06 16:34:10', 'admin', '2021-04-06 16:34:10', 1);
INSERT INTO `gaea_dict_item` VALUES (253, 'MENU_LANGUAGE', 'API文档', 'API', '', 1, 'zh', '', 1, 'admin', '2021-04-06 16:34:10', 'admin', '2021-04-06 16:34:10', 1);
INSERT INTO `gaea_dict_item` VALUES (254, 'MENU_LANGUAGE', '系统通告', 'Announcement', '', 1, 'zh', '', 1, 'admin', '2021-04-06 16:34:10', 'admin', '2021-04-06 16:34:10', 1);
INSERT INTO `gaea_dict_item` VALUES (255, 'REPORT_GROUP', '分组3', 'group_3', NULL, 1, 'zh', NULL, 3, 'admin', '2021-04-13 16:45:28', 'admin', '2021-04-13 16:45:28', 1);
INSERT INTO `gaea_dict_item` VALUES (257, 'MENU_LANGUAGE', '文件管理', 'FileManagement', NULL, 1, 'zh', NULL, 1, 'admin', '2021-04-27 13:57:58', 'admin', '2021-04-27 13:57:58', 1);
INSERT INTO `gaea_dict_item` VALUES (260, 'CHART_PROPERTIES', 'x轴字段', 'xAxis', NULL, 1, 'zh', NULL, 1, 'admin', '2022-08-17 18:06:18', 'admin', '2022-08-17 18:06:18', 1);
INSERT INTO `gaea_dict_item` VALUES (261, 'CHART_PROPERTIES', '柱状', 'bar', NULL, 1, 'zh', NULL, 6, 'admin', '2021-04-29 10:48:43', 'admin', '2021-04-29 10:48:50', 1);
INSERT INTO `gaea_dict_item` VALUES (262, 'CHART_PROPERTIES', '折线', 'line', NULL, 1, 'zh', NULL, 7, 'admin', '2021-04-29 10:48:43', 'admin', '2021-04-29 10:48:50', 1);
INSERT INTO `gaea_dict_item` VALUES (263, 'CHART_PROPERTIES', '饼图/仪表盘/气泡地图name', 'name', NULL, 1, 'zh', NULL, 8, 'admin', '2021-04-29 10:48:43', 'admin', '2021-04-29 10:48:50', 1);
INSERT INTO `gaea_dict_item` VALUES (264, 'CHART_PROPERTIES', '饼图/仪表盘/气泡地图value', 'value', NULL, 1, 'zh', NULL, 9, 'admin', '2021-04-29 10:48:43', 'admin', '2021-04-29 10:48:50', 1);
INSERT INTO `gaea_dict_item` VALUES (265, 'CHART_PROPERTIES', '时间轴-时', 'xAxis-hour', NULL, 1, 'zh', NULL, 2, 'admin', '2021-04-29 10:48:43', 'admin', '2021-04-29 10:48:50', 1);
INSERT INTO `gaea_dict_item` VALUES (266, 'CHART_PROPERTIES', '时间轴-天', 'xAxis-day', NULL, 1, 'zh', NULL, 3, 'admin', '2021-04-29 10:48:43', 'admin', '2021-04-29 10:48:50', 1);
INSERT INTO `gaea_dict_item` VALUES (267, 'CHART_PROPERTIES', '时间轴-月', 'xAxis-month', NULL, 1, 'zh', NULL, 4, 'admin', '2021-04-29 10:48:43', 'admin', '2021-04-29 10:48:50', 1);
INSERT INTO `gaea_dict_item` VALUES (268, 'CHART_PROPERTIES', '时间轴-年', 'xAxis-year', NULL, 1, 'zh', NULL, 5, 'admin', '2021-04-29 10:48:43', 'admin', '2021-04-29 10:48:50', 1);
INSERT INTO `gaea_dict_item` VALUES (269, 'CHART_PROPERTIES', '文本数字', 'text', NULL, 1, 'zh', NULL, 10, 'admin', '2022-08-17 18:06:18', 'admin', '2022-08-17 18:06:18', 1);
INSERT INTO `gaea_dict_item` VALUES (270, 'SOURCE_TYPE', 'mysql', 'mysql', '[{\"label\":\"driverName\",\"value\":\"com.mysql.cj.jdbc.Driver\",\"labelValue\":\"驱动类\"},{\"label\":\"jdbcUrl\",\"value\":\"jdbc:mysql://127.0.0.1:3306/test_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8\",\"labelValue\":\"连接串\"},{\"label\":\"username\",\"value\":\"root\",\"labelValue\":\"用户名\"},{\"label\":\"password\",\"value\":\"root\",\"labelValue\":\"密码\"}]', 1, 'zh', NULL, 1, '管理员', '2021-03-23 10:54:08', 'lixiaoyan', '2021-03-26 13:22:05', 3);
INSERT INTO `gaea_dict_item` VALUES (271, 'SOURCE_TYPE', 'oracle', 'oracle', '[{\"label\":\"driverName\",\"value\":\"oracle.jdbc.driver.OracleDriver\",\"labelValue\":\"驱动类\"},{\"label\":\"jdbcUrl\",\"value\":\"jdbc:oracle:thin:@//localhost:1521/orcl\",\"labelValue\":\"连接串\"},{\"label\":\"username\",\"value\":\"root\",\"labelValue\":\"用户名\"},{\"label\":\"password\",\"value\":\"root\",\"labelValue\":\"密码\"}]', 1, 'zh', NULL, 2, 'admin', '2021-03-23 10:54:08', 'admin', '2021-03-23 10:54:08', 1);
INSERT INTO `gaea_dict_item` VALUES (272, 'SOURCE_TYPE', 'mssqlserver', 'mssqlserver', '[{\"label\":\"driverName\",\"value\":\"com.microsoft.sqlserver.jdbc.SQLServerDriver\",\"labelValue\":\"驱动类\"},{\"label\":\"jdbcUrl\",\"value\":\"jdbc:sqlserver://127.0.0.1:1433;DatabaseName=test_db\",\"labelValue\":\"连接串\"},{\"label\":\"username\",\"value\":\"root\",\"labelValue\":\"用户名\"},{\"label\":\"password\",\"value\":\"root\",\"labelValue\":\"密码\"}]', 1, 'zh', NULL, 3, 'admin', '2021-03-23 10:54:08', 'admin', '2021-03-23 10:54:08', 1);
INSERT INTO `gaea_dict_item` VALUES (273, 'SOURCE_TYPE', 'elasticsearch_sql', 'elasticsearch_sql', '[{\"label\":\"apiUrl\",\"value\":\"http://10.108.26.164:9200/_xpack/sql?format=json\",\"labelValue\":\"请求路径\"},{\"label\":\"method\",\"value\":\"POST\",\"labelValue\":\"请求方式\"},{\"label\":\"header\",\"value\":\"{\\\"Content-Type\\\":\\\"application/json\\\"}\",\"labelValue\":\"请求头\"},{\"label\":\"body\",\"value\":\"{\\\"query\\\":\\\"select 1\\\"}\",\"labelValue\":\"请求体\"}]', 1, 'zh', NULL, 11, '管理员', '2021-03-23 10:54:08', 'admin', '2021-04-13 13:12:33', 9);
INSERT INTO `gaea_dict_item` VALUES (274, 'SOURCE_TYPE', 'kudu impala', 'kudu_impala', '[{\"label\":\"driverName\",\"value\":\"com.cloudera.impala.jdbc41.Driver\",\"labelValue\":\"驱动类\"},{\"label\":\"jdbcUrl\",\"value\":\"jdbc:impala://10.108.3.111:21050/ods\",\"labelValue\":\"连接串\"},{\"label\":\"username\",\"value\":\"\",\"labelValue\":\"用户名\"},{\"label\":\"password\",\"value\":\"\",\"labelValue\":\"密码\"}]', 1, 'zh', NULL, 12, 'admin', '2021-03-23 10:54:08', 'admin', '2021-04-01 09:18:09', 3);
INSERT INTO `gaea_dict_item` VALUES (275, 'SOURCE_TYPE', 'jdbc', 'jdbc', '[{\"label\":\"driverName\",\"value\":\"com.mysql.cj.jdbc.Driver\",\"labelValue\":\"驱动类\"},{\"label\":\"jdbcUrl\",\"value\":\"jdbc:mysql://127.0.0.1:3306/test_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8\",\"labelValue\":\"连接串\"},{\"label\":\"username\",\"value\":\"root\",\"labelValue\":\"用户名\"},{\"label\":\"password\",\"value\":\"root\",\"labelValue\":\"密码\"}]', 1, 'zh', NULL, 21, '管理员', '2021-04-13 17:26:38', 'admin', '2021-04-13 17:26:38', 1);
INSERT INTO `gaea_dict_item` VALUES (276, 'SOURCE_TYPE', 'http', 'http', '[{\"label\":\"apiUrl\",\"value\":\"https://gateway.test.com/api/getdata\",\"labelValue\":\"请求路径\"},{\"label\":\"method\",\"value\":\"POST\",\"labelValue\":\"请求方式\"},{\"label\":\"header\",\"value\":\"{\\\"Content-Type\\\":\\\"application/json;charset=UTF-8\\\"}\",\"labelValue\":\"请求头\"},{\"label\":\"body\",\"value\":\"{\\\"username\\\":\\\"admin\\\",\\\"password\\\":\\\"de12878c0ef5beb7d8848c3af8f54afb\\\",\\\"verifyCode\\\":\\\"\\\"}\",\"labelValue\":\"请求体\"}]', 1, 'zh', NULL, 29, '管理员', '2021-04-13 17:26:38', 'admin', '2021-04-13 17:26:38', 1);
INSERT INTO `gaea_dict_item` VALUES (288, 'CHART_PROPERTIES', 'y轴字段', 'yAxis', NULL, 1, 'zh', NULL, 11, 'admin', '2021-07-05 15:33:59', 'admin', '2021-07-05 15:33:59', 1);
INSERT INTO `gaea_dict_item` VALUES (289, 'SHARE_VAILD', '永久有效', '0', NULL, 1, 'zh', NULL, 1, 'admin', '2021-08-18 13:30:21', 'admin', '2021-08-18 13:30:21', 1);
INSERT INTO `gaea_dict_item` VALUES (290, 'SHARE_VAILD', '1天', '1', NULL, 1, 'zh', NULL, 2, 'admin', '2021-08-18 13:30:39', 'admin', '2021-08-18 13:30:39', 1);
INSERT INTO `gaea_dict_item` VALUES (291, 'SHARE_VAILD', '7天', '7', NULL, 1, 'zh', NULL, 3, 'admin', '2021-08-18 13:30:51', 'admin', '2021-08-18 13:30:56', 2);
INSERT INTO `gaea_dict_item` VALUES (292, 'SHARE_VAILD', '30天', '30', NULL, 1, 'zh', NULL, 4, 'admin', '2021-08-18 13:31:11', 'admin', '2021-08-18 13:31:11', 1);
INSERT INTO `gaea_dict_item` VALUES (293, 'BAR_PROPERTIES', 'x轴字段', 'xAxis', NULL, 1, 'zh', NULL, 1, 'admin', '2021-08-20 10:19:35', 'admin', '2021-08-20 10:19:35', 1);
INSERT INTO `gaea_dict_item` VALUES (294, 'BAR_PROPERTIES', '柱状', 'bar', NULL, 1, 'zh', NULL, 2, 'admin', '2021-08-20 10:19:56', 'admin', '2021-08-20 10:19:56', 1);
INSERT INTO `gaea_dict_item` VALUES (295, 'LINE_PROPERTIES', 'x轴字段', 'xAxis', NULL, 1, 'zh', NULL, 1, 'admin', '2021-08-20 10:27:39', 'admin', '2021-08-20 10:27:39', 1);
INSERT INTO `gaea_dict_item` VALUES (296, 'LINE_PROPERTIES', '折线', 'line', NULL, 1, 'zh', NULL, 2, 'admin', '2021-08-20 10:27:49', 'admin', '2021-08-20 10:27:49', 1);
INSERT INTO `gaea_dict_item` VALUES (297, 'BAR_LINE_PROPERTIES', 'x轴字段', 'xAxis', NULL, 1, 'zh', NULL, 1, 'admin', '2021-08-20 10:31:51', 'admin', '2021-08-20 10:31:51', 1);
INSERT INTO `gaea_dict_item` VALUES (298, 'BAR_LINE_PROPERTIES', 'x轴时间轴-时', 'xAxis-hour', NULL, 1, 'zh', NULL, 2, 'admin', '2021-08-20 10:32:11', 'admin', '2021-08-20 10:32:11', 1);
INSERT INTO `gaea_dict_item` VALUES (299, 'BAR_LINE_PROPERTIES', 'x轴时间轴-天', 'xAxis-day', NULL, 1, 'zh', NULL, 3, 'admin', '2021-08-20 10:32:25', 'admin', '2021-08-20 10:32:25', 1);
INSERT INTO `gaea_dict_item` VALUES (300, 'BAR_LINE_PROPERTIES', 'x轴时间轴-月', 'xAxis-month', NULL, 1, 'zh', NULL, 4, 'admin', '2021-08-20 10:32:38', 'admin', '2021-08-20 10:32:38', 1);
INSERT INTO `gaea_dict_item` VALUES (301, 'BAR_LINE_PROPERTIES', '时间轴-年', 'xAxis-year', NULL, 1, 'zh', NULL, 5, 'admin', '2021-08-20 10:32:52', 'admin', '2021-08-20 10:32:52', 1);
INSERT INTO `gaea_dict_item` VALUES (302, 'BAR_LINE_PROPERTIES', '柱状', 'bar', NULL, 1, 'zh', NULL, 6, 'admin', '2021-08-20 10:33:02', 'admin', '2021-08-20 10:33:02', 1);
INSERT INTO `gaea_dict_item` VALUES (303, 'BAR_LINE_PROPERTIES', '折线', 'line', NULL, 1, 'zh', NULL, 7, 'admin', '2021-08-20 10:33:11', 'admin', '2021-08-20 10:33:11', 1);
INSERT INTO `gaea_dict_item` VALUES (304, 'PIE_PROPERTIES', '名称name', 'name', NULL, 1, 'zh', NULL, 1, 'admin', '2021-08-20 10:35:27', 'admin', '2021-08-20 10:35:27', 1);
INSERT INTO `gaea_dict_item` VALUES (305, 'PIE_PROPERTIES', '数值value', 'value', NULL, 1, 'zh', NULL, 2, 'admin', '2021-08-20 10:35:38', 'admin', '2021-08-20 10:35:38', 1);
INSERT INTO `gaea_dict_item` VALUES (306, 'TEXT_PROPERTIES', '文本数字', 'text', NULL, 1, 'zh', NULL, 1, 'admin', '2021-08-20 10:36:04', 'admin', '2021-08-20 10:36:04', 1);
INSERT INTO `gaea_dict_item` VALUES (307, 'STACK_PROPERTIES', 'x轴字段', 'xAxis', NULL, 1, 'zh', NULL, 1, 'admin', '2021-08-20 10:31:51', 'admin', '2021-08-20 10:31:51', 1);
INSERT INTO `gaea_dict_item` VALUES (308, 'STACK_PROPERTIES', 'x轴时间轴-时', 'xAxis-hour', NULL, 1, 'zh', NULL, 2, 'admin', '2021-08-20 10:32:11', 'admin', '2021-08-20 10:32:11', 1);
INSERT INTO `gaea_dict_item` VALUES (309, 'STACK_PROPERTIES', 'x轴时间轴-天', 'xAxis-day', NULL, 1, 'zh', NULL, 3, 'admin', '2021-08-20 10:32:25', 'admin', '2021-08-20 10:32:25', 1);
INSERT INTO `gaea_dict_item` VALUES (310, 'STACK_PROPERTIES', 'x轴时间轴-月', 'xAxis-month', NULL, 1, 'zh', NULL, 4, 'admin', '2021-08-20 10:32:38', 'admin', '2021-08-20 10:32:38', 1);
INSERT INTO `gaea_dict_item` VALUES (311, 'STACK_PROPERTIES', '时间轴-年', 'xAxis-year', NULL, 1, 'zh', NULL, 5, 'admin', '2021-08-20 10:32:52', 'admin', '2021-08-20 10:32:52', 1);
INSERT INTO `gaea_dict_item` VALUES (312, 'STACK_PROPERTIES', 'y轴字段', 'yAxis', NULL, 1, 'zh', NULL, 6, 'admin', '2021-08-20 10:32:52', 'admin', '2021-08-20 10:32:52', 1);
INSERT INTO `gaea_dict_item` VALUES (313, 'STACK_PROPERTIES', '柱状', 'bar', NULL, 1, 'zh', NULL, 7, 'admin', '2021-08-20 10:33:02', 'admin', '2021-08-20 10:33:02', 1);
INSERT INTO `gaea_dict_item` VALUES (314, 'STACK_PROPERTIES', '折线', 'line', NULL, 1, 'zh', NULL, 8, 'admin', '2021-08-20 10:33:11', 'admin', '2021-08-20 10:33:11', 1);
INSERT INTO `gaea_dict_item` VALUES (315, 'MAP_PROPERTIES', '名称name', 'name', NULL, 1, 'zh', NULL, 1, 'admin', '2021-08-20 10:41:00', 'admin', '2021-08-20 10:41:00', 1);
INSERT INTO `gaea_dict_item` VALUES (316, 'MAP_PROPERTIES', '数值value', 'value', NULL, 1, 'zh', NULL, 2, 'admin', '2021-08-20 10:41:11', 'admin', '2021-08-20 10:41:11', 1);
INSERT INTO `gaea_dict_item` VALUES (317, 'SET_TYPE', 'sql', 'sql', NULL, 1, 'zh', NULL, NULL, 'admin', '2021-11-16 14:43:42', 'admin', '2021-11-16 14:43:42', 1);
INSERT INTO `gaea_dict_item` VALUES (318, 'SET_TYPE', 'http', 'http', NULL, 1, 'zh', NULL, NULL, 'admin', '2021-11-16 14:43:51', 'admin', '2021-11-16 14:43:51', 1);
INSERT INTO `gaea_dict_item` VALUES (319, 'COORD_PROPERTIES', '数据', 'series', NULL, 1, 'zh', NULL, NULL, 'admin', '2022-08-17 18:06:18', 'admin', '2022-08-17 18:06:18', 1);
INSERT INTO `gaea_dict_item` VALUES (320, 'COORD_PROPERTIES', 'X轴', 'xAxis', NULL, 1, 'zh', NULL, NULL, 'admin', '2022-08-17 18:06:18', 'admin', '2022-08-17 18:06:18', 1);
INSERT INTO `gaea_dict_item` VALUES (321, 'COORD_PROPERTIES', 'Y轴', 'yAxis', NULL, 1, 'zh', NULL, NULL, 'admin', '2022-08-17 18:06:18', 'admin', '2022-08-17 18:06:18', 1);
INSERT INTO `gaea_dict_item` VALUES (322, 'SOUTAR_PROPERTIES', '源端', 'source', NULL, 1, 'zh', NULL, NULL, 'admin', '2022-08-17 18:06:18', 'admin', '2022-08-17 18:06:18', 1);
INSERT INTO `gaea_dict_item` VALUES (323, 'SOUTAR_PROPERTIES', '目标端', 'target', NULL, 1, 'zh', NULL, NULL, 'admin', '2022-08-17 18:06:18', 'admin', '2022-08-17 18:06:18', 1);
INSERT INTO `gaea_dict_item` VALUES (324, 'SOUTAR_PROPERTIES', '数值', 'value', NULL, 1, 'zh', NULL, NULL, 'admin', '2022-08-17 18:06:18', 'admin', '2022-08-17 18:06:18', 1);
COMMIT;

-- ----------------------------
-- Table structure for gaea_file
-- ----------------------------
DROP TABLE IF EXISTS `gaea_file`;
CREATE TABLE `gaea_file` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `file_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '生成的唯一uuid',
                             `file_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件类型，字典FILE_TYPE',
                             `file_path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件在linux中的完整目录，比如/app/dist/export/excel/${fileid}.xlsx',
                             `url_path` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '通过接口的下载完整http路径',
                             `file_instruction` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '文件内容说明，比如 对账单(202001~202012)',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `create_time` timestamp NULL DEFAULT NULL,
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                             `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                             `version` int DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of gaea_file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gaea_report
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report`;
CREATE TABLE `gaea_report` (
                               `id` bigint NOT NULL AUTO_INCREMENT,
                               `report_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '名称',
                               `report_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '报表编码',
                               `report_group` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分组',
                               `report_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '报表类型',
                               `report_image` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '报表缩略图',
                               `report_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '报表描述',
                               `report_author` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '报表作者',
                               `download_count` bigint DEFAULT NULL COMMENT '报表下载次数',
                               `enable_flag` int DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                               `delete_flag` int DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                               `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                               `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                               `version` int DEFAULT NULL COMMENT '版本号',
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `UNIQUE_REPORT_CODE` (`report_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of gaea_report
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gaea_report_dashboard
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_dashboard`;
CREATE TABLE `gaea_report_dashboard` (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '看板id',
                                         `report_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表编码',
                                         `title` varchar(254) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '看板标题',
                                         `width` bigint DEFAULT NULL COMMENT '宽度px',
                                         `height` bigint DEFAULT NULL COMMENT '高度px',
                                         `background_color` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '背景色',
                                         `background_image` varchar(254) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '背景图片',
                                         `preset_line` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '工作台中的辅助线',
                                         `refresh_seconds` int DEFAULT NULL COMMENT '自动刷新间隔秒',
                                         `enable_flag` int DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                         `delete_flag` int DEFAULT '0' COMMENT ' 0--未删除 1--已删除 DIC_NAME=DEL_FLAG',
                                         `sort` int DEFAULT '0' COMMENT '排序，降序',
                                         `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                         `create_time` datetime DEFAULT NULL,
                                         `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                         `update_time` datetime DEFAULT NULL,
                                         `version` int DEFAULT NULL COMMENT '版本号',
                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE KEY `UNIQUE_REPORT_CODE` (`report_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of gaea_report_dashboard
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gaea_report_dashboard_widget
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_dashboard_widget`;
CREATE TABLE `gaea_report_dashboard_widget` (
                                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '组件id',
                                                `report_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '报表编码',
                                                `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '组件类型参考字典DASHBOARD_PANEL_TYPE',
                                                `setup` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '组件的渲染属性json',
                                                `data` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '组件的数据属性json',
                                                `collapse` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '组件的配置属性json',
                                                `position` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '组件的大小位置属性json',
                                                `options` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT 'options配置项',
                                                `refresh_seconds` int DEFAULT NULL COMMENT '自动刷新间隔秒',
                                                `enable_flag` int DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                                `delete_flag` int DEFAULT '0' COMMENT ' 0--未删除 1--已删除 DIC_NAME=DEL_FLAG',
                                                `sort` bigint DEFAULT '0' COMMENT '排序，图层的概念',
                                                `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                                `create_time` datetime DEFAULT NULL,
                                                `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                                                `update_time` datetime DEFAULT NULL,
                                                `version` int DEFAULT NULL COMMENT '版本号',
                                                PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of gaea_report_dashboard_widget
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gaea_report_data_set
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_data_set`;
CREATE TABLE `gaea_report_data_set` (
                                        `id` bigint NOT NULL AUTO_INCREMENT,
                                        `set_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据集编码',
                                        `set_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据集名称',
                                        `set_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据集描述',
                                        `source_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据源编码',
                                        `dyn_sentence` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '动态查询sql或者接口中的请求体',
                                        `case_result` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '结果案例',
                                        `enable_flag` int DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                        `delete_flag` int DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                        `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                        `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                        `version` int DEFAULT NULL,
                                        `set_type` varchar(10) DEFAULT NULL,
                                        PRIMARY KEY (`id`) USING BTREE,
                                        UNIQUE KEY `unique_set_code` (`set_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据集管理';

-- ----------------------------
-- Records of gaea_report_data_set
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gaea_report_data_set_param
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_data_set_param`;
CREATE TABLE `gaea_report_data_set_param` (
                                              `id` bigint NOT NULL AUTO_INCREMENT,
                                              `set_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据集编码',
                                              `param_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数名',
                                              `param_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数描述',
                                              `param_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数类型，字典=',
                                              `sample_item` varchar(1080) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '参数示例项',
                                              `required_flag` int DEFAULT '1' COMMENT '0--非必填 1--必填 DIC_NAME=REQUIRED_FLAG',
                                              `validation_rules` varchar(2048) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'js校验字段值规则，满足校验返回 true',
                                              `order_num` int DEFAULT NULL COMMENT '排序',
                                              `enable_flag` int DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                              `delete_flag` int DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                              `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                                              `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                              `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                                              `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                              `version` int DEFAULT NULL,
                                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据集查询参数';

-- ----------------------------
-- Records of gaea_report_data_set_param
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gaea_report_data_set_transform
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_data_set_transform`;
CREATE TABLE `gaea_report_data_set_transform` (
                                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                                  `set_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据集编码',
                                                  `transform_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据转换类型，DIC_NAME=TRANSFORM_TYPE; js，javaBean，字典转换',
                                                  `transform_script` varchar(10800) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据转换script,处理逻辑',
                                                  `order_num` int DEFAULT NULL COMMENT '排序,执行数据转换顺序',
                                                  `enable_flag` int DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                                  `delete_flag` int DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                                  `create_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                                                  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                  `update_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                                                  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                                  `version` int DEFAULT NULL,
                                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据集数据转换';

-- ----------------------------
-- Records of gaea_report_data_set_transform
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gaea_report_data_source
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_data_source`;
CREATE TABLE `gaea_report_data_source` (
                                           `id` bigint NOT NULL AUTO_INCREMENT,
                                           `source_code` varchar(100) DEFAULT NULL COMMENT '数据源编码',
                                           `source_name` varchar(100) DEFAULT NULL COMMENT '数据源名称',
                                           `source_desc` varchar(255) DEFAULT NULL COMMENT '数据源描述',
                                           `source_type` varchar(50) DEFAULT NULL COMMENT '数据源类型 DIC_NAME=SOURCE_TYPE; mysql，orace，sqlserver，elasticsearch，接口，javaBean，数据源类型字典中item-extend动态生成表单',
                                           `source_config` varchar(2048) DEFAULT NULL COMMENT '数据源连接配置json：关系库{ jdbcUrl:'''', username:'''', password:'''' } ES{ hostList:''ip1:9300,ip2:9300,ip3:9300'', clusterName:''elasticsearch_cluster'' }  接口{ apiUrl:''http://ip:port/url'', method:'''' } javaBean{ beanNamw:''xxx'' }',
                                           `enable_flag` int DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                           `delete_flag` int DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                           `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
                                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                           `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
                                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                           `version` int DEFAULT NULL,
                                           PRIMARY KEY (`id`) USING BTREE,
                                           UNIQUE KEY `unique_source_code` (`source_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据源管理';

-- ----------------------------
-- Records of gaea_report_data_source
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gaea_report_excel
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_excel`;
CREATE TABLE `gaea_report_excel` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `report_code` varchar(100) DEFAULT NULL COMMENT '报表编码',
                                     `set_codes` varchar(255) DEFAULT NULL COMMENT '数据集编码，以|分割',
                                     `set_param` varchar(1024) DEFAULT NULL COMMENT '数据集查询参数',
                                     `json_str` text COMMENT '报表json串',
                                     `enable_flag` int DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                     `delete_flag` int DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                     `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
                                     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                     `version` int DEFAULT NULL COMMENT '版本号',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE KEY `UNIQUE_REPORT_CODE` (`report_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gaea_report_excel
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gaea_report_share
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_share`;
CREATE TABLE `gaea_report_share` (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `share_code` varchar(50) DEFAULT NULL COMMENT '分享编码，系统生成，默认UUID',
                                     `share_valid_type` int DEFAULT NULL COMMENT '分享有效期类型，DIC_NAME=SHARE_VAILD',
                                     `share_valid_time` datetime DEFAULT NULL COMMENT '分享有效期',
                                     `share_token` varchar(255) DEFAULT NULL COMMENT '分享token',
                                     `share_url` varchar(100) DEFAULT NULL COMMENT '分享url',
                                     `share_password` varchar(10) DEFAULT NULL COMMENT '分享码',
                                     `report_code` varchar(50) DEFAULT NULL COMMENT '报表编码',
                                     `enable_flag` int DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
                                     `delete_flag` int DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
                                     `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
                                     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                     `version` int DEFAULT NULL,
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE KEY `UNIQUE_SHARE_CODE` (`share_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报表分享';

-- ----------------------------
-- Records of gaea_report_share
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
