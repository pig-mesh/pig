DROP DATABASE IF EXISTS `pig`;

CREATE DATABASE  `pig` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

USE `pig`;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept` ;
CREATE TABLE `sys_dept` (
                            `dept_id` bigint NOT NULL,
                            `name` varchar(50) COLLATE utf8_general_ci DEFAULT NULL COMMENT '部门名称',
                            `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
                            `del_flag` char(1) COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
                            `parent_id` bigint DEFAULT NULL,
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `create_by` varchar(64) COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            `update_by` varchar(64) COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                            PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES (1, '总经办', 0, '0', 0, '2020-03-13 13:13:16', ' ', '2020-03-13 13:14:31', ' ');
INSERT INTO `sys_dept` VALUES (2, '行政中心', 0, '0', 1, '2020-03-13 13:13:30', ' ', '2021-12-31 06:59:56', ' ');
INSERT INTO `sys_dept` VALUES (3, '技术中心', 0, '0', 1, '2020-03-13 13:14:55', ' ', '2021-12-31 06:59:56', ' ');
INSERT INTO `sys_dept` VALUES (4, '运营中心', 0, '0', 1, '2020-03-13 13:15:15', ' ', '2021-12-31 06:59:56', ' ');
INSERT INTO `sys_dept` VALUES (5, '研发中心', 0, '0', 3, '2020-03-13 13:15:34', ' ', '2021-12-31 06:59:56', ' ');
INSERT INTO `sys_dept` VALUES (6, '产品中心', 0, '0', 3, '2020-03-13 13:15:49', ' ', '2021-12-31 06:59:56', ' ');
INSERT INTO `sys_dept` VALUES (7, '测试中心', 0, '0', 3, '2020-03-13 13:16:02', ' ', '2021-12-31 06:59:56', ' ');
COMMIT;

-- ----------------------------
-- Table structure for sys_dept_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_relation`;
CREATE TABLE `sys_dept_relation` (
                                     `ancestor` bigint NOT NULL,
                                     `descendant` bigint NOT NULL,
                                     PRIMARY KEY (`ancestor`,`descendant`),
                                     KEY `idx1` (`ancestor`),
                                     KEY `idx2` (`descendant`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='部门关系表';

-- ----------------------------
-- Records of sys_dept_relation
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept_relation` VALUES (1, 1);
INSERT INTO `sys_dept_relation` VALUES (1, 2);
INSERT INTO `sys_dept_relation` VALUES (1, 3);
INSERT INTO `sys_dept_relation` VALUES (1, 4);
INSERT INTO `sys_dept_relation` VALUES (1, 5);
INSERT INTO `sys_dept_relation` VALUES (1, 6);
INSERT INTO `sys_dept_relation` VALUES (1, 7);
INSERT INTO `sys_dept_relation` VALUES (2, 2);
INSERT INTO `sys_dept_relation` VALUES (3, 3);
INSERT INTO `sys_dept_relation` VALUES (3, 5);
INSERT INTO `sys_dept_relation` VALUES (3, 6);
INSERT INTO `sys_dept_relation` VALUES (3, 7);
INSERT INTO `sys_dept_relation` VALUES (4, 4);
INSERT INTO `sys_dept_relation` VALUES (5, 5);
INSERT INTO `sys_dept_relation` VALUES (6, 6);
INSERT INTO `sys_dept_relation` VALUES (7, 7);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
                            `id` bigint NOT NULL,
                            `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '类型',
                            `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
                            `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
                            `system_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '是否是系统内置',
                            `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '删除标记',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                            `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `sys_dict_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES (1, 'dict_type', '字典类型', NULL, '0', '0', '2019-05-16 14:16:20', '', 'admin', '2021-12-29 12:29:18');
INSERT INTO `sys_dict` VALUES (2, 'log_type', '日志类型', NULL, '0', '0', '2020-03-13 14:21:01', '', 'admin', '2021-12-29 12:30:14');
INSERT INTO `sys_dict` VALUES (3, 'ds_type', '驱动类型', NULL, '0', '0', '2021-10-15 16:24:35', '', 'admin', '2021-12-29 12:30:18');
INSERT INTO `sys_dict` VALUES (4, 'param_type', '参数配置', '检索、原文、报表、安全、文档、消息、其他', '1', '0', '2022-03-25 20:51:26', 'admin', 'admin', '2022-03-25 20:51:26');
INSERT INTO `sys_dict` VALUES (5, 'status_type', '租户状态', '租户状态', '1', '0', '2022-03-25 20:56:51', 'admin', 'admin', '2022-03-25 20:56:51');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
                                 `id` bigint NOT NULL,
                                 `dict_id` bigint NOT NULL COMMENT '字典ID',
                                 `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '值',
                                 `label` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '标签',
                                 `type` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '字典类型',
                                 `description` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
                                 `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序（升序）',
                                 `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '备注',
                                 `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '删除标记',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                                 `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 KEY `sys_dict_value` (`value`) USING BTREE,
                                 KEY `sys_dict_label` (`label`) USING BTREE,
                                 KEY `sys_dict_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='字典项';

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_item` VALUES (1, 1, '1', '系统类', 'dict_type', '系统类字典', 0, ' ', '0', '2019-05-16 14:20:40', NULL, NULL, '2019-05-16 14:20:40');
INSERT INTO `sys_dict_item` VALUES (2, 1, '0', '业务类', 'dict_type', '业务类字典', 0, ' ', '0', '2019-05-16 14:20:59', NULL, NULL, '2019-05-16 14:20:59');
INSERT INTO `sys_dict_item` VALUES (3, 2, '0', '正常', 'log_type', '正常', 0, ' ', '0', '2020-03-13 14:23:22', NULL, NULL, '2020-03-13 14:23:22');
INSERT INTO `sys_dict_item` VALUES (4, 2, '9', '异常', 'log_type', '异常', 0, ' ', '0', '2020-03-13 14:23:35', NULL, NULL, '2020-03-13 14:23:35');
INSERT INTO `sys_dict_item` VALUES (5, 3, 'com.mysql.cj.jdbc.Driver', 'MYSQL8', 'ds_type', 'MYSQL8', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (6, 3, 'com.mysql.jdbc.Driver', 'MYSQL5', 'ds_type', 'MYSQL5', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (7, 3, 'oracle.jdbc.OracleDriver', 'Oracle', 'ds_type', 'Oracle', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (8, 3, 'org.mariadb.jdbc.Driver', 'mariadb', 'ds_type', 'mariadb', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (9, 3, 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'sqlserver2005+', 'ds_type', 'sqlserver2005+', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (10, 3, 'com.microsoft.jdbc.sqlserver.SQLServerDriver', 'sqlserver2000', 'ds_type', 'sqlserver2000', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (11, 3, 'com.ibm.db2.jcc.DB2Driver', 'db2', 'ds_type', 'db2', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (12, 3, 'org.postgresql.Driver', 'postgresql', 'ds_type', 'postgresql', 0, ' ', '0', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict_item` VALUES (13, 4, '1', '检索', 'param_type', '检索', 0, '检索', '0', '2022-03-25 20:51:51', 'admin', 'admin', '2022-03-25 20:51:51');
INSERT INTO `sys_dict_item` VALUES (14, 4, '2', '原文', 'param_type', '原文', 1, '原文', '0', '2022-03-25 20:52:06', 'admin', 'admin', '2022-03-25 20:52:06');
INSERT INTO `sys_dict_item` VALUES (15, 4, '3', '报表', 'param_type', '报表', 2, '报表', '0', '2022-03-25 20:52:16', 'admin', 'admin', '2022-03-25 20:52:16');
INSERT INTO `sys_dict_item` VALUES (16, 4, '4', '安全', 'param_type', '安全', 3, '安全', '0', '2022-03-25 20:52:32', 'admin', 'admin', '2022-03-25 20:52:32');
INSERT INTO `sys_dict_item` VALUES (17, 4, '5', '文档', 'param_type', '文档', 4, '文档', '0', '2022-03-25 20:52:52', 'admin', 'admin', '2022-03-25 20:52:52');
INSERT INTO `sys_dict_item` VALUES (18, 4, '6', '消息', 'param_type', '消息', 5, '消息', '0', '2022-03-25 20:53:07', 'admin', 'admin', '2022-03-25 20:53:07');
INSERT INTO `sys_dict_item` VALUES (19, 4, '9', '其他', 'param_type', '其他', 6, '其他', '0', '2022-03-25 20:54:50', 'admin', 'admin', '2022-03-25 20:54:50');
INSERT INTO `sys_dict_item` VALUES (20, 4, '0', '默认', 'param_type', '默认', 7, '默认', '0', '2022-03-25 20:55:23', 'admin', 'admin', '2022-03-25 20:55:23');
INSERT INTO `sys_dict_item` VALUES (21, 5, '0', '正常', 'status_type', '状态正常', 0, '状态正常', '0', '2022-03-25 20:57:12', 'admin', 'admin', '2022-03-25 20:57:12');
INSERT INTO `sys_dict_item` VALUES (22, 5, '9', '冻结', 'status_type', '状态冻结', 1, '状态冻结', '0', '2022-03-25 20:57:34', 'admin', 'admin', '2022-03-25 20:57:34');
COMMIT;


-- ----------------------------
-- Table structure for sys_public_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_public_param`;
CREATE TABLE `sys_public_param`  (
                                     `public_id` bigint(0) NOT NULL COMMENT '编号',
                                     `public_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                     `public_key` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                     `public_value` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                     `status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
                                     `validate_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                     `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
                                     `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
                                     `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                     `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
                                     `public_type` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
                                     `system_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0',
                                     PRIMARY KEY (`public_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_general_ci COMMENT = '公共参数配置表';

-- ----------------------------
-- Records of sys_public_param
-- ----------------------------
BEGIN;
INSERT INTO `sys_public_param` VALUES (1, '接口文档不显示的字段', 'GEN_HIDDEN_COLUMNS', 'tenant_id', '0', '', ' ', ' ', '2020-05-12 04:25:19', NULL, '9', '1');
INSERT INTO `sys_public_param` VALUES (2, '注册用户默认角色', 'USER_DEFAULT_ROLE', 'GENERAL_USER', '0', '', 'admin', 'admin', '2022-03-30 10:00:57', '2022-03-30 02:05:59', '2', '1');
COMMIT;


-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
                            `id` bigint NOT NULL,
                            `file_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `bucket_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `original` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `file_size` bigint DEFAULT NULL COMMENT '文件大小',
                            `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '0-正常，1-删除',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建者',
                            `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='文件管理表';

-- ----------------------------
-- Records of sys_file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
                           `id` bigint NOT NULL,
                           `type` char(1) COLLATE utf8_general_ci DEFAULT '1' COMMENT '日志类型',
                           `title` varchar(255) COLLATE utf8_general_ci DEFAULT '' COMMENT '日志标题',
                           `service_id` varchar(32) COLLATE utf8_general_ci DEFAULT NULL COMMENT '服务ID',
                           `remote_addr` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作IP地址',
                           `user_agent` varchar(1000) COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户代理',
                           `request_uri` varchar(255) COLLATE utf8_general_ci DEFAULT NULL COMMENT '请求URI',
                           `method` varchar(10) COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作方式',
                           `params` text COLLATE utf8_general_ci COMMENT '操作提交的数据',
                           `time` bigint DEFAULT NULL COMMENT '执行时间',
                           `del_flag` char(1) COLLATE utf8_general_ci DEFAULT '0' COMMENT '删除标记',
                           `exception` text COLLATE utf8_general_ci COMMENT '异常信息',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `create_by` varchar(64) COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                           `update_by` varchar(64) COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                           PRIMARY KEY (`id`),
                           KEY `sys_log_create_by` (`create_by`),
                           KEY `sys_log_request_uri` (`request_uri`),
                           KEY `sys_log_type` (`type`),
                           KEY `sys_log_create_date` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
                            `menu_id` bigint NOT NULL,
                            `name` varchar(32) COLLATE utf8_general_ci NOT NULL COMMENT '菜单名称',
                            `permission` varchar(32) COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单权限标识',
                            `path` varchar(128) COLLATE utf8_general_ci DEFAULT NULL COMMENT '前端URL',
                            `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID',
                            `icon` varchar(32) COLLATE utf8_general_ci DEFAULT NULL COMMENT '图标',
                            `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序值',
                            `keep_alive` char(1) COLLATE utf8_general_ci DEFAULT '0' COMMENT '0-开启，1- 关闭',
                            `type` char(1) COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单类型 （0菜单 1按钮）',
                            `del_flag` char(1) COLLATE utf8_general_ci DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)',
                            `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1000, '权限管理', NULL, '/admin', -1, 'icon-quanxianguanli', 1, '0', '0', '0', ' ', '2018-09-28 08:29:53', ' ', '2020-03-11 23:58:18');
INSERT INTO `sys_menu` VALUES (1100, '用户管理', NULL, '/admin/user/index', 1000, 'icon-yonghuguanli', 0, '0', '0', '0', ' ', '2017-11-02 22:24:37', ' ', '2020-03-12 00:12:57');
INSERT INTO `sys_menu` VALUES (1101, '用户新增', 'sys_user_add', NULL, 1100, NULL, 0, '0', '1', '0', ' ', '2017-11-08 09:52:09', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1102, '用户修改', 'sys_user_edit', NULL, 1100, NULL, 0, '0', '1', '0', ' ', '2017-11-08 09:52:48', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1103, '用户删除', 'sys_user_del', NULL, 1100, NULL, 0, '0', '1', '0', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1104, '导入导出', 'sys_user_import_export', NULL, 1100, NULL, 0, '0', '1', '0', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1200, '菜单管理', NULL, '/admin/menu/index', 1000, 'icon-caidanguanli', 1, '0', '0', '0', ' ', '2017-11-08 09:57:27', ' ', '2020-03-12 00:13:52');
INSERT INTO `sys_menu` VALUES (1201, '菜单新增', 'sys_menu_add', NULL, 1200, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:15:53', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1202, '菜单修改', 'sys_menu_edit', NULL, 1200, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:16:23', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1203, '菜单删除', 'sys_menu_del', NULL, 1200, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:16:43', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1300, '角色管理', NULL, '/admin/role/index', 1000, 'icon-jiaoseguanli', 2, '0', '0', '0', ' ', '2017-11-08 10:13:37', ' ', '2020-03-12 00:15:40');
INSERT INTO `sys_menu` VALUES (1301, '角色新增', 'sys_role_add', NULL, 1300, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:14:18', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1302, '角色修改', 'sys_role_edit', NULL, 1300, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:14:41', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1303, '角色删除', 'sys_role_del', NULL, 1300, NULL, 0, '0', '1', '0', ' ', '2017-11-08 10:14:59', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1304, '分配权限', 'sys_role_perm', NULL, 1300, NULL, 0, '0', '1', '0', ' ', '2018-04-20 07:22:55', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1305, '导入导出', 'sys_role_import_export', NULL, 1300, NULL, 0, '0', '1', '0', 'admin', '2022-03-21 11:14:52', 'admin', '2022-03-21 11:15:07');
INSERT INTO `sys_menu` VALUES (1400, '部门管理', NULL, '/admin/dept/index', 1000, 'icon-web-icon-', 3, '0', '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-12 00:15:44');
INSERT INTO `sys_menu` VALUES (1401, '部门新增', 'sys_dept_add', NULL, 1400, NULL, 0, '0', '1', '0', ' ', '2018-01-20 14:56:16', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1402, '部门修改', 'sys_dept_edit', NULL, 1400, NULL, 0, '0', '1', '0', ' ', '2018-01-20 14:56:59', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1403, '部门删除', 'sys_dept_del', NULL, 1400, NULL, 0, '0', '1', '0', ' ', '2018-01-20 14:57:28', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (1500, '岗位管理', '', '/admin/post/index', 1000, 'icon-guanwang', 4, '0', '0', '0', NULL, '2018-01-20 13:17:19', 'admin', '2022-03-15 17:32:06');
INSERT INTO `sys_menu` VALUES (1501, '岗位查看', 'sys_post_get', NULL, 1500, '1', 0, '0', '1', '0', NULL, '2018-05-15 21:35:18', 'admin', '2022-03-15 17:32:54');
INSERT INTO `sys_menu` VALUES (1502, '岗位新增', 'sys_post_add', NULL, 1500, '1', 1, '0', '1', '0', NULL, '2018-05-15 21:35:18', 'admin', '2022-03-15 17:32:48');
INSERT INTO `sys_menu` VALUES (1503, '岗位修改', 'sys_post_edit', NULL, 1500, '1', 2, '0', '1', '0', NULL, '2018-05-15 21:35:18', 'admin', '2022-03-15 17:33:10');
INSERT INTO `sys_menu` VALUES (1504, '岗位删除', 'sys_post_del', NULL, 1500, '1', 3, '0', '1', '0', NULL, '2018-05-15 21:35:18', 'admin', '2022-03-15 17:33:27');
INSERT INTO `sys_menu` VALUES (1505, '导入导出', 'sys_post_import_export', NULL, 1500, NULL, 4, '0', '1', '0', 'admin', '2022-03-21 12:53:05', 'admin', '2022-03-21 12:53:05');
INSERT INTO `sys_menu` VALUES (2000, '系统管理', NULL, '/setting', -1, 'icon-xitongguanli', 2, '0', '0', '0', ' ', '2017-11-07 20:56:00', ' ', '2020-03-11 23:52:53');
INSERT INTO `sys_menu` VALUES (2100, '日志管理', NULL, '/admin/log/index', 2000, 'icon-rizhiguanli', 3, '0', '0', '0', ' ', '2017-11-20 14:06:22', ' ', '2020-03-12 00:15:49');
INSERT INTO `sys_menu` VALUES (2101, '日志删除', 'sys_log_del', NULL, 2100, NULL, 0, '0', '1', '0', ' ', '2017-11-20 20:37:37', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2102, '导入导出', 'sys_log_import_export', NULL, 2100, NULL, 0, '0', '1', '0', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2200, '字典管理', NULL, '/admin/dict/index', 2000, 'icon-navicon-zdgl', 2, '0', '0', '0', ' ', '2017-11-29 11:30:52', ' ', '2020-03-12 00:15:58');
INSERT INTO `sys_menu` VALUES (2201, '字典删除', 'sys_dict_del', NULL, 2200, NULL, 0, '0', '1', '0', ' ', '2017-11-29 11:30:11', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2202, '字典新增', 'sys_dict_add', NULL, 2200, NULL, 0, '0', '1', '0', ' ', '2018-05-11 22:34:55', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2203, '字典修改', 'sys_dict_edit', NULL, 2200, NULL, 0, '0', '1', '0', ' ', '2018-05-11 22:36:03', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2300, '令牌管理', NULL, '/admin/token/index', 2000, 'icon-denglvlingpai', 4, '0', '0', '0', ' ', '2018-09-04 05:58:41', ' ', '2020-03-13 12:57:25');
INSERT INTO `sys_menu` VALUES (2301, '令牌删除', 'sys_token_del', NULL, 2300, NULL, 0, '0', '1', '0', ' ', '2018-09-04 05:59:50', ' ', '2020-03-13 12:57:34');
INSERT INTO `sys_menu` VALUES (2400, '终端管理', '', '/admin/client/index', 2000, 'icon-shouji', 0, '0', '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-12 00:15:54');
INSERT INTO `sys_menu` VALUES (2401, '客户端新增', 'sys_client_add', NULL, 2400, '1', 0, '0', '1', '0', ' ', '2018-05-15 21:35:18', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2402, '客户端修改', 'sys_client_edit', NULL, 2400, NULL, 0, '0', '1', '0', ' ', '2018-05-15 21:37:06', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2403, '客户端删除', 'sys_client_del', NULL, 2400, NULL, 0, '0', '1', '0', ' ', '2018-05-15 21:39:16', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2600, '文件管理', NULL, '/admin/file/index', 2000, 'icon-wenjianguanli', 1, '0', '0', '0', ' ', '2018-06-26 10:50:32', ' ', '2019-02-01 20:41:30');
INSERT INTO `sys_menu` VALUES (2601, '文件删除', 'sys_file_del', NULL, 2600, NULL, 0, '0', '1', '0', ' ', '2017-11-29 11:30:11', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2602, '文件新增', 'sys_file_add', NULL, 2600, NULL, 0, '0', '1', '0', ' ', '2018-05-11 22:34:55', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2603, '文件修改', 'sys_file_edit', NULL, 2600, NULL, 0, '0', '1', '0', ' ', '2018-05-11 22:36:03', ' ', '2021-05-25 06:48:34');
INSERT INTO `sys_menu` VALUES (2700, '参数管理', NULL, '/admin/param/index', 2000, 'icon-navicon-zdgl', 5, '0', '0', '0', 'admin', '2022-03-25 20:40:27', 'admin', '2022-03-25 20:40:35');
INSERT INTO `sys_menu` VALUES (2701, '参数新增', 'sys_publicparam_add', NULL, 2700, NULL, 0, '0', '1', '0', 'admin', '2022-03-25 20:45:05', 'admin', '2022-03-25 20:45:05');
INSERT INTO `sys_menu` VALUES (2702, '参数删除', 'sys_publicparam_del', NULL, 2700, NULL, 1, '0', '1', '0', 'admin', '2022-03-25 20:45:43', 'admin', '2022-03-25 20:45:43');
INSERT INTO `sys_menu` VALUES (2703, '参数修改', 'sys_publicparam_edit', NULL, 2700, NULL, 3, '0', '1', '0', 'admin', '2022-03-25 20:46:04', 'admin', '2022-03-25 20:46:04');
INSERT INTO `sys_menu` VALUES (3000, '开发平台', NULL, '/gen', -1, 'icon-shejiyukaifa-', 3, '1', '0', '0', ' ', '2020-03-11 22:15:40', ' ', '2020-03-11 23:52:54');
INSERT INTO `sys_menu` VALUES (3100, '数据源管理', NULL, '/gen/datasource', 3000, 'icon-mysql', 3, '1', '0', '0', ' ', '2020-03-11 22:17:05', ' ', '2020-03-12 00:16:09');
INSERT INTO `sys_menu` VALUES (3200, '代码生成', NULL, '/gen/index', 3000, 'icon-weibiaoti46', 0, '0', '0', '0', ' ', '2020-03-11 22:23:42', ' ', '2020-03-12 00:16:14');
INSERT INTO `sys_menu` VALUES (3300, '表单管理', NULL, '/gen/form', 3000, 'icon-record', 1, '1', '0', '0', ' ', '2020-03-11 22:19:32', ' ', '2020-03-12 00:16:18');
INSERT INTO `sys_menu` VALUES (3301, '表单新增', 'gen_form_add', NULL, 3300, '', 0, '0', '1', '0', ' ', '2018-05-15 21:35:18', ' ', '2020-03-11 22:39:08');
INSERT INTO `sys_menu` VALUES (3302, '表单修改', 'gen_form_edit', NULL, 3300, '', 0, '0', '1', '0', ' ', '2018-05-15 21:35:18', ' ', '2020-03-11 22:39:09');
INSERT INTO `sys_menu` VALUES (3303, '表单删除', 'gen_form_del', NULL, 3300, '', 0, '0', '1', '0', ' ', '2018-05-15 21:35:18', ' ', '2020-03-11 22:39:11');
INSERT INTO `sys_menu` VALUES (3400, '表单设计', NULL, '/gen/design', 3000, 'icon-biaodanbiaoqian', 2, '1', '0', '0', ' ', '2020-03-11 22:18:05', ' ', '2020-03-12 00:16:25');
INSERT INTO `sys_menu` VALUES (4000, '服务监控', NULL, 'http://localhost:5001/login', -1, 'icon-zhexiantu', 4, '0', '0', '0', 'admin', '2022-03-21 09:44:50', 'admin', '2022-03-21 09:47:06');
INSERT INTO `sys_menu` VALUES (9999, '系统官网', NULL, 'https://pig4cloud.com/#/', -1, 'icon-guanwangfangwen', 999, '0', '0', '0', ' ', '2019-01-17 17:05:19', 'admin', '2020-03-11 23:52:57');
COMMIT;

-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details` (
                                            `client_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端ID',
                                            `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源列表',
                                            `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '客户端密钥',
                                            `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '域',
                                            `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '认证类型',
                                            `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '重定向地址',
                                            `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色列表',
                                            `access_token_validity` int DEFAULT NULL COMMENT 'token 有效期',
                                            `refresh_token_validity` int DEFAULT NULL COMMENT '刷新令牌有效期',
                                            `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '令牌扩展字段JSON',
                                            `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '是否自动放行',
                                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                            `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                                            `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                                            PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='终端信息表';

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details` VALUES ('app', NULL, 'app', 'server', 'app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('daemon', NULL, 'daemon', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('gen', NULL, 'gen', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('pig', NULL, 'pig', 'server', 'password,app,refresh_token,authorization_code,client_credentials', 'http://localhost:4040/sso1/login,http://localhost:4041/sso1/login', NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES ('test', NULL, 'test', 'server', 'password,app,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true', NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
                             `post_id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
                             `post_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位编码',
                             `post_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '岗位名称',
                             `post_sort` int(0) NOT NULL COMMENT '岗位排序',
                             `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '创建人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '更新人',
                             `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
                             PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT = '岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` VALUES (1, 'user', '员工', 2, '0', '2022-03-19 10:05:15', 'admin', '2022-03-19 10:42:28', 'admin', '打工人');
INSERT INTO `sys_post` VALUES (2, 'cto', 'cto', 0, '0', '2022-03-19 10:06:20', 'admin', '2022-03-19 10:06:20', 'admin', 'cto666');
INSERT INTO `sys_post` VALUES (3, 'boss', '董事长', -1, '0', '2022-03-19 10:06:35', 'admin', '2022-03-19 10:42:44', 'admin', '大boss');
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
                                  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                  `post_id` bigint(0) NOT NULL COMMENT '岗位ID',
                                  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT = '用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_post` VALUES (1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                            `role_id` bigint NOT NULL,
                            `role_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `role_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `role_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
                            `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '删除标识（0-正常,1-删除）',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '修改人',
                            `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                            PRIMARY KEY (`role_id`),
                            UNIQUE KEY `role_idx1_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', '管理员', '0', '2017-10-29 15:45:51', '2018-12-26 14:09:11', NULL, NULL);
INSERT INTO `sys_role` VALUES (2, '普通用户','GENERAL_USER', '普通用户', '0', '2022-03-30 09:59:24', '2022-03-30 09:59:24', 'admin', 'admin');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
                                 `role_id` bigint NOT NULL,
                                 `menu_id` bigint NOT NULL,
                                 PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES (1, 1000);
INSERT INTO `sys_role_menu` VALUES (1, 1100);
INSERT INTO `sys_role_menu` VALUES (1, 1101);
INSERT INTO `sys_role_menu` VALUES (1, 1102);
INSERT INTO `sys_role_menu` VALUES (1, 1103);
INSERT INTO `sys_role_menu` VALUES (1, 1104);
INSERT INTO `sys_role_menu` VALUES (1, 1200);
INSERT INTO `sys_role_menu` VALUES (1, 1201);
INSERT INTO `sys_role_menu` VALUES (1, 1202);
INSERT INTO `sys_role_menu` VALUES (1, 1203);
INSERT INTO `sys_role_menu` VALUES (1, 1300);
INSERT INTO `sys_role_menu` VALUES (1, 1301);
INSERT INTO `sys_role_menu` VALUES (1, 1302);
INSERT INTO `sys_role_menu` VALUES (1, 1303);
INSERT INTO `sys_role_menu` VALUES (1, 1304);
INSERT INTO `sys_role_menu` VALUES (1, 1305);
INSERT INTO `sys_role_menu` VALUES (1, 1400);
INSERT INTO `sys_role_menu` VALUES (1, 1401);
INSERT INTO `sys_role_menu` VALUES (1, 1402);
INSERT INTO `sys_role_menu` VALUES (1, 1403);
INSERT INTO `sys_role_menu` VALUES (1, 1500);
INSERT INTO `sys_role_menu` VALUES (1, 1501);
INSERT INTO `sys_role_menu` VALUES (1, 1502);
INSERT INTO `sys_role_menu` VALUES (1, 1503);
INSERT INTO `sys_role_menu` VALUES (1, 1504);
INSERT INTO `sys_role_menu` VALUES (1, 1505);
INSERT INTO `sys_role_menu` VALUES (1, 2000);
INSERT INTO `sys_role_menu` VALUES (1, 2100);
INSERT INTO `sys_role_menu` VALUES (1, 2101);
INSERT INTO `sys_role_menu` VALUES (1, 2102);
INSERT INTO `sys_role_menu` VALUES (1, 2200);
INSERT INTO `sys_role_menu` VALUES (1, 2201);
INSERT INTO `sys_role_menu` VALUES (1, 2202);
INSERT INTO `sys_role_menu` VALUES (1, 2203);
INSERT INTO `sys_role_menu` VALUES (1, 2300);
INSERT INTO `sys_role_menu` VALUES (1, 2301);
INSERT INTO `sys_role_menu` VALUES (1, 2400);
INSERT INTO `sys_role_menu` VALUES (1, 2401);
INSERT INTO `sys_role_menu` VALUES (1, 2402);
INSERT INTO `sys_role_menu` VALUES (1, 2403);
INSERT INTO `sys_role_menu` VALUES (1, 2600);
INSERT INTO `sys_role_menu` VALUES (1, 2601);
INSERT INTO `sys_role_menu` VALUES (1, 2602);
INSERT INTO `sys_role_menu` VALUES (1, 2603);
INSERT INTO `sys_role_menu` VALUES (1, 2700);
INSERT INTO `sys_role_menu` VALUES (1, 2701);
INSERT INTO `sys_role_menu` VALUES (1, 2702);
INSERT INTO `sys_role_menu` VALUES (1, 2703);
INSERT INTO `sys_role_menu` VALUES (1, 3000);
INSERT INTO `sys_role_menu` VALUES (1, 3100);
INSERT INTO `sys_role_menu` VALUES (1, 3200);
INSERT INTO `sys_role_menu` VALUES (1, 3300);
INSERT INTO `sys_role_menu` VALUES (1, 3301);
INSERT INTO `sys_role_menu` VALUES (1, 3302);
INSERT INTO `sys_role_menu` VALUES (1, 3303);
INSERT INTO `sys_role_menu` VALUES (1, 3400);
INSERT INTO `sys_role_menu` VALUES (1, 4000);
INSERT INTO `sys_role_menu` VALUES (1, 9999);
INSERT INTO `sys_role_menu` VALUES (2, 4000);
INSERT INTO `sys_role_menu` VALUES (2, 9999);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `user_id` bigint NOT NULL,
                            `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
                            `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
                            `salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '随机盐',
                            `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '简介',
                            `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
                            `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
                            `lock_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '0-正常，9-锁定',
                            `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '0-正常，1-删除',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建者',
                            `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                            PRIMARY KEY (`user_id`),
                            KEY `user_idx1_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$RpFJjxYiXdEsAGnWp/8fsOetMuOON96Ntk/Ym2M/RKRyU0GZseaDC', NULL, '17034642999', '', 1, '0', '0', '2018-04-20 07:15:18', '2019-01-31 14:29:07', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
                                 `user_id` bigint NOT NULL,
                                 `role_id` bigint NOT NULL,
                                 PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
